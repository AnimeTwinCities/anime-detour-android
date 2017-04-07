package org.animetwincities.animedetour.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import icepick.Icepick;
import inkapplicaitons.android.logger.Logger;
import inkapplications.android.layoutinjector.LayoutInjector;
import inkapplications.android.layoutinjector.LayoutNotSpecifiedException;
import inkapplications.guava.Stopwatch;
import io.reactivex.disposables.CompositeDisposable;
import org.animetwincities.animedetour.framework.auth.AuthRepository;
import org.animetwincities.animedetour.framework.auth.User;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.animetwincities.animedetour.framework.dependencyinjection.ApplicationComponent;
import org.animetwincities.animedetour.framework.dependencyinjection.DaggerActivityComponentAware;
import org.animetwincities.animedetour.framework.dependencyinjection.module.AndroidActivityModule;

import javax.inject.Inject;

abstract public class BaseFragment extends Fragment implements DaggerActivityComponentAware
{
    @Inject
    Logger logger;

    @Inject
    AuthRepository auth;

    private CompositeDisposable disposables;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        Stopwatch timer = Stopwatch.createStarted();
        super.onActivityCreated(savedInstanceState);
        this.initializeInjections();
        this.logger.trace("Fragment Lifecycle: %s.onCreate()", this.getClass().getSimpleName());
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this, getView());

        logger.debug("Fragment onActivityCreated took %s", timer.stop());
        this.setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        try {
            return LayoutInjector.inflateContentView(this, inflater, container);
        } catch (LayoutNotSpecifiedException e) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        this.disposables = new CompositeDisposable();
        this.disposables.add(
            this.auth.signInAnonymously().subscribe(this::onNewUser, error -> {})
        );
    }

    public void onNewUser(User user)
    {
        this.logger.trace("Fragment received new user: %s", user.getId());
    }

    @Override
    public void onStop() {
        super.onStop();
        this.disposables.dispose();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this, outState);
    }

    private void initializeInjections()
    {
        AnimeDetourApplication application = (AnimeDetourApplication) this.getActivity().getApplication();
        ApplicationComponent applicationComponent = application.getApplicationComponent();
        AndroidActivityModule activityModule = new AndroidActivityModule((AppCompatActivity) this.getActivity());
        ActivityComponent activityComponent = applicationComponent.newActivityComponent(activityModule);

        activityComponent.inject(this);
        this.injectSelf(activityComponent);
    }
}
