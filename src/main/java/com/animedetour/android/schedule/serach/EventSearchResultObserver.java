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

import java.util.List;

/**
 * Listens for updates to search result contents and updates an item adapter
 * in order to display the result set to the user.
 *
 * If there are no results, this triggers the empty view to display.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class EventSearchResultObserver implements Observer<List<Event>>
{
    final private Monolog logger;
    final private ItemAdapter<?, Event> adapter;
    final private View emptyView;

    public EventSearchResultObserver(
        Monolog logger,
        ItemAdapter<?, Event> adapter,
        View emptyView
    ) {
        this.logger = logger;
        this.adapter = adapter;
        this.emptyView = emptyView;
    }

    @Override public void onCompleted() {}

    @Override
    public void onError(Throwable e)
    {
        this.logger.error("Problem searching for event", e);
    }

    @Override
    public void onNext(List<Event> events)
    {
        this.adapter.setItems(events);

        if (events.isEmpty()) {
            this.emptyView.setVisibility(View.VISIBLE);
        } else {
            this.emptyView.setVisibility(View.GONE);
        }
    }
}
