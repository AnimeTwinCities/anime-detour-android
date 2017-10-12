/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule.serach;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.animedetour.android.R;
import com.animedetour.android.database.event.type.EventTypeRepository;
import com.animedetour.android.framework.BaseActivity;
import com.animedetour.android.model.Event;
import com.animedetour.android.schedule.EventPalette;
import com.animedetour.android.schedule.EventViewBinder;
import com.animedetour.android.schedule.PanelView;
import com.inkapplications.android.widget.listview.ItemAdapter;

import butterknife.BindView;
import monolog.LogName;
import monolog.Monolog;
import prism.framework.Layout;

import javax.inject.Inject;

/**
 * Search page for quickly finding events.
 *
 * This has a search bar for the user to type in and filters down the events
 * by their search criteria.
 * If the user has not typed anything in the search bar, it will display the
 * user filters that they can search by.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@LogName("Event Search")
@Layout(R.layout.event_search)
final public class EventSearchActivity extends BaseActivity
{
    @BindView(R.id.event_search_action_bar)
    Toolbar actionBar;

    @BindView(R.id.event_search_bar)
    SearchView searchBar;

    @BindView(R.id.event_search_results)
    ListView results;

    @BindView(R.id.event_search_filters)
    ListView filters;

    @BindView(R.id.search_empty_view)
    View emptyView;

    @Inject
    EventViewBinder viewBinder;

    @Inject
    QueryListenerFactory queryListenerFactory;

    @Inject
    EventTypeRepository filterData;

    @Inject
    EventPalette palette;

    @Inject
    Monolog logger;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setSupportActionBar(this.actionBar);
        this.actionBar.setTitle("");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.searchBar.setIconifiedByDefault(false);
        this.searchBar.requestFocusFromTouch();
        ItemAdapter<PanelView, Event> adapter = new ItemAdapter<>(this.viewBinder);
        this.results.setAdapter(adapter);
        FilterViewBinder filterBinder = new FilterViewBinder(this, this.searchBar, this.palette);
        ItemAdapter<FilterItemView, String> filterAdapter = new ItemAdapter<>(filterBinder);
        this.filters.setAdapter(filterAdapter);
        this.filterData.findAllCategories(
            new EventTypeObserver(this.logger, this.emptyView, filterAdapter)
        );

        OnQueryTextListener queryListener = this.queryListenerFactory.create(
            adapter,
            this.searchBar,
            this.emptyView,
            this.results,
            this.filters
        );
        this.searchBar.setOnQueryTextListener(queryListener);
        queryListener.onQueryTextChange(this.searchBar.getQuery().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
