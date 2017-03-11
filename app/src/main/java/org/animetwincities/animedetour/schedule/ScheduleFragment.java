package org.animetwincities.animedetour.schedule;

import inkapplicaitons.android.logger.Logger;
import inkapplications.android.layoutinjector.Layout;
import io.reactivex.disposables.CompositeDisposable;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.auth.User;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;

import javax.inject.Inject;
import java.util.List;

@Layout(R.layout.schedule)
public class ScheduleFragment extends BaseFragment
{
    @Inject
    ScheduleRepository scheduleRepository;

    @Inject
    Logger logger;

    private CompositeDisposable disposables;

    @Override
    public void injectSelf(ActivityComponent component)
    {
        component.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        disposables = new CompositeDisposable();
    }

    @Override
    public void onNewUser(User user) {
        super.onNewUser(user);
        disposables.add(
            scheduleRepository.observeEvents("ad-2017").subscribe(this::newEvents)
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        disposables.dispose();
    }

    public void newEvents(List<Event> events)
    {
        for (Event event : events) {
            logger.debug(event.toString());
        }
    }
}
