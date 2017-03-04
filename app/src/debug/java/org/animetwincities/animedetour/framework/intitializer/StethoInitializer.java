package org.animetwincities.animedetour.framework.intitializer;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.inkapplications.android.applicationlifecycle.Initializer;

/**
 * Initializes the Stetho Utility
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class StethoInitializer extends Initializer
{
    @Override
    public void onCreate(Application application)
    {
        Stetho.initializeWithDefaults(application);
    }
}
