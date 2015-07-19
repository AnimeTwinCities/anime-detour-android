/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.database.event;

import com.animedetour.api.sched.model.Event;
import com.inkapplications.groundcontrol.CriteriaWorkerFactory;
import com.inkapplications.groundcontrol.SubscriptionFactory;
import com.inkapplications.groundcontrol.Worker;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import org.joda.time.DateTime;
import rx.Observer;
import rx.Subscription;

import java.sql.SQLException;
import java.util.List;

/**
 * Provides an API for querying Event entities.
 *
 * This repository creates new requests asynchronously where needed by
 * delegating to several different worker services.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class EventRepository
{
    /** Manage in-flight requests to async repos. */
    final private SubscriptionFactory<Event> subscriptionFactory;

    /** A local DAO for storing events. */
    final private Dao<Event, String> localAccess;

    /** Worker for looking up a list of all events. */
    final private Worker<List<Event>> allEventsWorker;

    /** Worker for looking up a list of events by their start time. */
    final private CriteriaWorkerFactory<List<Event>, DateTime> allByDayFactory;

    /** Worker for looking up a single event with a tag. */
    final private CriteriaWorkerFactory<Event, TagCriteria> upcomingByTagFactory;

    /** Worker for looking up a single event of a type. */
    final private CriteriaWorkerFactory<Event, TypeCriteria> upcomingByTypeFactory;

    final private CriteriaWorkerFactory<List<Event>, String> allMatchingFactory;

    /**
     * @param subscriptionFactory Manage in-flight requests to async repos.
     * @param localAccess A local DAO for storing events.
     * @param allEventsWorker Worker for looking up a list of all events.
     * @param allByDayFactory Worker for looking up a list of events by their start time.
     * @param upcomingByTagFactory Worker for looking up a single event with a tag.
     * @param upcomingByTypeFactory Worker for looking up a single event of a type.
     */
    public EventRepository(
        SubscriptionFactory<Event> subscriptionFactory,
        Dao<Event, String> localAccess,
        AllEventsWorker allEventsWorker,
        CriteriaWorkerFactory<List<Event>, DateTime> allByDayFactory,
        CriteriaWorkerFactory<Event, TagCriteria> upcomingByTagFactory,
        CriteriaWorkerFactory<Event, TypeCriteria> upcomingByTypeFactory,
        CriteriaWorkerFactory<List<Event>, String> allMatchingFactory
    ) {
        this.localAccess = localAccess;
        this.allEventsWorker = allEventsWorker;
        this.subscriptionFactory = subscriptionFactory;
        this.allByDayFactory = allByDayFactory;
        this.upcomingByTagFactory = upcomingByTagFactory;
        this.upcomingByTypeFactory = upcomingByTypeFactory;
        this.allMatchingFactory = allMatchingFactory;
    }

    /**
     * Find All Events.
     *
     * @return an observable that will update with events data
     */
    public Subscription findAll(Observer<List<Event>> observer)
    {
        return this.subscriptionFactory.createCollectionSubscription(
            this.allEventsWorker,
            observer,
            "findAll"
        );
    }

    /**
     * Find All events for a specified day.
     *
     * This is run by the START time of the event
     *
     * @param day The day to lookup events for
     * @return an observable that will update with events data
     */
    public Subscription findAllOnDay(DateTime day, Observer<List<Event>> observer)
    {
        String key = "findAllOnDay:" + day.getDayOfYear();
        return this.subscriptionFactory.createCollectionSubscription(
            this.allByDayFactory.createWorker(day),
            observer,
            key
        );
    }

    /**
     * Find all of the "featured" events to suggest to the user.
     */
    public Subscription findFeatured(Observer<Event> observer, long ordinal)
    {
        return this.findUpcomingByType(
            "Anime Detour Panel",
            ordinal,
            observer
        );
    }

    /**
     * Finds a single event of a given type.
     */
    public Subscription findUpcomingByType(String type, long ordinal, Observer<Event> observer)
    {
        String key = "findUpcomingByType:" + type + ":" + ordinal;
        return this.subscriptionFactory.createSubscription(
            this.upcomingByTypeFactory.createWorker(new TypeCriteria(type, ordinal)),
            observer,
            key
        );
    }

    /**
     * Finds a single event containing a specific tag.
     *
     * @param tag The tag to search for events containing.
     */
    public Subscription findUpcomingByTag(String tag, long ordinal, Observer<Event> observer)
    {
        String key = "findUpcomingByTag:" + tag + ":" + ordinal;
        return this.subscriptionFactory.createSubscription(
            this.upcomingByTagFactory.createWorker(new TagCriteria(tag, ordinal)),
            observer,
            key
        );
    }

    /**
     * Finds events where the title matches a specified string.
     *
     * @param search The string to search in the title of events for.
     */
    public Subscription findMatching(String search, Observer<List<Event>> observer)
    {
        String key = "findMatching:" + search;
        return this.subscriptionFactory.createCollectionSubscription(
            this.allMatchingFactory.createWorker(search),
            observer,
            key
        );
    }

    /**
     * Get All local events.
     *
     * @return A list of every event in the local data store
     */
    public List<Event> getAll() throws SQLException
    {
        return this.allEventsWorker.lookupLocal();
    }

    /**
     * Get All Events on a specific day.
     *
     * @return Events that occur on that day (by start time)
     */
    public List<Event> getAllOnDay(DateTime day) throws SQLException
    {
        return this.allByDayFactory.createWorker(day).lookupLocal();
    }

    /**
     * Get a specific event by its ID.
     */
    public Event get(String id) throws SQLException
    {
        return this.localAccess.queryForId(id);
    }

    /**
     * Fetches the most recently updated event according to fetch time.
     */
    public Event getMostRecentUpdated() throws SQLException
    {
        QueryBuilder<Event, String> builder = this.localAccess.queryBuilder();
        builder.orderBy("fetched", false);


        PreparedQuery<Event> query = builder.prepare();
        Event result = this.localAccess.queryForFirst(query);

        return result;
    }

    public void persist(Event event) throws SQLException
    {
        this.localAccess.createOrUpdate(event);
    }
}
