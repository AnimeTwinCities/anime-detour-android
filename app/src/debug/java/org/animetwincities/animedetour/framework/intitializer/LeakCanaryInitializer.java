package org.animetwincities.animedetour.framework.intitializer;

import android.app.Application;
import com.inkapplications.android.applicationlifecycle.Initializer;
import com.squareup.leakcanary.LeakCanary;

/**
 * Initialize the Leak Canary Library.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class LeakCanaryInitializer extends Initializer {
    @Override
    public void onCreate(Application application) {

        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            // This won't prevent other initialization code from running, but
            // Will at least stop analyzing LeakCanary
            return;
        }
        LeakCanary.install(application);
    }
}
