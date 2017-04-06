package org.animetwincities.animedetour.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.BindView;
import com.inkapplications.logger.RxLogger;
import icepick.State;
import inkapplications.android.layoutinjector.Layout;
import io.reactivex.disposables.CompositeDisposable;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.AppConfig;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.auth.User;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.threeten.bp.LocalDate;

import javax.inject.Inject;
import java.util.List;

/**
 * Display a list of events for a single day.
 */
@Layout(R.layout.event_list)
public class DayFragment extends BaseFragment
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
    int scrollPosition = -1;

    private CompositeDisposable disposables;

    public DayFragment() {}

    public DayFragment(LocalDate day)
    {
        this.day = day;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        logger.debug("Created with day %s ", day);
        eventList.setAdapter(adapter);
        eventList.setOnItemClickListener(this::openEvent);

        disposables = new CompositeDisposable();
        disposables.addAll(
            scheduleRepository.observeEventsOnDay(day).subscribe(this::setEvents, this::loadError)
        );
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
            scheduleRepository.observeFavorites(user.getId()).subscribe(this::setFavorites, this::loadError)
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

        if (this.scrollPosition != -1) {
            eventList.setSelection(this.scrollPosition);
            scrollPosition = -1;
        }
    }

    public void setFavorites(List<String> events)
    {
        logger.debug("Received %s favorites", events.size());
        adapter.setFavorites(events);
    }

    private void loadError(Throwable error)
    {
        logger.error(error, "Problem during event loading");
        eventList.setEmptyView(emptyView);
        loadingIndicator.setVisibility(View.GONE);
    }
}
