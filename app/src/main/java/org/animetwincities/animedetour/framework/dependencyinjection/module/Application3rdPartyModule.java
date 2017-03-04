package org.animetwincities.animedetour.framework.dependencyinjection.module;

import dagger.Module;
import dagger.Provides;
import inkapplicaitons.android.logger.CompositeLogger;
import inkapplicaitons.android.logger.Logger;
import org.animetwincities.animedetour.framework.dependencyinjection.AvailableInDebug;

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
    public Logger getLogger(@AvailableInDebug Logger debugLog)
    {
        List<Logger> loggers = Arrays.asList(debugLog);

        return new CompositeLogger(loggers);
    }
}
