/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc. All rights Reserved.
 */
package com.animedetour.android.schedule;

import com.inkapplications.prism.widget.recyclerview.ItemViewBinder;

/**
 * Binds the events tied to a list of favorites into views.
 *
 * This extracts the event from a Favorite and delegates the view logic to the
 * Event's ViewBinder.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
class FavoriteViewBinder implements ItemViewBinder<PanelView, Favorite>
{
    final private EventViewBinder eventViewBinder;

    public FavoriteViewBinder(EventViewBinder eventViewBinder)
    {
        this.eventViewBinder = eventViewBinder;
    }

    @Override
    public PanelView createView()
    {
        return this.eventViewBinder.createView();
    }

    @Override
    public void bindView(Favorite favorite, PanelView view)
    {
        this.eventViewBinder.bindView(favorite.getEvent(), view);
    }
}