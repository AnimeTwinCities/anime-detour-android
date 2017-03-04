package org.animetwincities.animedetour.framework.dependencyinjection.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.inkapplications.android.applicationlifecycle.ApplicationLifecycleSubscriber;
import dagger.Module;
import dagger.Provides;
import inkapplicaitons.android.logger.ConsoleLogger;
import inkapplicaitons.android.logger.Logger;
import okhttp3.Interceptor;
import org.animetwincities.animedetour.framework.dependencyinjection.AvailableInDebug;
import org.animetwincities.animedetour.framework.intitializer.StethoInitializer;

import java.util.Arrays;
import java.util.List;

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

    @Provides
    @AvailableInDebug
    public ApplicationLifecycleSubscriber getDebugApplicationSubscribers()
    {
        return new StethoInitializer();
    }

    @Provides
    @AvailableInDebug
    List<Interceptor> getDebugOkHttpInterceptors()
    {
        return Arrays.asList((Interceptor) new StethoInterceptor());
    }
}
