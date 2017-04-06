package org.animetwincities.animedetour.schedule;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.BindView;
import com.inkapplications.logger.RxLogger;
import icepick.State;
import inkapplications.android.layoutinjector.Layout;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.auth.User;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.threeten.bp.LocalDate;

import javax.inject.Inject;
import java.util.List;

/**
 * Display a list of the events that a user has favorited.
 */
@Layout(R.layout.event_list)
public class FavoritesFragment extends BaseFragment
{
    @Inject
    ScheduleRepository scheduleRepository;

    @Inject
    RxLogger logger;

    @Inject
    EventListAdapter adapter;

    @State
    LocalDate day;

    @BindView(R.id.event_list)
    ListView eventList;

    @BindView(R.id.events_loading_indicator)
    View loadingIndicator;

    @BindView(R.id.event_list_empty)
    View emptyView;

    @State
    int scrollPosition;

    private CompositeDisposable disposables;

    public FavoritesFragment() {}

    @Override
    public void onStart()
    {
        super.onStart();
        logger.debug("Created with day %s ", day);
        eventList.setAdapter(adapter);
        eventList.setOnItemClickListener(this::openEvent);

        disposables = new CompositeDisposable();
    }

    private void openEvent(AdapterView<?> adapterView, View view, int i, long l)
    {
        String id = adapter.getItem(i).getId();
        startActivity(EventDetailActivity.createIntent(this.getContext(), id));
    }

    @Override
    public void onNewUser(User user) {
        super.onNewUser(user);
        disposables.add(
            scheduleRepository.observeFavorites(user.getId())
                .observeOn(Schedulers.computation())
                .flatMap(favoriteList -> scheduleRepository.observeEvents()
                    .observeOn(Schedulers.computation())
                    .flatMap(events -> Observable.fromIterable(events)
                        .filter(event -> favoriteList.contains(event.getId()))
                        .toList()
                        .toObservable()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setEvents, this::loadError)
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        disposables.dispose();
    }

    @Override
    public void onPause() {
        super.onPause();
        scrollPosition = this.eventList.getFirstVisiblePosition();
        logger.debug("Saving scroll position: %s", scrollPosition);
    }

    @Override
    public void injectSelf(ActivityComponent component)
    {
        component.inject(this);
    }

    public void setEvents(List<Event> events)
    {
        logger.debug("Received %s events", events.size());
        eventList.setEmptyView(emptyView);
        loadingIndicator.setVisibility(View.GONE);
        adapter.setEvents(events);

        logger.debug("Restoring scroll position: %s", scrollPosition);
        eventList.setSelection(this.scrollPosition);
    }

    private void loadError(Throwable error)
    {
        logger.error(error, "Problem during event loading");
        eventList.setEmptyView(emptyView);
        loadingIndicator.setVisibility(View.GONE);
    }
}
