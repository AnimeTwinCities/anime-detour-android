package org.animetwincities.animedetour.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.animetwincities.animedetour.R;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * View that shows an event card in a row/index of many events.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class EventRowView extends RelativeLayout
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
    private View starred;

    /**
     * A semi-transperent fade over the panel to indicate that it is in the past.
     */
    private View fadeOverlay;

    /**
     * A label to quickly indicate a minimum age for the event.
     */
    private TextView ageWarning;

    /**
     * An icon to indicate that this panel will be ASL interpreted for the hard
     * of hearing.
     */
    private View hoh;

    /**
     * A strip of color used to indicate the category.
     */
    private View color;

    public EventRowView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.init(context);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.EventRowView,
            0,
            0
        );

        String name = attributes.getString(R.styleable.EventRowView_name);
        String description = attributes.getString(R.styleable.EventRowView_description);

        this.title.setText(name);
        this.description.setText(description);
    }

    public EventRowView(Context context)
    {
        super(context);
        this.init(context);
    }

    /** Shared Constructor logic */
    private void init(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.event_item, this);
        this.title = (TextView) this.findViewById(R.id.event_row_name);
        this.description = (TextView) this.findViewById(R.id.event_row_description);
        this.starred = this.findViewById(R.id.event_row_starred);
        this.fadeOverlay = this.findViewById(R.id.event_row_overlay);
        this.color = this.findViewById(R.id.event_row_color_label);
        this.ageWarning = (TextView) this.findViewById(R.id.event_row_age_warning);
        this.hoh = this.findViewById(R.id.event_row_hoh);
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
        this.setTitle("");
        this.setDescription("");
        this.fadeOverlay.setVisibility(GONE);
        this.ageWarning.setVisibility(GONE);
        this.hoh.setVisibility(GONE);
        this.setLabelColor(Color.TRANSPARENT);
        this.starred.setVisibility(View.GONE);
    }

    /**
     * Bind a panel object to display in the view
     *
     * @param event The panel to sync data from
     */
    public void bind(Event event)
    {
        String timePlace = getTimeRangeString(event.getStart(), event.getEnd(), event.getRoom());

        this.setDescription(timePlace);
        this.setTitle(event.getName());

        if (event.getStart().isBefore(LocalDateTime.now())) {
            this.fadeOverlay.setVisibility(VISIBLE);
        } else {
            this.fadeOverlay.setVisibility(GONE);
        }

        if (event.hasTag("21+")) {
            this.ageWarning.setText("21+");
            this.ageWarning.setVisibility(VISIBLE);
        } else if (event.hasTag("18+")) {
            this.ageWarning.setText("18+");
            this.ageWarning.setVisibility(VISIBLE);
        } else {
            this.ageWarning.setVisibility(GONE);
        }

        if (event.hasTag("asl")) {
            this.hoh.setVisibility(VISIBLE);
        } else {
            this.hoh.setVisibility(GONE);
        }
    }

    public void setLabelColor(@ColorInt int color)
    {
        this.color.setBackgroundColor(color);
    }

    public void setStarred(boolean starred)
    {
        this.starred.setVisibility(starred ? VISIBLE : GONE);
    }

    /**
     * Get a time stamp for a panel.
     *
     * If the panel ends on a different day than it started, this will include
     * a day for the end time, like `Sat, 2:00pm - Sun, 8:00am`
     * Otherwise it will leave it off, like `Sat, 2:00pm - 4:00pm`
     *
     * @param start The start time of the panel
     * @param end The end time of the panel
     * @return A formatted timespan of the start and end time of the panel,
     *         e.g. `Sun, 2:00PM - 6:00PM`
     */
    protected String getTimeRangeString(LocalDateTime start, LocalDateTime end, String room)
    {
        DateTimeFormatter dayTimeFormat = DateTimeFormatter.ofPattern(getResources().getString(R.string.event_day_time_format));
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(getResources().getString(R.string.event_time_format));

        if (start.getDayOfYear() == end.getDayOfYear() && room != null && !room.isEmpty()) {
            return String.format(getResources().getString(R.string.event_detail_time_place_format), dayTimeFormat.format(start), timeFormat.format(end), room);
        } else if (start.getDayOfYear() != end.getDayOfYear() && room != null && !room.isEmpty()) {
            return String.format(getResources().getString(R.string.event_detail_time_place_format), dayTimeFormat.format(start), dayTimeFormat.format(end), room);
        } else if (start.getDayOfYear() == end.getDayOfYear() && (room == null || room.isEmpty())) {
            return String.format(getResources().getString(R.string.event_detail_time_place_format_room_missing), dayTimeFormat.format(start), timeFormat.format(end));
        } else {
            return String.format(getResources().getString(R.string.event_detail_time_place_format_room_missing), dayTimeFormat.format(start), dayTimeFormat.format(end));
        }
    }
}
