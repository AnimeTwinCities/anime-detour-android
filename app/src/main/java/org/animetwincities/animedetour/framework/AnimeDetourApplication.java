package org.animetwincities.animedetour.framework;

import android.app.Application;
import android.content.res.Configuration;
import com.inkapplications.android.applicationlifecycle.ApplicationLifecycleSubscriber;
import inkapplicaitons.android.logger.Logger;
import org.animetwincities.animedetour.framework.dependencyinjection.ApplicationComponent;
import org.animetwincities.animedetour.framework.dependencyinjection.DaggerApplicationComponent;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidApplicationModule;

import javax.inject.Inject;

public class AnimeDetourApplication extends Application
{
    @Inject
    Logger logger;

    @Inject
    ApplicationLifecycleSubscriber applicationCallbackHook;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.initializeInjections();
        this.logger.trace("Application Lifecycle: #onCreate()");
        this.applicationCallbackHook.onCreate(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.logger.trace("Application Lifecycle: #onLowMemory()");
        this.applicationCallbackHook.onLowMemory(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        this.logger.trace("Application Lifecycle: #onTrimMemory()");
        this.applicationCallbackHook.onTrimMemory(this, level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.logger.trace("Application Lifecycle: #onTerminate()");
        this.applicationCallbackHook.onTerminate(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.logger.trace("Application Lifecycle: #onConfigurationChanged()");
    }

    /** Inject this class and set up global application component. */
    private void initializeInjections()
    {
        DaggerApplicationComponent.Builder builder = DaggerApplicationComponent.builder();
        builder.androidApplicationModule(new AndroidApplicationModule(this));

        this.applicationComponent = builder.build();
        this.applicationComponent.inject(this);
    }

    /**
     * Get the application component singleton for injecting sub-components.
     */
    public ApplicationComponent getApplicationComponent()
    {
        return this.applicationComponent;
    }
}
