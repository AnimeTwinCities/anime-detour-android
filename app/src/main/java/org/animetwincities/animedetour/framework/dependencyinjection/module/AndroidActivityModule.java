package org.animetwincities.animedetour.framework.dependencyinjection.module;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.WindowManager;
import dagger.Module;
import dagger.Provides;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityScope;

/**
 * Android Activity Services.
 *
 * These are activity specific dependency definitions so that a single service
 * can be injected.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@Module
public class AndroidActivityModule
{
    /**
     * Activity context to update services from.
     */
    final private AppCompatActivity activity;

    /**
     * @param activity The Activity that all of the provided resources
     *                 will be based off of.
     */
    public AndroidActivityModule(AppCompatActivity activity)
    {
        this.activity = activity;
    }

    /**
     * Current Activity instance.
     *
     * Avoid depending on this service in favor of more granular services.
     *
     * @return The currently activity instance that this module is bound to.
     */
    @Provides
    @ActivityScope
    public Activity activityContext()
    {
        return this.activity;
    }

    /**
     * Current Activity instance (support level).
     *
     * Avoid depending on this service in favor of more granular services.
     *
     * @return The currently activity instance that this module is bound to.
     */
    @Provides
    @ActivityScope
    public FragmentActivity supportActivityContext()
    {
        return this.activity;
    }

    /**
     * Instantiates a layout XML file into its corresponding View objects.
     *
     * @return LayoutInflater instance that this Window retrieved from its Context.
     */
    @Provides
    @ActivityScope
    public LayoutInflater inflater()
    {
        return this.activity.getLayoutInflater();
    }

    /**
     * Interface for interacting with Fragment objects inside of an Activity.
     *
     * @return The FragmentManager for interacting with fragments associated with this activity.
     */
    @Provides
    @ActivityScope
    public FragmentManager fragmentManager()
    {
        return this.activity.getSupportFragmentManager();
    }

    /**
     * Used to instantiate menu XML files into Menu objects.
     *
     * @return a MenuInflater with the current activity context.
     */
    @Provides
    @ActivityScope
    public MenuInflater menuInflater()
    {
        return this.activity.getMenuInflater();
    }

    /**
     * The interface that apps use to talk to the window manager.
     *
     * @return The window manager for showing custom windows.
     */
    @Provides
    @ActivityScope
    public WindowManager windowManager()
    {
        return this.activity.getWindowManager();
    }

    @Provides
    @ActivityScope
    public ActionBar provideSupportActionBar()
    {
        return this.activity.getSupportActionBar();
    }

    @Provides
    @ActivityScope
    public Resources.Theme theme()
    {
        return this.activity.getTheme();
    }
}
