package org.animetwincities.animedetour.schedule;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.BindView;
import com.inkapplications.logger.RxLogger;
import icepick.State;
import inkapplications.android.layoutinjector.Layout;
import io.reactivex.disposables.CompositeDisposable;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.threeten.bp.LocalDate;

import javax.inject.Inject;
import java.util.List;

/**
 * Shows a pager with each fragment representing one day of the schedule.
 */
@Layout(R.layout.schedule)
final public class ScheduleFragment extends BaseFragment
{
    @Inject
    ScheduleRepository scheduleRepository;

    @Inject
    RxLogger logger;

    @BindView(R.id.schedule_pager)
    ViewPager pager;

    @BindView(R.id.schedule_pager_tabs)
    TabLayout tabs;

    @BindView(R.id.schedule_list_empty)
    View emptyView;

    @BindView(R.id.schedule_loading_indicator)
    View loadingIndicator;

    @State
    int position = -1;

    private CompositeDisposable disposables;
    private MultiDayPagerAdapter adapter;

    @Override
    public void injectSelf(ActivityComponent component)
    {
        component.inject(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        adapter = new MultiDayPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        disposables = new CompositeDisposable();

        disposables.add(
            scheduleRepository.observeDays().subscribe(this::onDaysLoaded, this::onLoadError)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        position = pager.getCurrentItem();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        disposables.dispose();
    }

    private void onDaysLoaded(List<LocalDate> days)
    {
        loadingIndicator.setVisibility(View.GONE);

        if (days.isEmpty()) {
            logger.warn("List of days was empty");
            emptyView.setVisibility(View.VISIBLE);
        } else {
            logger.debug("Found %s days", days.size());
            emptyView.setVisibility(View.GONE);
            adapter.setDays(days);
            if (position != -1) {
                pager.setCurrentItem(position);
                position = -1;
            }
        }
    }

    private void onLoadError(Throwable error)
    {
        logger.error(error, "Problem loading schedule days");

        if (adapter.getCount() == 0) {
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }
}
