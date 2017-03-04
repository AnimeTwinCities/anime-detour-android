package org.animetwincities.animedetour.framework;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

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
