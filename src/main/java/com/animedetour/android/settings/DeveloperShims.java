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
import com.animedetour.api.sched.model.Event;
import monolog.Monolog;
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
    final private PreferenceManager preferences;
    final private Monolog logger;
    final private Random random = new Random();

    @Inject
    public DeveloperShims(
        EventRepository eventData,
        FavoriteRepository favoriteData,
        LocalDatabase database,
        PreferenceManager preferences,
        Monolog logger
    ) {
        this.eventData = eventData;
        this.favoriteData = favoriteData;
        this.database = database;
        this.preferences = preferences;
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
        event.setDescription("Aenean at blandit purus. Pellentesque libero dui, consectetur id justo vitae, interdum sollicitudin augue. Praesent ac porttitor neque. Nullam lectus nunc, aliquet nec ipsum sed, tempor mattis mi. Curabitur facilisis nibh vel nisl hendrerit, nec maximus nunc convallis. Phasellus pretium nec urna non pharetra. Nunc a lorem arcu. Suspendisse convallis nisl sed porttitor ullamcorper. Nam fringilla pretium nisi, id scelerisque lorem posuere et. Nullam dictum condimentum diam, sed varius arcu ornare eget. Pellentesque pulvinar neque lacinia, pellentesque felis tempor, ullamcorper purus. In hac habitasse platea dictumst. Nam sollicitudin eget mauris sit amet efficitur. Suspendisse sed massa vehicula elit maximus suscipit id commodo augue. Proin egestas pulvinar libero. Nullam pharetra dignissim neque quis vehicula. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent ut placerat sem. Nullam at suscipit nunc. Mauris congue pretium lectus, non mattis diam dapibus vitae. Sed malesuada ligula vel blandit finibus. Maecenas vitae venenatis tellus, vel viverra orci. In luctus massa tempus purus tristique commodo. Vivamus eget lorem mauris. Suspendisse potenti. Nulla non ex in diam vulputate vestibulum. Phasellus dapibus risus a ipsum vulputate, ut dictum metus accumsan. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vivamus euismod tristique consectetur. Nunc sem libero, semper sit amet vulputate in, varius sit amet neque. Mauris nec nulla eu orci mattis rhoncus et sed massa. Fusce non vehicula risus. Vivamus euismod tempor maximus. Nunc risus felis, ultricies aliquam tincidunt vitae, ultricies non elit. Suspendisse potenti. Etiam ullamcorper neque vitae auctor mollis. Pellentesque ac tincidunt odio. Aenean eu massa dictum, ullamcorper neque sed, pellentesque mauris. Aliquam tristique lacinia orci, vel dictum urna fringilla sed. Aenean eros ligula, fringilla a scelerisque id, porttitor vel nibh. Morbi consequat diam vel magna lobortis pellentesque. Nullam malesuada finibus est, a tempus ex vulputate nec. Cras in ligula finibus, euismod sapien nec, eleifend turpis. Nam consectetur turpis dolor, et ultricies eros ornare sit amet. Vestibulum interdum vel mauris eu cursus");
        event.setMediaUrl("http://photos.animedetour.com/Anime-Detour-2014/i-hBjX6XW/0/X3/DSC08740-X3.jpg");
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

    /**
     * Reset/clear all shared preferences.
     */
    public void dropPreferences()
    {
        this.preferences.truncate();
    }
}
