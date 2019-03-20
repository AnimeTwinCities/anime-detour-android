package org.animetwincities.animedetour.framework.dependencyinjection;

import org.animetwincities.animedetour.MainActivity;
import org.animetwincities.animedetour.SplashActivity;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidActivityModule;
import org.animetwincities.animedetour.guest.GuestDetailActivity;
import org.animetwincities.animedetour.guest.GuestDetailFragment;
import org.animetwincities.animedetour.guest.GuestIndexFragment;
import org.animetwincities.animedetour.schedule.DayFragment;
import org.animetwincities.animedetour.schedule.EventDetailActivity;
import org.animetwincities.animedetour.schedule.EventSearchActivity;
import org.animetwincities.animedetour.schedule.FavoritesFragment;
import org.animetwincities.animedetour.schedule.ScheduleFragment;

import dagger.Subcomponent;
import org.animetwincities.animedetour.settings.SettingsFragment;
import org.animetwincities.animedetour.privacypolicy.PrivacyPolicyFragment;

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
    void inject(SplashActivity target);
    void inject(GuestDetailActivity target);
    void inject(EventDetailActivity target);
    void inject(EventSearchActivity target);

    void inject(ScheduleFragment target);
    void inject(GuestIndexFragment target);
    void inject(GuestDetailFragment target);
    void inject(DayFragment target);
    void inject(FavoritesFragment target);
    void inject(SettingsFragment target);
    void inject(PrivacyPolicyFragment target);
}
