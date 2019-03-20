package org.animetwincities.animedetour.framework.dependencyinjection.module;

import android.content.*;

import com.facebook.drawee.backends.pipeline.*;
import com.facebook.imagepipeline.core.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.google.firebase.remoteconfig.*;
import com.inkapplications.android.applicationlifecycle.*;
import com.inkapplications.logger.*;

import org.animetwincities.animedetour.framework.*;
import org.animetwincities.animedetour.framework.dependencyinjection.*;
import org.animetwincities.animedetour.framework.firebase.*;
import org.animetwincities.animedetour.framework.fresco.*;
import org.animetwincities.animedetour.framework.threeten.*;

import java.io.*;
import java.util.*;

import javax.inject.*;

import dagger.*;
import inkapplicaitons.android.logger.*;
import inkapplicaitons.android.logger.Logger;
import okhttp3.*;

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
