/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.animetwincities.animedetour.settings;

import android.widget.Switch;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import inkapplications.android.layoutinjector.Layout;
import org.animetwincities.animedetour.BuildConfig;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.AppConfig;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.animetwincities.animedetour.schedule.notification.EventNotificationManager;

import javax.inject.Inject;

/**
 * Settings page for the application.
 *
 * Gives the user options to enable/disable things like notifications.
 *
 * Also includes developer options which will be displayed after tapping the
 * version number ten times.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Layout(R.layout.settings)
final public class SettingsFragment extends BaseFragment
{
    @BindView(R.id.settings_event_notification_switch)
    Switch eventNotifications;

    @BindView(R.id.settings_version_label)
    TextView versionLabel;

    @Inject
    AppConfig preferences;

    @Inject
    EventNotificationManager notificationManager;

    @Override
    public void onStart()
    {
        super.onStart();

        this.eventNotifications.setChecked(this.preferences.receiveEventNotifications());

        String versionFormat = this.getString(R.string.settings_application_version);
        String versionString = String.format(versionFormat, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        this.versionLabel.setText(versionString);
    }

    /**
     * Change whether the user wants to receive notifications for events.
     */
    @OnCheckedChanged(R.id.settings_event_notification_switch)
    public void toggleNotifications(boolean checked)
    {
        this.preferences.setEventNotifications(checked);
        this.notificationManager.toggleNotifications(checked);
    }

    @Override
    public void injectSelf(ActivityComponent component) {
        component.inject(this);
    }
}
