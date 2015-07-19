/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.animedetour.android.R;
import com.animedetour.android.schedule.EventActivity;
import com.animedetour.api.sched.model.Event;

/**
 * Handles incoming intents for scheduled event notifications.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class UpcomingEventReciever extends BroadcastReceiver
{
    final public static String EXTRA_EVENT = "Bundle_Event";
    final public static String EXTRA_BUNDLE = "notification_bundle";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
        Event event = (Event) bundle.getSerializable(EXTRA_EVENT);

        Notification notification = this.getNotification(context, event);
        notificationManager.notify(event.getId().hashCode(), notification);
    }

    private Notification getNotification(Context context, Event event) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(event.getName());
        builder.setContentText(context.getString(R.string.notification_description));
        Intent eventActivityIntent = EventActivity.createIntent(context, event);
        PendingIntent contentIntent = PendingIntent.getActivity(context, event.getId().hashCode(), eventActivityIntent, 0);
        builder.setAutoCancel(true);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.flat_logo);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        return builder.build();
    }
}
