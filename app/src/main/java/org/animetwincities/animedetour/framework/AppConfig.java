package org.animetwincities.animedetour.framework;

import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.animetwincities.rxfirebase.FirebaseObservables;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

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
    final static private String TEST_HEADLINE = "test_headline";

    final private FirebaseRemoteConfig remoteConfig;

    @Inject
    public AppConfig(FirebaseRemoteConfig remoteConfig) {
        this.remoteConfig = remoteConfig;
    }

    /** Just a test */
    public String getTestHeadline()
    {
        return this.remoteConfig.getString(TEST_HEADLINE);
    }

    /**
     * Update the config values with remote configuration values.
     *
     * @return an observable that can be subscribed to when the configuration
     *         update is complete
     */
    public Completable update()
    {
        Task<Void> updateTask = remoteConfig.fetch();

        return FirebaseObservables.fromVoidTask(updateTask)
            .subscribeOn(Schedulers.io())
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

        defaults.put(TEST_HEADLINE, "Hello Wald!");

        return defaults;
    }
}
