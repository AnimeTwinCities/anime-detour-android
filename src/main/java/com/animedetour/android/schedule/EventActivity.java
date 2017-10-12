/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014,2016 Anime Twin Cities, Inc.
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

import butterknife.BindView;
import butterknife.OnClick;
import com.animedetour.android.R;
import com.animedetour.android.analytics.EventFactory;
import com.animedetour.android.database.event.EventRepository;
import com.animedetour.android.database.favorite.FavoriteRepository;
import com.animedetour.android.framework.BaseActivity;
import com.animedetour.android.model.Event;
import com.animedetour.android.schedule.favorite.Favorite;
import com.animedetour.android.schedule.notification.EventNotificationManager;
import com.animedetour.android.view.FinishClickListener;
import com.animedetour.android.view.StarFloatingActionButton;
import com.animedetour.android.view.fader.ToolbarFader;
import com.animedetour.android.view.fader.ToolbarFaderFactory;
import com.animedetour.android.view.scrim.ImageScrim;
import com.google.common.base.Joiner;
import com.wefika.flowlayout.FlowLayout;
import monolog.LogName;
import monolog.Monolog;
import org.joda.time.DateTime;
import prism.framework.DisplayName;
import prism.framework.Layout;

import javax.inject.Inject;
import java.sql.SQLException;

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
    @BindView(R.id.event_banner)
    ImageScrim bannerView;

    /**
     * Main content section of the event.
     */
    @BindView(R.id.event_text)
    TextView descriptionView;

    @BindView(R.id.event_speakers)
    TextView speakers;

    @BindView(R.id.event_type)
    TextView eventType;

    @BindView(R.id.event_type_container)
    View eventTypeContainer;

    @BindView(R.id.event_details)
    TextView eventDetails;

    @BindView(R.id.event_action_bar)
    Toolbar actionBar;

    @BindView(R.id.event_add)
    StarFloatingActionButton addButton;

    @BindView(R.id.event_container)
    ScrollView detailsContainer;

    @BindView(R.id.event_age_warning)
    TextView ageWarning;

    @BindView(R.id.event_hoh_message)
    View hohMessage;

    @BindView(R.id.event_tags)
    FlowLayout tags;

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

        if (this.event.getTags().contains("21+")) {
            String warning = this.getString(R.string.event_age_warning, "21+");
            this.ageWarning.setText(warning);
            this.ageWarning.setVisibility(View.VISIBLE);
        } else if (this.event.getTags().contains("18+")) {
            String warning = this.getString(R.string.event_age_warning, "18+");
            this.ageWarning.setText(warning);
            this.ageWarning.setVisibility(View.VISIBLE);
        } else {
            this.ageWarning.setVisibility(View.GONE);
        }

        if (this.event.getTags().contains("asl")) {
            this.hohMessage.setVisibility(View.VISIBLE);
        } else {
            this.hohMessage.setVisibility(View.GONE);
        }

        String type = this.event.getCategory();
        this.eventType.setText(type);
        int colorRes = this.eventPalette.getDimColor(type);
        int color = this.getResources().getColor(colorRes);
        this.eventTypeContainer.setBackgroundColor(color);
        int bannerColorRes = this.eventPalette.getColor(type);
        int bannerColor = this.getResources().getColor(bannerColorRes);
        this.bannerView.setBackgroundColor(bannerColor);
        if (null != this.event.getHosts()) {
            this.speakers.setText(Joiner.on(",").join(this.event.getHosts()));
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

        for (String tag : this.event.getTags()) {
            if (tag.trim().isEmpty()) { continue; }
            TextView tagView = (TextView) getLayoutInflater().inflate(R.layout.tag, this.tags, false);
            tagView.setText(tag);
            this.tags.addView(tagView);
        }
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
        DateTime start = this.event.getStart();
        DateTime end = this.event.getEnd();

        String format = start.getDayOfYear() == end.getDayOfYear() ? this.getString(R.string.panel_details) : this.getString(R.string.panel_details_multiday);
        String location = this.event.getRoom();

        return String.format(format, start.toDate(), end.toDate(), location);
    }

    /**
     * Updates the top banner based on the event data
     */
    protected void updateBannerImage()
    {
        if (null == this.event.getBanner()) {
            return;
        }

        this.bannerView.expandImage();
        this.bannerView.setImage(this.event.getBanner());
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
