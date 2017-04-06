/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.animetwincities.animedetour.schedule.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import inkapplicaitons.android.logger.Logger;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.AnimeDetourApplication;
import org.animetwincities.animedetour.schedule.Event;
import org.animetwincities.animedetour.schedule.EventDetailActivity;
import org.animetwincities.animedetour.schedule.ScheduleRepository;

import javax.inject.Inject;

/**
 * Handles incoming intents for scheduled event notifications.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class UpcomingEventReciever extends BroadcastReceiver
{
    final public static String EXTRA_EVENT = "Bundle_Event";
    final public static String EXTRA_BUNDLE = "notification_bundle";

    @Inject
    ScheduleRepository scheduleRepository;

    @Inject
    Logger logger;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        AnimeDetourApplication application = (AnimeDetourApplication) context.getApplicationContext();
        application.getApplicationComponent().inject(this);
        logger.debug("Received Notification.");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
        String eventId = (String) bundle.getSerializable(EXTRA_EVENT);

        scheduleRepository.observeEvent(eventId)
            .subscribe(event -> {
                Notification notification = this.getNotification(context, event);
                notificationManager.notify(event.getId().hashCode(), notification);
            }, error -> logger.error(error, "Problem loading event for notification"));

    }

    private Notification getNotification(Context context, Event event) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(event.getName());
        builder.setContentText(context.getString(R.string.notification_description));
        Intent eventActivityIntent = EventDetailActivity.createIntent(context, event.getId());
        PendingIntent contentIntent = PendingIntent.getActivity(context, event.getId().hashCode(), eventActivityIntent, 0);
        builder.setAutoCancel(true);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.ic_ad);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        return builder.build();
    }
}
