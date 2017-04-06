package org.animetwincities.animedetour.framework.dependencyinjection;

import dagger.Component;
import org.animetwincities.animedetour.framework.AnimeDetourApplication;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidActivityModule;
import org.animetwincities.animedetour.framework.dependencyinjection.module.Application3rdPartyModule;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidApplicationModule;
import org.animetwincities.animedetour.framework.dependencyinjection.module.DebugModule;
import org.animetwincities.animedetour.map.HotelMapFragment;
import org.animetwincities.animedetour.schedule.notification.NotificationScheduler;
import org.animetwincities.animedetour.schedule.notification.UpcomingEventReciever;

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
    void inject(HotelMapFragment target);
    void inject(NotificationScheduler target);
    void inject(UpcomingEventReciever target);

    ActivityComponent newActivityComponent(AndroidActivityModule module);
}
