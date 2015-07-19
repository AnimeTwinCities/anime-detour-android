/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.home;

import android.app.Activity;
import com.animedetour.api.sched.model.Event;
import monolog.Monolog;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Creates new controllers for the featured event banners at runtime when they
 * are loaded into the homepage.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class FeaturedControllerFactory
{
    final private Activity context;
    final private Monolog logger;

    @Inject
    public FeaturedControllerFactory(Activity context, Monolog logger)
    {
        this.context = context;
        this.logger = logger;
    }

    public FeaturedController create(Event event)
    {
        return new FeaturedController(this.context, this.logger, event);
    }
}
