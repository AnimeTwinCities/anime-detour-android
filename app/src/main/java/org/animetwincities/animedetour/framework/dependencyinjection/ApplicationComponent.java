package org.animetwincities.animedetour.framework.dependencyinjection;

import dagger.Component;
import org.animetwincities.animedetour.framework.AnimeDetourApplication;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidActivityModule;
import org.animetwincities.animedetour.framework.dependencyinjection.module.Application3rdPartyModule;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidApplicationModule;
import org.animetwincities.animedetour.framework.dependencyinjection.module.DebugModule;

import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
        DebugModule.class,
        AndroidApplicationModule.class,
        Application3rdPartyModule.class,
    }
)
public interface ApplicationComponent
{
    void inject(AnimeDetourApplication application);

    ActivityComponent newActivityComponent(AndroidActivityModule module);
}
