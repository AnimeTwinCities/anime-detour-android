package org.animetwincities.animedetour.framework.threeten;

import android.app.Application;
import com.inkapplications.android.applicationlifecycle.Initializer;
import com.jakewharton.threetenabp.AndroidThreeTen;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Initialize ThreeTen with the application so it can get the timezone
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@Singleton
public class ThreeTenInitializer extends Initializer
{
    @Inject
    public ThreeTenInitializer() {}

    @Override
    public void onCreate(Application application)
    {
        AndroidThreeTen.init(application);
    }
}
