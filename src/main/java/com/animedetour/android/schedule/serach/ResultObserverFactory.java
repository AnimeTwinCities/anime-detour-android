/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule.serach;

import android.view.View;
import com.animedetour.api.sched.model.Event;
import com.inkapplications.android.widget.listview.ItemAdapter;
import monolog.Monolog;
import rx.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Creates new instances of the observer to be used for search results that
 * has been injected at runtime.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class ResultObserverFactory
{
    final private Monolog logger;

    @Inject
    public ResultObserverFactory(Monolog log)
    {
        logger = log;
    }

    public Observer<List<Event>> create(ItemAdapter<?, Event> adapter, View emptyView)
    {
        return new EventSearchResultObserver(this.logger, adapter, emptyView);
    }
}
