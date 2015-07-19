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
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.animedetour.android.R;
import com.animedetour.api.sched.model.Event;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Panel tile view
 *
 * This is a view for the small card / list format of a panel.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class PanelView extends RelativeLayout
{
    /**
     * The title of the panel, displayed at the top of the card
     */
    private TextView title;

    /**
     * The description caption on the card
     */
    private TextView description;

    /**
     * An icon indicating if the user has the panel starred.
     */
    private ImageView starred;

    /**
     * A semi-transperent fade over the panel to indicate that it is in the past.
     */
    private View fadeOverlay;

    private View color;

    /**
     * The time format to use for the panel start and end time
     */
    final private static DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("hh:mma");

    public PanelView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.init(context);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.PanelView,
            0,
            0
        );

        String name = attributes.getString(R.styleable.PanelView_name);
        String description = attributes.getString(R.styleable.PanelView_description);

        this.title.setText(name);
        this.description.setText(description);
    }

    public PanelView(Context context)
    {
        super(context);
        this.init(context);
    }

    /** Shared Constructor logic */
    private void init(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.view_panel, this);
        this.title = (TextView) this.findViewById(R.id.view_panel_name);
        this.description = (TextView) this.findViewById(R.id.view_panel_description);
        this.starred = (ImageView) this.findViewById(R.id.view_panel_starred);
        this.fadeOverlay = this.findViewById(R.id.view_panel_overlay);
        this.starred = (ImageView) this.findViewById(R.id.view_panel_starred);
        this.color = this.findViewById(R.id.view_panel_color_label);
    }

    /**
     * @param title The title of the panel, displayed at the top of the card
     */
    public void setTitle(String title)
    {
        this.title.setText(title);
    }

    /**
     * @param description The description caption on the card
     */
    public void setDescription(String description)
    {
        this.description.setText(description);
    }

    /**
     * Reset the view to a default state; intended to be used when the view
     * gets recycled by a listview.
     */
    public void reset()
    {
        this.setLabelColor(android.R.color.transparent);
        this.setTitle("");
        this.setDescription("");
        this.fadeOverlay.setVisibility(GONE);
    }

    /**
     * Bind a panel object to display in the view
     *
     * @param event The panel to sync data from
     */
    public void bind(Event event)
    {
        String timeRange = this.getTimeRangeString(event.getStartDateTime(), event.getEndDateTime());
        String inPreposition = this.getContext().getString(R.string.in_preposition);
        String venue = event.getVenue();
        String fullDescription = timeRange + " " + inPreposition + " " + venue;

        this.setDescription(fullDescription);
        this.setTitle(event.getName());

        if (event.getStartDateTime().isBeforeNow()) {
            this.fadeOverlay.setVisibility(VISIBLE);
        } else {
            this.fadeOverlay.setVisibility(GONE);
        }
    }

    /**
     * Set the highlight color for the panel used to indicate the category/type.
     *
     * @param colorResource The color resource ID to display.
     */
    public void setLabelColor(@ColorRes int colorResource)
    {
        int color = this.getResources().getColor(colorResource);
        this.color.setBackgroundColor(color);
    }

    /**
     * @param start The start time of the panel
     * @param end The end time of the panel
     * @return A formated timespan of the start and end time of the panel, e.g. "2:00PM - 6:00PM"
     */
    protected String getTimeRangeString(DateTime start, DateTime end)
    {
        return PanelView.TIME_FORMAT.print(start) + " - " + PanelView.TIME_FORMAT.print(end);
    }
}
