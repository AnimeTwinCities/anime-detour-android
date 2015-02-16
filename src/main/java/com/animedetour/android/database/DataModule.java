/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.database;

import android.app.Application;
import com.animedetour.android.database.event.EventRepository;
import com.animedetour.android.database.event.AllEventsByDayFactory;
import com.animedetour.android.database.event.AllEventsWorker;
import com.animedetour.android.database.event.FetchedEventMetrics;
import com.animedetour.android.database.event.UpcomingEventByTypeFactory;
import com.animedetour.android.database.event.UpcomingEventsByTagFactory;
import com.animedetour.android.database.favorite.FavoriteRepository;
import com.animedetour.android.database.favorite.GetAllFavoritesWorker;
import com.animedetour.android.database.guest.GuestRepository;
import com.animedetour.android.database.guest.AllCategoriesWorker;
import com.animedetour.android.schedule.Favorite;
import com.animedetour.api.guest.GuestEndpoint;
import com.animedetour.api.guest.model.Category;
import com.animedetour.api.guest.model.Guest;
import com.animedetour.api.sched.api.ScheduleEndpoint;
import com.animedetour.api.sched.api.model.Event;
import com.inkapplications.groundcontrol.SubscriptionFactory;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import dagger.Module;
import dagger.Provides;
import org.apache.commons.logging.Log;

import javax.inject.Singleton;
import java.sql.SQLException;

@Module(library = true, complete = false)
@SuppressWarnings("UnusedDeclaration")
final public class DataModule
{
    @Provides @Singleton EventRepository provideEventRepository(
        ConnectionSource connectionSource,
        ScheduleEndpoint remote,
        Log logger
    ) {
        SubscriptionFactory<Event> subscriptionFactory = new SubscriptionFactory<>(logger);

        try {
            Dao<Event, String> local = DaoManager.createDao(connectionSource, Event.class);
            FetchedEventMetrics metrics = new FetchedEventMetrics(local);

            return new EventRepository(
                subscriptionFactory,
                local,
                new AllEventsWorker(local, remote, metrics),
                new AllEventsByDayFactory(local, remote, metrics),
                new UpcomingEventsByTagFactory(local, remote, metrics),
                new UpcomingEventByTypeFactory(local, remote, metrics)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides @Singleton GuestRepository provideGuestRepository(
        ConnectionSource connectionSource,
        GuestEndpoint remote,
        Log logger
    ) {
        SubscriptionFactory<Category> subscriptionFactory = new SubscriptionFactory<>(logger);

        try {
            Dao<Category, Integer> localCategory = DaoManager.createDao(connectionSource, Category.class);
            Dao<Guest, String> localGuest = DaoManager.createDao(connectionSource, Guest.class);
            return new GuestRepository(
                subscriptionFactory,
                new AllCategoriesWorker(connectionSource, localCategory, localGuest, remote)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides @Singleton FavoriteRepository provideGuestRepository(
        ConnectionSource connectionSource
    ) {
        try {
            Dao<Favorite, Integer> local = DaoManager.createDao(connectionSource, Favorite.class);
            GetAllFavoritesWorker collectionWorker = new GetAllFavoritesWorker(local);

            return new FavoriteRepository(local, collectionWorker);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides @Singleton DetourDatabaseHelper provideHelper(
        Application context
    ) {
        return new DetourDatabaseHelper(context);
    }

    @Provides @Singleton ConnectionSource provideConnectionSource(
        DetourDatabaseHelper helper
    ) {
        return new AndroidConnectionSource(helper);
    }
}
