/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.animedetour.android.R;
import com.animedetour.android.analytics.EventFactory;
import com.animedetour.android.database.event.EventRepository;
import com.animedetour.android.database.favorite.FavoriteRepository;
import com.animedetour.android.framework.BaseActivity;
import com.animedetour.android.schedule.favorite.Favorite;
import com.animedetour.android.schedule.notification.EventNotificationManager;
import com.animedetour.android.view.FinishClickListener;
import com.animedetour.android.view.scrim.ImageScrim;
import com.animedetour.android.view.StarFloatingActionButton;
import com.animedetour.android.view.fader.ToolbarFader;
import com.animedetour.android.view.fader.ToolbarFaderFactory;
import com.animedetour.api.sched.model.Event;
import monolog.LogName;
import monolog.Monolog;
import prism.framework.DisplayName;
import prism.framework.Layout;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Date;

/**
 * Event Activity
 *
 * This is the screen that displays detailed information for a single event.
 * This is only ever displayed on top of other activities and should be stripped
 * off of the backstack when leaving.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Layout(R.layout.event)
@DisplayName(R.string.event_details_title)
@LogName("Event")
final public class EventActivity extends BaseActivity
{
    /**
     * Name of the intent-extra that the event is serialized into.
     */
    final private static String EXTRA_EVENT = "event";

    /**
     * View for the top banner on the page that can display an image & title.
     */
    @Bind(R.id.event_banner)
    ImageScrim bannerView;

    /**
     * Main content section of the event.
     */
    @Bind(R.id.event_text)
    TextView descriptionView;

    @Bind(R.id.event_speakers)
    TextView speakers;

    @Bind(R.id.event_type)
    TextView eventType;

    @Bind(R.id.event_type_container)
    View eventTypeContainer;

    @Bind(R.id.event_details)
    TextView eventDetails;

    @Bind(R.id.event_action_bar)
    Toolbar actionBar;

    @Bind(R.id.event_add)
    StarFloatingActionButton addButton;

    @Bind(R.id.event_container)
    ScrollView detailsContainer;

    @Inject
    Monolog logger;

    @Inject
    FavoriteRepository favoriteRepository;

    @Inject
    EventNotificationManager notificationManager;

    @Inject
    EventRepository eventData;

    @Inject
    ToolbarFaderFactory faderFactory;

    @Inject
    EventPalette eventPalette;

    /**
     * The event we are currently displaying
     */
    private Event event;

    /**
     * Properly constructs the activity event, basically a constructor for the event.
     *
     * @param event The event to display details of.
     */
    public static Intent createIntent(Context context, Event event)
    {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra(EXTRA_EVENT, event);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.none);
        this.event = (Event) this.getIntent().getExtras().getSerializable(EXTRA_EVENT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        this.updateBannerImage();

        try {
            this.event = this.eventData.get(this.event.getId());
        } catch (SQLException e) {
            this.logger.error("Error when loading event details", e);
        }

        if (null != this.event.getDescription()) {
            this.descriptionView.setText(Html.fromHtml(this.event.getDescription()));
        } else {
            this.descriptionView.setText("");
        }
        this.bannerView.setTitle(this.event.getName());

        String type = this.event.getEventType();
        this.eventType.setText(type);
        int colorRes = this.eventPalette.getDimColor(type);
        int color = this.getResources().getColor(colorRes);
        this.eventTypeContainer.setBackgroundColor(color);
        int bannerColorRes = this.eventPalette.getColor(type);
        int bannerColor = this.getResources().getColor(bannerColorRes);
        this.bannerView.setBackgroundColor(bannerColor);
        if (null != this.event.getSpeakers()) {
            this.speakers.setText(Html.fromHtml(this.event.getSpeakers()));
        } else {
            this.speakers.setText("");
        }
        this.eventDetails.setText(this.getEventDetailsString());

        this.setupNavigation();

        try {
            boolean favorited = this.favoriteRepository.isFavorited(this.event);
            if (favorited) {
                this.addButton.setStarred(true);
            } else {
                this.addButton.setStarred(false);
            }
        } catch (SQLException e) {
            this.logger.error("Error when checking if event is a favorite", e);
        }

        ToolbarFader fader = this.faderFactory.create(this.bannerView, this.detailsContainer, this.actionBar);
        this.detailsContainer.getViewTreeObserver().addOnScrollChangedListener(fader);
    }

    @Override
    public void finish()
    {
        super.finish();
        this.overridePendingTransition(0, R.anim.slide_out_right);
    }

    /**
     * Add This event to the user's favorites/schedule.
     */
    @OnClick(R.id.event_add)
    public void addFavoriteEvent()
    {
        if (this.addButton.isStarred()) {
            this.unfavoriteEvent();
        } else {
            this.favoriteEvent();
        }
    }

    /**
     * Unfavorite this event and unchedule any notifications.
     */
    public void unfavoriteEvent()
    {
        try {
            this.favoriteRepository.remove(this.event);
            this.addButton.setStarred(false);
            this.notificationManager.cancelNotification(this.event);
        } catch (SQLException e) {
            this.logger.error("Error when removing Favorite", e);
        }
    }

    /**
     * Favorite this event and schedule a notification.
     */
    public void favoriteEvent()
    {
        this.logger.trace(EventFactory.favoriteEvent(this.event));

        try {
            Favorite favorite = new Favorite();
            favorite.setEvent(this.event);
            this.favoriteRepository.save(favorite);
            this.addButton.setStarred(true);
            this.notificationManager.scheduleNotification(this.event);
        } catch (SQLException e) {
            this.logger.error("Error when saving Favorite", e);
        }
    }

    /**
     * Get details string for event
     *
     * The details for an event include the time and location for the event.
     *
     * @return The details string for the event
     */
    protected String getEventDetailsString()
    {
        String format = this.getString(R.string.panel_details);
        Date start = this.event.getStartDateTime().toDate();
        Date end = this.event.getEndDateTime().toDate();
        String location = this.event.getVenue();

        return String.format(format, start, end, location);
    }

    /**
     * Updates the top banner based on the event data
     */
    protected void updateBannerImage()
    {
        if (null == this.event.getMediaUrl()) {
            return;
        }

        this.bannerView.expandImage();
        this.bannerView.setImage(this.event.getMediaUrl());
    }

    /**
     * Set up the action-bar
     */
    protected void setupNavigation()
    {
        this.actionBar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        this.actionBar.setNavigationOnClickListener(new FinishClickListener(this));
    }
}
