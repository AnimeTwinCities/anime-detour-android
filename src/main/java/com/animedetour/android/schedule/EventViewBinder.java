/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule;

import android.content.Context;
import android.view.ViewGroup;
import com.animedetour.api.sched.model.Event;
import com.inkapplications.android.widget.recyclerview.ItemBoundClickListener;
import com.inkapplications.android.widget.recyclerview.ItemViewBinder;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Binds Events to the displayed Panel View.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class EventViewBinder implements ItemViewBinder<PanelView, Event>
{
    /** Context to be used when creating new panel views. */
    private Context context;

    /** Listener to invoke when a panel view is clicked. */
    private PanelViewController clickListener;

    /** For creating the label colors on events. */
    private EventPalette eventPalette;

    /**
     * @param context Context to be used when creating new panel views.
     * @param clickListener Listener to invoke when a panel view is clicked.
     */
    @Inject
    public EventViewBinder(
        Context context,
        EventPalette eventPalette,
        PanelViewController clickListener
    ) {
        this.context = context;
        this.eventPalette = eventPalette;
        this.clickListener = clickListener;
    }

    @Override
    public PanelView createView(ViewGroup viewGroup, int i)
    {
        return new PanelView(this.context);
    }

    @Override
    public void bindView(final Event event, final PanelView view)
    {
        view.reset();
        view.bind(event);

        String eventType = event.getEventType();
        view.setLabelColor(this.eventPalette.getColor(eventType));

        view.setOnClickListener(new ItemBoundClickListener<>(event, this.clickListener));
    }
}
