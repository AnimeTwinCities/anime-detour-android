package org.animetwincities.animedetour;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.inkapplications.logger.RxLogger;
import inkapplications.android.layoutinjector.Layout;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import org.animetwincities.animedetour.framework.Animations;
import org.animetwincities.animedetour.framework.AppConfig;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.animetwincities.animedetour.guest.GuestRepository;
import org.animetwincities.animedetour.schedule.ScheduleRepository;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * A Splash screen for the app that shows at launch and as the app loads initial data.
 */
@Layout(R.layout.splash)
public class SplashActivity extends BaseActivity
{
    @Inject
    AppConfig appConfig;

    @Inject
    ScheduleRepository scheduleRepository;

    @Inject
    GuestRepository guestRepository;

    @Inject
    RxLogger logger;

    @BindView(R.id.splash_message)
    TextView message;

    private CompositeDisposable disposables;

    @Override
    public void injectSelf(ActivityComponent component)
    {
        component.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        disposables = new CompositeDisposable();

        disposables.addAll(
                appConfig.update()
                    .delay(appConfig.hasLoadedSchedule() ? 400 : 0, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onPostConfigRefresh, this::onConfigError)
        );
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        disposables.dispose();
    }

    private void onConfigError(Throwable throwable) {
        logger.error(throwable, "Error for App Config Load. Using old config.");
        onPostConfigRefresh();
    }

    private void onPreloadError(Throwable throwable) {
        logger.error(throwable, "Error during preloading");
        complete();
    }

    private void onPostConfigRefresh() {
        if (!appConfig.hasLoadedSchedule()) {
            Animations.fadeIn(message);
            message.setVisibility(View.VISIBLE);
        }

        preload();
    }

    private void preload() {
        Completable data = Completable.mergeArrayDelayError(
            scheduleRepository.observeEvents()
                .take(1)
                .ignoreElements()
                .timeout(30, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()),
            guestRepository.observeGuests()
                .take(1)
                .ignoreElements()
                .timeout(30, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()),
            guestRepository.preloadImages()
                .timeout(45, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
        );
        disposables.add(data.subscribe(this::dataLoadComplete, this::onPreloadError));
    }

    private void dataLoadComplete() {
        appConfig.setScheduleLoaded();
        complete();
    }

    private void complete() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
