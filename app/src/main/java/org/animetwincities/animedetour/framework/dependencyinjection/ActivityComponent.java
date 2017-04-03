package org.animetwincities.animedetour.framework.dependencyinjection;

import org.animetwincities.animedetour.MainActivity;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidActivityModule;
import org.animetwincities.animedetour.guests.ui.GuestDetailActivity;
import org.animetwincities.animedetour.guests.ui.GuestDetailFragment;
import org.animetwincities.animedetour.guests.ui.GuestsFragment;
import org.animetwincities.animedetour.schedule.ScheduleFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
    modules = {
        AndroidActivityModule.class,
    }
)
public interface ActivityComponent
{
    /**
     * @deprecated This is needed to inject the base activity, but not
     *             child-activities of it. Create an inject method in this class
     *             for your specific activity instead!
     */
    @Deprecated
    void inject(BaseActivity baseActivity);

    /**
     * @deprecated This is needed to inject the base fragment, but not
     *             child-activities of it. Create an inject method in this class
     *             for your specific fragment instead!
     */
    @Deprecated
    void inject(BaseFragment baseFragment);

    void inject(MainActivity target);
    void inject(ScheduleFragment target);

    void inject(GuestsFragment target);
    void inject(GuestDetailActivity target);
    void inject(GuestDetailFragment target);
}
