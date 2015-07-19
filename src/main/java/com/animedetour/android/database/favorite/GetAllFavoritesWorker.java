/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.database.favorite;

import com.animedetour.android.schedule.favorite.Favorite;
import com.animedetour.api.sched.model.Event;
import com.inkapplications.groundcontrol.SingleYieldWorker;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Looks up all events that the user has favorited locally.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class GetAllFavoritesWorker extends SingleYieldWorker<List<Favorite>>
{
    /** Local favorite storage. */
    final private Dao<Favorite, Integer> localAccess;

    /** Local event storage used for sorting. */
    final private Dao<Event, Integer> localEventAccess;

    /**
     * @param localAccess Local favorite storage.
     * @param localEventAccess Local event storage used for sorting.
     */
    public GetAllFavoritesWorker(
        Dao<Favorite, Integer> localAccess,
        Dao<Event, Integer> localEventAccess
    ) {
        this.localAccess = localAccess;
        this.localEventAccess = localEventAccess;
    }

    /**
     * @return All of the events that the user has marked as a favorite.
     */
    public List<Favorite> lookupLocal() throws SQLException
    {
        QueryBuilder<Favorite, Integer> builder = this.localAccess.queryBuilder();
        builder.join(this.localEventAccess.queryBuilder());
        builder.orderByRaw("event.start ASC, event.name ASC");

        PreparedQuery<Favorite> query = builder.prepare();
        List<Favorite> result = this.localAccess.query(query);

        return result;
    }
}
