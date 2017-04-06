package org.animetwincities.animedetour.framework.dependencyinjection.module;

import android.content.Context;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.inkapplications.android.applicationlifecycle.ApplicationCallbacks;
import com.inkapplications.android.applicationlifecycle.ApplicationLifecycleSubscriber;
import com.inkapplications.logger.RxLogAdapter;
import com.inkapplications.logger.RxLogger;
import dagger.Module;
import dagger.Provides;
import inkapplicaitons.android.logger.CompositeLogger;
import inkapplicaitons.android.logger.Logger;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.animetwincities.animedetour.framework.AppConfig;
import org.animetwincities.animedetour.framework.dependencyinjection.AvailableInDebug;
import org.animetwincities.animedetour.framework.firebase.FirebaseLogger;
import org.animetwincities.animedetour.framework.fresco.FrescoInitializer;
import org.animetwincities.animedetour.framework.threeten.ThreeTenInitializer;

import javax.inject.Singleton;
import java.io.File;
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
    public Logger getLogger(RxLogger logger)
    {
        return logger;
    }

    @Provides
    @Singleton
    public RxLogger getRxLogger(@AvailableInDebug Logger debugLog, FirebaseLogger firebaseLog)
    {
        List<Logger> loggers = Arrays.asList(debugLog, firebaseLog);

        return new RxLogAdapter(new CompositeLogger(loggers));
    }

    @Provides
    @Singleton
    ApplicationLifecycleSubscriber getApplicationSubscriber(
        @AvailableInDebug ApplicationLifecycleSubscriber debugSubscriber,
        FrescoInitializer fresco,
        ThreeTenInitializer threeten
    ) {
        return new ApplicationCallbacks(debugSubscriber, fresco, threeten);
    }

    @Provides
    @Singleton
    FirebaseDatabase getDatabase()
    {
        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        firebase.setPersistenceEnabled(true);

        return firebase;
    }

    @Provides
    @Singleton
    ImagePipeline imagePipeline()
    {
        return Fresco.getImagePipeline();
    }

    @Provides
    @Singleton
    OkHttpClient httpClient(Context context, @AvailableInDebug List<Interceptor> debugInterceptors)
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        File cacheDir = new File(context.getCacheDir(), "http");
        long cacheSize = 80 * 1024 * 1024; // 80MB
        builder.cache(new Cache(cacheDir, cacheSize));

        for (Interceptor interceptor : debugInterceptors) {
            builder.addNetworkInterceptor(interceptor);
        }

        return builder.build();
    }

    @Provides
    @Singleton
    FirebaseRemoteConfig getRemoteConfig()
    {
        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        config.setDefaults(AppConfig.getDefaultConfigMap());

        return config;
    }

    @Provides
    @Singleton
    FirebaseAuth getFirebaseAuth()
    {
        return FirebaseAuth.getInstance();
    }
}
