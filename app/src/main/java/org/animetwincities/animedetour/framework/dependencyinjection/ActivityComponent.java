package org.animetwincities.animedetour.framework.dependencyinjection;

import dagger.Subcomponent;
import org.animetwincities.animedetour.MainActivity;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidActivityModule;

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

    void inject(MainActivity target);
}
