package org.animetwincities.animedetour.framework.dependencyinjection;

import dagger.Subcomponent;
import org.animetwincities.animedetour.MainActivity;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidActivityModule;
import org.animetwincities.animedetour.schedule.ScheduleFragment;

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
}
