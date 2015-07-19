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
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.List;

/**
 * Looks up a list of all locally stored events by a specified date after
 * syncing with the API.
 *
 * Events will be  any that *start* on the specified date and ordered by start
 * time.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class AllEventsByDayWorker extends SyncEventsWorker
{
    final private Dao<Event, String> localAccess;
    final private DateTime criteria;

    public AllEventsByDayWorker(
        Dao<Event, String> localAccess,
        ScheduleEndpoint remoteAccess,
        FetchedEventMetrics fetchedMetrics,
        DateTime criteria
    ) {
        super(localAccess, remoteAccess, fetchedMetrics);

        this.localAccess = localAccess;
        this.criteria = criteria;
    }

    @Override
    public List<Event> lookupLocal() throws SQLException
    {
        DateTime start = this.criteria.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        DateTime end = this.criteria.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
        QueryBuilder<Event, String> builder = this.localAccess.queryBuilder();
        builder.orderBy("start", true);
        builder.orderBy("name", true);
        builder.where().between("start", start, end);

        PreparedQuery<Event> query = builder.prepare();
        List<Event> result = this.localAccess.query(query);

        return result;
    }
}
