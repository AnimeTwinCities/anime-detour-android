package org.animetwincities.animedetour.framework.dependencyinjection.module;

import dagger.Module;
import dagger.Provides;
import inkapplicaitons.android.logger.ConsoleLogger;
import inkapplicaitons.android.logger.Logger;
import org.animetwincities.animedetour.framework.dependencyinjection.AvailableInDebug;

/**
 * Defines Debug-only Services.
 *
 * DEBUG FILE – There is a corresponding file for this in the release variant!
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@Module
public class DebugModule
{
    @Provides
    @AvailableInDebug
    public Logger getLogger()
    {
        return new ConsoleLogger(1);
    }
}
