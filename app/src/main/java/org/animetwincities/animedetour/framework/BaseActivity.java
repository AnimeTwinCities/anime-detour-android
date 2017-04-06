package org.animetwincities.animedetour.framework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import butterknife.ButterKnife;
import com.inkapplications.logger.RxLogger;
import icepick.Icepick;
import inkapplications.android.layoutinjector.LayoutInjector;
import inkapplications.guava.Stopwatch;
import io.reactivex.disposables.CompositeDisposable;
import org.animetwincities.animedetour.framework.auth.AuthRepository;
import org.animetwincities.animedetour.framework.auth.User;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.animetwincities.animedetour.framework.dependencyinjection.ApplicationComponent;
import org.animetwincities.animedetour.framework.dependencyinjection.DaggerActivityComponentAware;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidActivityModule;

import javax.inject.Inject;

/**
 * Boilerplate activity pre-configured to run framework utilities.
 *
 * This class should not be used for re-usable logic. Not all activities must
 * extend this class, it is simply for convenience.
 */
abstract public class BaseActivity extends AppCompatActivity implements DaggerActivityComponentAware
{
    @Inject
    RxLogger logger;

    @Inject
    AuthRepository authRepository;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        Stopwatch timer = Stopwatch.createStarted();
        super.onCreate(savedInstanceState);

        this.initializeInjections();
        this.logger.trace("Activity Lifecycle: %s.onCreate()", this.getClass().getSimpleName());
        LayoutInjector.injectContentView(this);
        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);

        logger.debug("Activity onCreate took %s", timer.stop());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.logger.trace("Activity Lifecycle: %s.onStart()", this.getClass().getSimpleName());
        disposables.dispose();
        disposables = new CompositeDisposable();
        disposables.addAll(
            authRepository.signInAnonymously().subscribe(this::onNewUser, error -> logger.error(error, "Problem Signing In Anonymously"))
        );
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
    }

    /**
     * Invoked when a new User object is available.
     *
     * Any code dependant on the user object can be invoked from this method.
     * This is Invoked sometime after OnStart. if a listener is created here,
     * it is safest to unsubscribe int the corresponding onStop method.
     *
     * @param user The user that has been signed in. Never Null.
     */
    protected void onNewUser(@NonNull User user)
    {
        this.logger.trace("Activity Event: %s.onNewUser(%s)", this.getClass().getSimpleName(), user);
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
        disposables.dispose();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        this.logger.trace("Activity Lifecycle: %s.onSaveInstanceState()", this.getClass().getSimpleName());
        Icepick.saveInstanceState(this, outState);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }
}
