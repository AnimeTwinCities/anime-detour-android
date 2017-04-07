/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.animetwincities.animedetour.schedule;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import butterknife.BindView;
import com.inkapplications.android.widget.recyclerview.ItemBoundClickListener;
import com.inkapplications.android.widget.recyclerview.ItemViewBinder;
import inkapplicaitons.android.logger.Logger;
import inkapplications.android.layoutinjector.Layout;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.AppConfig;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;

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
@Layout(R.layout.event_search)
public class EventSearchActivity extends BaseActivity
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
    ScheduleRepository scheduleRepository;

    @Inject
    Logger logger;

    @Inject
    EventListAdapter adapter;

    @Inject
    AppConfig config;

    @Inject
    InputMethodManager inputManager;

    private CompositeDisposable disposables;

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
        disposables = new CompositeDisposable();
        this.searchBar.setIconifiedByDefault(false);
        this.searchBar.requestFocusFromTouch();
        this.results.setAdapter(adapter);
        ItemAdapter<FilterItemView, String> filterAdapter = new ItemAdapter<>(new ItemViewBinder<FilterItemView, String>() {
            @Override
            public FilterItemView createView(ViewGroup viewGroup, int i)
            {
                return new FilterItemView(EventSearchActivity.this);
            }

            @Override
            public void bindView(String type, FilterItemView view)
            {
                view.setTitle(type);
                view.setColor(config.getEventPalette(type));
                view.setOnClickListener(new ItemBoundClickListener<>(type, (s, view1) -> searchBar.setQuery(s, true)));
            }
        });
        this.results.setOnItemClickListener((adapterView, view, i, l) -> {
            String id = adapter.getItem(i).getId();
            startActivity(EventDetailActivity.createIntent(EventSearchActivity.this, id));
        });
        this.filters.setAdapter(filterAdapter);
        disposables.add(scheduleRepository.observeEvents()
            .flatMap(events -> Observable.fromIterable(events)
                .map(Event::getCategory)
                .distinct()
                .toList()
                .toObservable()
            )
            .subscribe(filterAdapter::setItems, error -> logger.error(error, "Problem loading events for categories"))
        );

        OnQueryTextListener queryListener = new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                inputManager.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                toggleVisibility(newText);
                disposables.add(
                    scheduleRepository.observeEvents()
                        .flatMap(eventList -> Observable.fromIterable(eventList)
                            .filter(event -> {
                                String query = newText.toLowerCase();
                                if (event.getName().toLowerCase().contains(query)) {
                                    return true;
                                }
                                if (event.getDescription().toLowerCase().contains(query)) {
                                    return true;
                                }
                                if (event.getCategory().toLowerCase().contains(query)) {
                                    return true;
                                }
                                if (event.getHosts() != null && TextUtils.join(",", event.getHosts()).toLowerCase().contains(query)) {
                                    return true;
                                }
                                if (event.getTags() != null && TextUtils.join(",", event.getTags()).toLowerCase().contains(query)) {
                                    return true;
                                }

                                return false;
                            })
                            .toList()
                            .toObservable())
                        .subscribe(events -> {
                            adapter.setEvents(events);

                            if (events.isEmpty()) {
                                emptyView.setVisibility(View.VISIBLE);
                                filters.setVisibility(View.GONE);
                                results.setVisibility(View.GONE);
                            } else if (newText.isEmpty()) {
                                emptyView.setVisibility(View.GONE);
                                filters.setVisibility(View.VISIBLE);
                                results.setVisibility(View.GONE);
                            } else {
                                emptyView.setVisibility(View.GONE);
                                filters.setVisibility(View.GONE);
                                results.setVisibility(View.VISIBLE);
                            }

                        }, error -> logger.error(error, "Problem observing events for search"))
                );

                return true;
            }

            /**
             * Displays the search results or the empty search view depending on whether
             * the user query is empty.
             *
             * @param userQuery The text the user has entered in the search bar.
             */
            private void toggleVisibility(String userQuery)
            {
                if (userQuery.trim().equals("")) {
                    filters.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    results.setVisibility(View.GONE);
                    return;
                }

                emptyView.setVisibility(View.VISIBLE);
                filters.setVisibility(View.GONE);
                results.setVisibility(View.GONE);
            }
        };
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

    @Override
    protected void onStop() {
        super.onStop();
        disposables.dispose();
    }

    @Override
    public void injectSelf(ActivityComponent component) {
        component.inject(this);
    }
}
