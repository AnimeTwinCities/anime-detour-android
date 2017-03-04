package org.animetwincities.animedetour;

import android.os.Bundle;
import inkapplicaitons.android.logger.Logger;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;

import javax.inject.Inject;

public class MainActivity extends BaseActivity
{
    @Inject
    Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.logger.debug("Hello Logging!");
    }

    @Override
    public void injectSelf(ActivityComponent component) {
        component.inject(this);
    }
}
