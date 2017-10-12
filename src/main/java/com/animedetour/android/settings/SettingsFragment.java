/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.settings;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.animedetour.android.BuildConfig;
import com.animedetour.android.R;
import com.animedetour.android.framework.BaseFragment;
import com.animedetour.android.schedule.notification.EventNotificationManager;
import monolog.LogName;
import prism.framework.DisplayName;
import prism.framework.Layout;

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
@DisplayName(R.string.settings_title)
@LogName("Settings")
@Layout(R.layout.settings)
final public class SettingsFragment extends BaseFragment
{
    /**
     * Keeps track of clicks on the version number so we can display dev settings.
     */
    private int versionClicks = 0;

    /**
     * Settings that should only be displayed to developers.
     */
    @BindViews({R.id.settings_event_generate, R.id.settings_drop_data, R.id.settings_fake_slowdown})
    View[] developerViews;

    @BindView(R.id.settings_event_notification_switch)
    Switch eventNotifications;

    @BindView(R.id.settings_fake_slowdown_switch)
    Switch fakeSlowdown;

    @BindView(R.id.settings_version_label)
    TextView versionLabel;

    @Inject
    PreferenceManager preferences;

    @Inject
    EventNotificationManager notificationManager;

    @Inject
    DeveloperShims developerShims;

    @Override
    public void onStart()
    {
        super.onStart();

        this.eventNotifications.setChecked(this.preferences.receiveEventNotifications());
        this.fakeSlowdown.setChecked(this.preferences.fakeSlowConnections());

        String versionFormat = this.getString(R.string.settings_application_version);
        String versionString = String.format(versionFormat, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        this.versionLabel.setText(versionString);

        if (this.preferences.isDeveloper()) {
            this.showDeveloperSettings();
        }
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

    /**
     * Change whether the user wants to receive notifications for events.
     */
    @OnCheckedChanged(R.id.settings_fake_slowdown_switch)
    public void toggleSlowdown(boolean checked)
    {
        this.preferences.setFakeSlowConnections(checked);
    }

    /**
     * Click on the version number, enable dev settings if over 10 clicks.
     */
    @OnClick(R.id.settings_version)
    public void trackDeveloperClick()
    {
        ++versionClicks;

        if (versionClicks > 10 && false == this.preferences.isDeveloper()) {
            this.showDeveloperSettings();
            this.preferences.setDeveloper(true);
            Toast.makeText(this.getActivity(), "Developer Unlocked!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Developer setting: Generate a new upcoming event.
     */
    @OnClick(R.id.settings_event_generate_button)
    public void generateEvent()
    {
        this.developerShims.addFakeUpcomingEvent();
        Toast.makeText(this.getActivity(), "Event scheduled!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Developer setting: Drop all local data.
     */
    @OnClick(R.id.settings_drop_data_button)
    public void dropData()
    {
        this.developerShims.dropData();
        Toast.makeText(this.getActivity(), "Local Data dropped!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Developer setting: Drop all local data and preferences.
     */
    @OnLongClick(R.id.settings_drop_data_button)
    public boolean dropAllData()
    {
        this.developerShims.dropData();
        this.developerShims.dropPreferences();
        Toast.makeText(this.getActivity(), "ALL Data and preferences dropped!", Toast.LENGTH_SHORT).show();

        return true;
    }

    /**
     * Show the developer settings views.
     */
    protected void showDeveloperSettings()
    {
        for (View view : this.developerViews) {
            view.setVisibility(View.VISIBLE);
        }
    }
}
