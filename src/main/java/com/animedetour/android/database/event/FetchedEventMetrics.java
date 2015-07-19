/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.database.event;

import com.animedetour.api.sched.model.Event;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import org.joda.time.DateTime;

import java.sql.SQLException;

/**
 * Looks up information on when the locally stored events were fetched from the API.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class FetchedEventMetrics
{
    final private Dao<Event, String> localAccess;

    public FetchedEventMetrics(Dao<Event, String> localAccess)
    {
        this.localAccess = localAccess;
    }

    /**
     * Checks if the most recent record is before a threshold.
     *
     * Current threshold is set to an hour.
     *
     * @return true the data is considered too old.
     */
    final public boolean dataIsStale() throws SQLException
    {
        Event mostRecent = this.getMostRecentUpdated();

        if (null == mostRecent) {
            return true;
        }

        DateTime earliest = new DateTime().minusHours(1);
        if (mostRecent.getFetched().isBefore(earliest)) {
            return true;
        }

        return false;
    }

    /**
     * Fetches the most recently updated event according to fetch time.
     */
    final public Event getMostRecentUpdated() throws SQLException
    {
        QueryBuilder<Event, String> builder = this.localAccess.queryBuilder();
        builder.orderBy("fetched", false);


        PreparedQuery<Event> query = builder.prepare();
        Event result = this.localAccess.queryForFirst(query);

        return result;
    }
}
