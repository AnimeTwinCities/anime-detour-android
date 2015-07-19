/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.database.event;

import com.animedetour.api.sched.ScheduleEndpoint;
import com.animedetour.api.sched.model.Event;
import com.inkapplications.groundcontrol.Worker;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import org.joda.time.DateTime;
import rx.Subscriber;

import java.sql.SQLException;
import java.util.List;

/**
 * Looks up a single event of a specified type after synchronizing with
 * the remote API.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class UpcomingEventByTypeWorker implements Worker<Event>
{
    final private Dao<Event, String> localAccess;
    final private ScheduleEndpoint remoteAccess;
    final private FetchedEventMetrics fetchedEventMetrics;
    final private TypeCriteria criteria;

    public UpcomingEventByTypeWorker(
        Dao<Event, String> localAccess,
        ScheduleEndpoint remoteAccess,
        FetchedEventMetrics fetchedEventMetrics,
        TypeCriteria type
    ) {
        this.localAccess = localAccess;
        this.remoteAccess = remoteAccess;
        this.fetchedEventMetrics = fetchedEventMetrics;
        this.criteria = type;
    }

    @Override
    public void call(Subscriber<? super Event> subscriber)
    {
        try {
            this.lookup(subscriber);
        } catch (Exception e) {
            subscriber.onError(e);
        }

        subscriber.onCompleted();
    }

    /**
     * Looks up local data, syncs the repository, then does a new lookup.
     */
    private void lookup(Subscriber<? super Event> subscriber) throws SQLException
    {
        Event currentEvents = this.lookupLocal();
        subscriber.onNext(currentEvents);

        if (false == this.fetchedEventMetrics.dataIsStale()) {
            return;
        }

        Event mostRecent = this.fetchedEventMetrics.getMostRecentUpdated();
        long since = mostRecent == null ? 0 : mostRecent.getFetched().getMillis();

        List<Event> events = this.remoteAccess.getSchedule(since);
        this.saveLocal(events);

        Event newEvent = this.lookupLocal();
        subscriber.onNext(newEvent);
    }

    /**
     * Get an upcoming event by type and position.
     *
     * Searches for events of a specified type, orders them by their
     * start time excluding events that have already started, and returns a
     * single event of the specified position.
     *
     * @return The upcoming event
     */
    @Override
    public Event lookupLocal() throws SQLException
    {
        QueryBuilder<Event, String> builder = this.localAccess.queryBuilder();
        Where<Event, String> where = builder.where();
        where.eq("eventType", this.criteria.getType());
        where.and();
        where.gt("start", new DateTime());
        builder.orderBy("start", true);
        builder.offset(this.criteria.getOrdinal() - 1);
        builder.limit(1L);
        PreparedQuery<Event> prepared = builder.prepare();

        Event result = this.localAccess.queryForFirst(prepared);

        return result;
    }

    public void saveLocal(List<Event> events) throws SQLException
    {
        for (Event event : events) {
            this.localAccess.createOrUpdate(event);
        }
    }
}
