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
import com.inkapplications.groundcontrol.RemovableSyncWorker;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Controls Synchronizing between the local event database with the remote API.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
abstract public class SyncEventsWorker extends RemovableSyncWorker<List<Event>>
{
    /** A local DAO for storing events. */
    final private Dao<Event, String> localAccess;

    /** A remote endpoint for updating the local storage. */
    final private ScheduleEndpoint remoteAccess;

    /** Service to lookup info on whether the local information is stale. */
    final private FetchedEventMetrics fetchedMetrics;

    /**
     * @param localAccess A local DAO for storing events.
     * @param remoteAccess A remote endpoint for updating the local storage.
     * @param fetchedMetrics Service to lookup info on whether the local information is stale.
     */
    public SyncEventsWorker(
        Dao<Event, String> localAccess,
        ScheduleEndpoint remoteAccess,
        FetchedEventMetrics fetchedMetrics
    ) {
        this.localAccess = localAccess;
        this.remoteAccess = remoteAccess;
        this.fetchedMetrics = fetchedMetrics;
    }

    @Override
    public List<Event> lookupRemote() throws SQLException
    {
        Event mostRecent = this.fetchedMetrics.getMostRecentUpdated();

        long since = mostRecent == null ? 0 : mostRecent.getFetched().getMillis() / 1000;
        List<Event> events = this.remoteAccess.getSchedule(since);

        return events;
    }

    @Override
    public void saveLocal(List<Event> events) throws SQLException
    {
        for (Event event : events) {
            this.localAccess.createOrUpdate(event);
        }
    }

    @Override
    public void removeLocal(List<Event> events) throws SQLException
    {
        for (Event event : events) {
            this.localAccess.deleteById(event.getId());
        }
    }

    @Override
    public List<Event> lookupRemovedRemote() throws Exception
    {
        Event mostRecent = this.fetchedMetrics.getMostRecentUpdated();
        long since = mostRecent == null ? 0 : mostRecent.getFetched().getMillis() / 1000;

        return this.remoteAccess.getSchedule(since, "del");
    }

    @Override
    public boolean dataIsStale() throws SQLException
    {
        return true;
    }
}
