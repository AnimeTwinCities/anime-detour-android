package org.animetwincities.animedetour.framework.dependencyinjection.module;

import android.app.AlarmManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.inputmethod.InputMethodManager;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AndroidApplicationModule {
    final private Application application;

    /**
     * @param application The active Android application.
     */
    public AndroidApplicationModule(Application application) {
        this.application = application;
    }

    /**
     * Android Application instance.
     *
     * Avoid depending on this service in favor of more granular services.
     *
     * @return The root Android application instance that is bound to the module.
     */
    @Provides
    @Singleton
    public Application application()
    {
        return this.application;
    }

    /**
     * Generic system context.
     *
     * Avoid depending on this service in favor of more granular services.
     *
     * @return The context of the single, global Application object of the current process.
     */
    @Provides
    @Singleton
    public Context applicationContext()
    {
        return this.application.getApplicationContext();
    }

    /**
     * Information you can retrieve about a particular application.
     *
     * @return The full application info for this context's package.
     */
    @Provides
    @Singleton
    public ApplicationInfo applicationInfo()
    {
        return this.application.getApplicationInfo();
    }

    /**
     * Provides access to an application's raw asset files.
     *
     * @return An AssetManager instance for your application's package.
     */
    @Provides
    @Singleton
    public AssetManager assets()
    {
        return this.application.getAssets();
    }

    /**
     * This class provides applications access to the content model.
     *
     * @return A ContentResolver instance for your application's package.
     */
    @Provides
    @Singleton
    public ContentResolver contentResolver()
    {
        return this.application.getContentResolver();
    }

    /**
     * Loads classes and resources from a repository.
     *
     * @return A class loader you can use to retrieve classes in this package.
     */
    @Provides
    @Singleton
    public ClassLoader classLoader()
    {
        return this.application.getClassLoader();
    }

    /**
     * Retrieves various kinds of information related to the application
     * packages that are currently installed on the device.
     *
     * @return A PackageManager instance to find global package information.
     */
    @Provides
    @Singleton
    public PackageManager packageManager()
    {
        return this.application.getPackageManager();
    }

    @Provides
    @Singleton
    public InputMethodManager inputMethodManager(Application context)
    {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * Keeps track of all non-code assets associated with an application.
     *
     * @return A Resources instance for your application's package.
     */
    @Provides
    @Singleton
    public Resources resources()
    {
        return this.application.getResources();
    }

    /**
     * Application Shared Preferences.
     *
     * This provides a private-mode level shared preferences instance, since
     * all other modes are deprecated.
     * The provided instance does not allow concurrent writing.
     *
     * @return A SharedPreferences through which you can retrieve and modify its values.
     */
    @Provides
    @Singleton
    public SharedPreferences sharedPreferences()
    {
        return this.application.getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public AlarmManager alarmManager()
    {
        return (AlarmManager) this.application.getSystemService(Context.ALARM_SERVICE);
    }
}
