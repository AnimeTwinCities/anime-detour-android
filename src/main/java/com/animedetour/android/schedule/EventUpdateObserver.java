/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2015 Anime Twin Cities, Inc.
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
import rx.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens to data updates of the event list for a single day.
 *
 * When an update to the events list is detected, this will notify the fragment
 * of the new data and handle any errors.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class EventUpdateObserver implements Observer<List<Event>>
{
    final private Monolog logger;

    /** View to display when there are no items in the list */
    final private View emptyView;

    /** The list view we're to put events into. */
    final private ListView panelList;

    final private ItemAdapter<PanelView, Event> itemAdapter;

    /**
     * Scroll position state.
     *
     * This is a temporary storage of the scroll position state for when the
     * scroll position is restored - since the list will likely  not yet have
     * data in it. Once data is updated, this should be the position to scroll
     * to.
     */
    private int scrollPosition = 0;

    /**
     * Whether the scroll position has been restored already.
     *
     * This allows us to check if we've already changed the scroll position
     * after receiving a data update. This way, if multiple data updates come
     * in, we won't move the list multiple times; which could be annoying to the
     * user.
     */
    private boolean restored = false;

    /**
     * The most recent collection of events received.
     *
     * This is needed to be used as a reference when changing filters for what
     * is displayed in the list.
     */
    private List<Event> events = new ArrayList<>();

    /**
     * Currently displayed filter. (default: all)
     */
    private String filterType = EventFilterUpdater.ALL_EVENTS;

    public EventUpdateObserver(
        ListView panelList,
        ItemAdapter<PanelView, Event> listAdapter,
        View emptyView,
        Monolog logger
    ) {
        this.panelList = panelList;
        this.itemAdapter = listAdapter;
        this.emptyView = emptyView;
        this.logger = logger;
    }

    @Override
    public void onCompleted()
    {
    }

    @Override
    public void onError(Throwable e)
    {
        this.logger.error("Error fetching schedule", e);
    }

    @Override
    public void onNext(List<Event> events)
    {
        this.events = events;
        this.displayFiltered(this.filterType);
    }

    /**
     * Change the events displayed by a specified type filter.
     *
     * @param filterType The Event type to only display events of.
     */
    public void displayFiltered(String filterType)
    {
        this.filterType = filterType;
        ArrayList<Event> filtered = new ArrayList<>();

        if (null == filterType || filterType.equals(EventFilterUpdater.ALL_EVENTS)) {
            this.displayEvents(this.events);
            return;
        }

        for (Event event : this.events) {
            String type = event.getEventType();
            if (null ==  type || false == type.equals(filterType)) {
                continue;
            }
            filtered.add(event);
        }

        this.displayEvents(filtered);
    }

    /**
     * Change the items displayed in the event list.
     *
     * If the list is empty, this will toggle an empty view to be displayed.
     *
     * @param events The list of events to display.
     */
    private void displayEvents(List<Event> events)
    {
        this.toggleEmptyView(events.isEmpty());
        this.itemAdapter.setItems(events);

        if (false == events.isEmpty()) {
            this.restoreState();
        }
    }

    /**
     * Show or hide the empty view visibility.
     *
     * @param isEmpty Whether the list is empty and we should show the empty view.
     */
    private void toggleEmptyView(boolean isEmpty)
    {
        if (isEmpty) {
            this.emptyView.setVisibility(View.VISIBLE);
        } else {
            this.emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * Scroll to the previous item position if it has not already been restored.
     */
    private void restoreState()
    {
        if (this.restored) {
            return;
        }

        this.restored = true;
        this.panelList.smoothScrollToPosition(this.scrollPosition);
    }

    /**
     * Get the current position of the event list view.
     *
     * @return The first visible item position in the list.
     */
    final public int getScrollPosition()
    {
        return this.panelList.getFirstVisiblePosition();
    }

    /**
     * Scrolls the list to an item position.
     *
     * This retains the scroll position so that if the list is updated, it will
     * return to that position.
     *
     * @param scrollPosition the position to scroll to.
     */
    public void setScrollPosition(int scrollPosition)
    {
        this.panelList.smoothScrollToPosition(scrollPosition);
        this.scrollPosition = scrollPosition;
    }
}
