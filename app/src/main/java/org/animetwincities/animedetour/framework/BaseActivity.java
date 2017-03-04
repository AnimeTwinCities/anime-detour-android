package org.animetwincities.animedetour.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import icepick.Icepick;
import inkapplicaitons.android.logger.Logger;
import inkapplications.android.layoutinjector.LayoutInjector;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.animetwincities.animedetour.framework.dependencyinjection.ApplicationComponent;
import org.animetwincities.animedetour.framework.dependencyinjection.DaggerActivityComponentAware;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidActivityModule;
import org.animetwincities.animedetour.framework.stopwatch.LimitTimer;
import org.animetwincities.animedetour.framework.stopwatch.TimerFactory;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * Boilerplate activity pre-configured to run framework utilities.
 *
 * This class should not be used for re-usable logic. Not all activities must
 * extend this class, it is simply for convenience.
 */
abstract public class BaseActivity extends AppCompatActivity implements DaggerActivityComponentAware
{
    @Inject
    Logger logger;

    @Inject
    TimerFactory timerFactory;

    @Inject
    FirebaseRemoteConfig remoteConfig;

    @Inject
    AppConfig appConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.initializeInjections();
        LimitTimer timer = this.timerFactory.startForLimit("Activity Initialize", 500, TimeUnit.MILLISECONDS);
        this.logger.trace("Activity Lifecycle: %s.onCreate()", this.getClass().getSimpleName());
        LayoutInjector.injectContentView(this);
        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        remoteConfig.fetch().addOnCompleteListener(this::configComplete);

        timer.finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.logger.trace("Activity Lifecycle: %s.onStart()", this.getClass().getSimpleName());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.logger.info("Viewing: %s", this.getClass().getSimpleName());
        this.logger.trace("Activity Lifecycle: %s.onResume()", this.getClass().getSimpleName());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.logger.trace("Activity Lifecycle: %s.onPostResume()", this.getClass().getSimpleName());
        this.remoteConfig.activateFetched();
        this.onNewConfig(this.appConfig);
    }

    /**
     * Invoked when a new Application config is available.
     *
     * If the Activity has code that can be changed with a remote config, it
     * should be updated here.
     */
    protected void onNewConfig(AppConfig config)
    {
        this.logger.trace("Activity Lifecycle: %s.onNewConfig()", this.getClass().getSimpleName());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        this.logger.trace("Activity Lifecycle: %s.onPause()", this.getClass().getSimpleName());
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.logger.trace("Activity Lifecycle: %s.onStop()", this.getClass().getSimpleName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        this.logger.trace("Activity Lifecycle: %s.onSaveInstanceState()", this.getClass().getSimpleName());
        Icepick.saveInstanceState(this, outState);
    }

    /**
     * Invoked when Firebase Config load is complete.
     */
    private void configComplete(Task<Void> voidTask) {
        if (voidTask.isSuccessful()) {
            this.remoteConfig.activateFetched();
            this.onNewConfig(this.appConfig);
        } else {
            this.logger.error(voidTask.getException(), "Failed to load Firebase Config.");
        }
    }

    /**
     * Inject this class and invoke the child injector.
     */
    private void initializeInjections()
    {
        AnimeDetourApplication application = (AnimeDetourApplication) this.getApplication();
        ApplicationComponent applicationComponent = application.getApplicationComponent();
        ActivityComponent activityComponent = applicationComponent.newActivityComponent(new AndroidActivityModule(this));

        activityComponent.inject(this);
        this.injectSelf(activityComponent);
    }
}
