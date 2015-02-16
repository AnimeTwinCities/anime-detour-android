/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.database;

import com.animedetour.android.schedule.Favorite;
import com.animedetour.api.guest.model.Category;
import com.animedetour.api.guest.model.Guest;
import com.animedetour.api.sched.api.model.Event;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;

/**
 * Service for performing actions across all database tables.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class LocalDatabase
{
    final private ConnectionSource connectionSource;

    @Inject
    public LocalDatabase(ConnectionSource connectionSource)
    {
        this.connectionSource = connectionSource;
    }

    /**
     * Clear all data in all of our local database tables.
     *
     * @throws SQLException If anything goes wrong clearing the tables.
     */
    public void truncateAll() throws SQLException
    {
        TableUtils.clearTable(this.connectionSource, Favorite.class);
        TableUtils.clearTable(this.connectionSource, Event.class);
        TableUtils.clearTable(this.connectionSource, Guest.class);
        TableUtils.clearTable(this.connectionSource, Category.class);
    }
}
