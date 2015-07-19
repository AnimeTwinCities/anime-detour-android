/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.animedetour.android.analytics.EventFactory;
import com.animedetour.android.schedule.EventActivity;
import com.animedetour.api.sched.model.Event;
import monolog.Monolog;

/**
 * Reacts to events on the home pages "featured" event banners.
 *
 * Handles the click events and image updates for the banner views.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class FeaturedController implements OnClickListener
{
    /** Activity containing the views for starting a new activity. */
    final private Context context;

    /** Logger to log networking issues with image loading. */
    final private Monolog logger;

    /** An event bound to the view and displayed in it. */
    final private Event event;

    /**
     * @param context Activity containing the views for starting a new activity.
     * @param logger Logger to log networking issues with image loading.
     * @param event An event bound to the view and displayed in it.
     */
    public FeaturedController(Context context, Monolog logger, Event event)
    {
        this.context = context;
        this.logger = logger;
        this.event = event;
    }

    @Override
    public void onClick(View view)
    {
        this.logger.trace(EventFactory.suggestedClick(this.event));
        Intent intent = EventActivity.createIntent(this.context, this.event);
        this.context.startActivity(intent);
    }
}
