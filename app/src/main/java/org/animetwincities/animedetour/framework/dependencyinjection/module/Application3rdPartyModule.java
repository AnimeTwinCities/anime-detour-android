package org.animetwincities.animedetour.framework.dependencyinjection.module;

import com.inkapplications.android.applicationlifecycle.ApplicationCallbacks;
import com.inkapplications.android.applicationlifecycle.ApplicationLifecycleSubscriber;
import dagger.Module;
import dagger.Provides;
import inkapplicaitons.android.logger.CompositeLogger;
import inkapplicaitons.android.logger.Logger;
import org.animetwincities.animedetour.framework.dependencyinjection.AvailableInDebug;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

/**
 * Defines services provided by libraries brought into the project.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@Module
public class Application3rdPartyModule
{
    @Provides
    @Singleton
    public Logger getLogger(@AvailableInDebug Logger debugLog)
    {
        List<Logger> loggers = Arrays.asList(debugLog);

        return new CompositeLogger(loggers);
    }

    @Provides
    @Singleton
    ApplicationLifecycleSubscriber getApplicationSubscriber(@AvailableInDebug ApplicationLifecycleSubscriber debugSubscriber)
    {
        return new ApplicationCallbacks(debugSubscriber);
    }
}
