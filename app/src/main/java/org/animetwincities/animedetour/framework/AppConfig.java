package org.animetwincities.animedetour.framework;

import android.content.*;
import android.graphics.*;
import android.support.annotation.*;

import com.google.android.gms.tasks.*;
import com.google.firebase.remoteconfig.*;

import org.animetwincities.animedetour.R;
import org.animetwincities.rxfirebase.*;

import java.util.*;
import java.util.concurrent.*;

import javax.inject.*;

import inkapplicaitons.android.logger.*;
import io.reactivex.*;
import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

/**
 * Application Configuration settings.
 *
 * This provides accessor methods to the Firebase App Config without actually
 * coupling activities to it or exposing it's stateful methods.
 * It also allows us to define defaults in a compile-time type safe way.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@Singleton
public class AppConfig
{
    final static private String YEAR = "year";

    final private Logger logger;
    final private FirebaseRemoteConfig remoteConfig;
    final private SharedPreferences sharedPreferences;
    final private Context context;

    @Inject
    public AppConfig(Logger logger, FirebaseRemoteConfig remoteConfig, SharedPreferences sharedPreferences, Context context) {
        this.logger = logger;
        this.remoteConfig = remoteConfig;
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }

    /**
     * Get a palette color to associate with an event.
     *
     * In the case that no color is configured for the category type, this will
     * return a transparent hex code.
     *
     * @param category The category of an event to get a color for.
     * @return A Hex code string.
     */
    @ColorInt
    public int getEventPalette(String category)
    {
        if (null == category) {
            this.logger.error("Null category name given on palette lookup! Defaulting to transparent");
            return Color.TRANSPARENT;
        }

        String key = "palette_event_" + category.replace(" ", "_").toLowerCase();
        String configColor = this.remoteConfig.getString(key);

        if (null == configColor || configColor.isEmpty()) {
            this.logger.error("Unconfigured category given for palette: %s – defaulting to grey", key);
            return context.getResources().getColor(R.color.md_grey_700);
        }

        try {
            return Color.parseColor(configColor);
        } catch (IllegalArgumentException error) {
            this.logger.error("Invalid color given for palette: %s – got color: %s", key, configColor);

            return context.getResources().getColor(R.color.md_grey_700);
        }
    }

    /**
     * Get the Schedule ID that is to be displayed.
     */
    public String getSchedule()
    {
        return remoteConfig.getString(YEAR);
    }

    /**
     * @return Whether the app has completed loading the current schedule data.
     */
    public boolean hasLoadedSchedule()
    {
        return this.sharedPreferences.getBoolean("loaded_schedule_" + getSchedule(), false);
    }

    /**
     * Mark the current schedule data as loaded to prevent any further preloading.
     */
    public void setScheduleLoaded()
    {
        this.sharedPreferences.edit().putBoolean("loaded_schedule_" + getSchedule(), true).apply();
    }

    /**
     * Update the config values with remote configuration values.
     *
     * @return an observable that can be subscribed to when the configuration
     *         update is complete
     */
    public Completable update()
    {
        Task<Void> updateTask = remoteConfig.fetch(14400L);

        return FirebaseObservables.fromVoidTask(updateTask)
            .subscribeOn(Schedulers.io())
            .timeout(1, TimeUnit.SECONDS)
            .doOnComplete(remoteConfig::activateFetched)
            .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get all configuration defaults.
     *
     * @return A Key=>Value map of application configuration settings to use as
     *         default values.
     */
    static public Map<String, Object> getDefaultConfigMap()
    {
        HashMap<String, Object> defaults = new HashMap<>();

        defaults.put(YEAR, "ad-2019");

        return defaults;
    }

    public boolean receiveEventNotifications() {
        return this.sharedPreferences.getBoolean("event_notifications", true);
    }

    public void setEventNotifications(boolean enabled) {
        this.sharedPreferences.edit().putBoolean("event_notifications", enabled).apply();
    }

    public boolean showPastEvents() {
        return this.sharedPreferences.getBoolean("event_show_past", false);
    }

    public void setShowPastEvents(boolean value) {
        this.sharedPreferences.edit().putBoolean("event_show_past", value).apply();
    }
}
