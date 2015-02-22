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
import com.animedetour.api.sched.api.model.Event;
import com.inkapplications.android.widget.listview.ItemAdapter;
import org.apache.commons.logging.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Creates new Event Update subscriber factories at runtime.
 *
 * Since the subscribers need views that are not accessible until runtime, this
 * class will create and inject them for us.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class EventSubscriberFactory
{
    final private Log logger;

    @Inject
    public EventSubscriberFactory(Log logger)
    {
        this.logger = logger;
    }

    public EventUpdateSubscriber create(
        ListView panelList,
        ItemAdapter<PanelView, Event> listAdapter,
        View emptyView
    ) {
        return new EventUpdateSubscriber(panelList, listAdapter, emptyView, this.logger);
    }
}
