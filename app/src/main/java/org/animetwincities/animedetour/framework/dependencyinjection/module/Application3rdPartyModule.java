package org.animetwincities.animedetour.framework.dependencyinjection.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.inkapplications.android.applicationlifecycle.ApplicationCallbacks;
import com.inkapplications.android.applicationlifecycle.ApplicationLifecycleSubscriber;
import dagger.Module;
import dagger.Provides;
import inkapplicaitons.android.logger.CompositeLogger;
import inkapplicaitons.android.logger.Logger;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.animetwincities.animedetour.framework.dependencyinjection.AvailableInDebug;
import org.animetwincities.animedetour.framework.fresco.FrescoInitializer;

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
    ApplicationLifecycleSubscriber getApplicationSubscriber(
        @AvailableInDebug ApplicationLifecycleSubscriber debugSubscriber,
        FrescoInitializer fresco
    ) {
        return new ApplicationCallbacks(debugSubscriber, fresco);
    }

    @Provides
    @Singleton
    OkHttpClient httpClient(@AvailableInDebug List<Interceptor> debugInterceptors)
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        for (Interceptor interceptor : debugInterceptors) {
            builder.addInterceptor(interceptor);
        }

        return builder.build();
    }
}
