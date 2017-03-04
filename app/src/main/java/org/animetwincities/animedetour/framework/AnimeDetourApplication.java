package org.animetwincities.animedetour.framework;

import android.app.Application;
import android.content.res.Configuration;
import inkapplicaitons.android.logger.Logger;
import org.animetwincities.animedetour.framework.dependencyinjection.ApplicationComponent;
import org.animetwincities.animedetour.framework.dependencyinjection.DaggerApplicationComponent;

import javax.inject.Inject;

public class AnimeDetourApplication extends Application
{
    @Inject
    Logger logger;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.initializeInjections();
        this.logger.trace("Application Lifecycle: #onCreate()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.logger.trace("Application Lifecycle: #onLowMemory()");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        this.logger.trace("Application Lifecycle: #onTrimMemory()");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.logger.trace("Application Lifecycle: #onTerminate()");
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
