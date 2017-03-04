package org.animetwincities.animedetour.framework.dependencyinjection.module;

import com.inkapplications.android.applicationlifecycle.ApplicationCallbacks;
import com.inkapplications.android.applicationlifecycle.ApplicationLifecycleSubscriber;
import dagger.Module;
import dagger.Provides;
import inkapplicaitons.android.logger.EmptyLogger;
import inkapplicaitons.android.logger.Logger;
import org.animetwincities.animedetour.framework.dependencyinjection.AvailableInDebug;

/**
 * Defines Debug-only Services.
 *
 * Release FILE – There is a corresponding file for this in the release variant!
 *
 * THIS IS THE RELEASE FILE. THIS MODULE SHOULD PROVIDE STUB/EMPTY SERVICES ONLY!!
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
        return new EmptyLogger();
    }

    @Provides
    @AvailableInDebug
    public ApplicationLifecycleSubscriber getDebugApplicationSubscribers()
    {
        return new ApplicationCallbacks(/* Keep blank! No new code for release mode! */);
    }
}
