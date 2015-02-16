/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.settings;

import com.animedetour.android.database.LocalDatabase;
import com.animedetour.android.database.event.EventRepository;
import com.animedetour.android.database.favorite.FavoriteRepository;
import com.animedetour.api.sched.api.model.Event;
import org.apache.commons.logging.Log;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.Random;

/**
 * Developer fake data shims and generators.
 *
 * This is responsible for inserting fake data into the local database for
 * developer testing.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Singleton
public class DeveloperShims
{
    final private EventRepository eventData;
    final private FavoriteRepository favoriteData;
    final private LocalDatabase database;
    final private Log logger;
    final private Random random = new Random();

    @Inject
    public DeveloperShims(
        EventRepository eventData,
        FavoriteRepository favoriteData,
        LocalDatabase database,
        Log logger
    ) {
        this.eventData = eventData;
        this.favoriteData = favoriteData;
        this.database = database;
        this.logger = logger;
    }

    /**
     * Create a new event minutes from now.
     *
     * Start time set to 16 minutes so that we can catch the 15 minute notification.
     */
    public void addFakeUpcomingEvent()
    {
        Event event = new Event();
        event.setId("fake-event-" + this.random.nextInt());
        event.setName("Fake Upcoming Event");
        event.setEventType("Anime Detour Panel");
        event.setDescription("A fake event generated from the developer settings!");
        event.setStartDateTime(new DateTime().plusMinutes(16));
        event.setEndDateTime(new DateTime().plusMinutes(17));
        event.setFetched(new DateTime());

        try {
            this.eventData.persist(event);
            this.favoriteData.create(event);
        } catch (SQLException e) {
            this.logger.error("Error when saving event: " + event, e);
        }
    }

    /**
     * Drop all local databases.
     */
    public void dropData()
    {
        try {
            this.database.truncateAll();
        } catch (SQLException e) {
            this.logger.error("Could not drop database", e);
        }
    }
}
