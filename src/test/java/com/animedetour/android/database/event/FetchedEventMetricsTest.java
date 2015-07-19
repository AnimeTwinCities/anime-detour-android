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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
@SuppressWarnings("unchecked")
public class FetchedEventMetricsTest
{
    @Test
    public void staleForEmptyEventList() throws Exception
    {
        Event oldEvent = new Event();
        oldEvent.setFetched(new DateTime(0));

        Event newEvent = new Event();
        newEvent.setFetched(new DateTime());


        boolean emptyResult = this.resultFor(null);
        boolean oldResult = this.resultFor(oldEvent);
        boolean newResult = this.resultFor(newEvent);

        assertTrue("Data should always be stale when there are no events", emptyResult);
        assertTrue("Data should be stale for old events", oldResult);
        assertFalse("Data should not be stale for recent events", newResult);
    }

    private boolean resultFor(Event event) throws Exception
    {
        Dao<Event, String> dao = mock(Dao.class);
        when(dao.queryBuilder()).thenReturn(mock(QueryBuilder.class));
        when(dao.queryForFirst(any(PreparedQuery.class))).thenReturn(event);

        FetchedEventMetrics metrics = new FetchedEventMetrics(dao);

        return metrics.dataIsStale();
    }
}
