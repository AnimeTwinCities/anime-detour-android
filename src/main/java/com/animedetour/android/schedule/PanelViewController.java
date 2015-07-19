/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule;

import android.app.Activity;
import android.content.Intent;
import com.animedetour.android.analytics.EventFactory;
import com.animedetour.api.sched.model.Event;
import com.inkapplications.android.widget.recyclerview.ViewClickListener;
import monolog.Monolog;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Handles events on the panel view items in the list of events.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class PanelViewController implements ViewClickListener<PanelView, Event>
{
    final private Activity context;
    final private Monolog logger;

    @Inject
    public PanelViewController(Activity context, Monolog logger)
    {
        this.context = context;
        this.logger = logger;
    }

    /**
     * Opens an event activity when one of the panel items is clicked.
     *
     * @param selected The event data item that was clicked by the user.
     * @param view The view item that was clicked by the user.
     */
    @Override
    public void onViewClicked(Event selected, PanelView view)
    {
        this.logger.trace(EventFactory.eventDetails(selected));

        Intent intent = EventActivity.createIntent(this.context, selected);
        this.context.startActivity(intent);
    }
}
