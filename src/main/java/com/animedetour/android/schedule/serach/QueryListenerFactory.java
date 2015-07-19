/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule.serach;

import android.support.v7.widget.SearchView;
import android.view.View;
import com.animedetour.android.database.event.EventRepository;
import com.animedetour.api.sched.model.Event;
import com.inkapplications.android.widget.listview.ItemAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Creates instances of the query listener to be used for watching search
 * queries and looking up events based on the text entered that is injected
 * at runtime.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class QueryListenerFactory
{
    final private ResultObserverFactory resultObserverFactory;
    final private EventRepository eventData;

    @Inject
    public QueryListenerFactory(
        ResultObserverFactory resultObserverFactory,
        EventRepository eventData
    ) {
        this.resultObserverFactory = resultObserverFactory;
        this.eventData = eventData;
    }

    public SearchView.OnQueryTextListener create(ItemAdapter<?, Event> adapter, View emptyView)
    {
        return new EventQueryListener(this.eventData, this.resultObserverFactory.create(adapter, emptyView));
    }
}
