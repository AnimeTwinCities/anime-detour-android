/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule;

import android.view.View;
import android.widget.ListView;
import com.animedetour.api.sched.model.Event;
import com.inkapplications.android.widget.listview.ItemAdapter;
import monolog.Monolog;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Creates new Event Update observer factories at runtime.
 *
 * Since the subscribers need views that are not accessible until runtime, this
 * class will create and inject them for us.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class EventObserverFactory
{
    final private Monolog logger;

    @Inject
    public EventObserverFactory(Monolog logger)
    {
        this.logger = logger;
    }

    public EventUpdateObserver create(
        ListView panelList,
        ItemAdapter<PanelView, Event> listAdapter,
        View emptyView
    ) {
        return new EventUpdateObserver(panelList, listAdapter, emptyView, this.logger);
    }
}
