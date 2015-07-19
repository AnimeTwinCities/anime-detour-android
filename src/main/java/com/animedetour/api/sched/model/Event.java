/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.api.sched.model;

import com.animedetour.api.sched.deserialization.PanelDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Panel / Event
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@SuppressWarnings("UnusedDeclaration")
public class Event implements Serializable
{
    /**
     * Globally Unique ID for the panel.
     */
    @DatabaseField(id = true)
    @JsonProperty("id")
    private String id;

    /**
     * A key specific to this panel.
     */
    @DatabaseField
    @JsonProperty("event_key")
    private String eventKey;

    /**
     * The Name of the panel.
     */
    @DatabaseField
    @JsonProperty("name")
    private String name;

    /**
     * The start time of the panel.
     */
    @DatabaseField(index = true, dataType = DataType.DATE_TIME)
    @JsonProperty("event_start")
    @JsonDeserialize(using = PanelDateDeserializer.class)
    private DateTime start;

    /**
     * End time of the panel.
     */
    @DatabaseField(dataType = DataType.DATE_TIME)
    @JsonProperty("event_end")
    @JsonDeserialize(using = PanelDateDeserializer.class)
    private DateTime end;

    /**
     * The Type specified for the event. E.g. 'Programming' or 'Room Parties'.
     */
    @DatabaseField(index = true)
    @JsonProperty("event_type")
    private String eventType;

    /**
     * An optional subtype for the event. E.g. 'Panel' or 'Game'.
     */
    @DatabaseField
    @JsonProperty("event_subtype")
    private String eventSubType;

    /**
     * The detailed description of the event panel.
     */
    @DatabaseField
    @JsonProperty("description")
    private String description;

    /**
     * The name of the venue location where the panel is located.
     */
    @DatabaseField
    @JsonProperty("venue")
    private String venue;

    /**
     * A unique Identifier for the venue location where the panel is located.
     */
    @DatabaseField(index = true)
    @JsonProperty("venue_id")
    private String venueId;

    /**
     * A list of official speakers running the event.
     */
    @DatabaseField
    @JsonProperty("speakers")
    private String speakers;

    /**
     * A URL to a relevant banner image to the event.
     */
    @DatabaseField
    @JsonProperty("media_url")
    private String mediaUrl;

    /**
     * A Comma Separated list of meta-tags for the event.
     */
    @DatabaseField
    @JsonProperty("tags")
    private String tags;

    /**
     * A timestamp of when the object created locally / was fetched from the API.
     */
    @DatabaseField(dataType = DataType.DATE_TIME, index = true)
    private DateTime fetched = new DateTime();

    /**
     * @return Globally Unique ID for the panel
     */
    final public String getId()
    {
        return id;
    }

    /**
     * @param id Globally Unique ID for the panel
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return A key specific to this panel
     */
    final public String getEventKey()
    {
        return eventKey;
    }

    /**
     * @param eventKey A key specific to this panel
     */
    public void setEventKey(String eventKey)
    {
        this.eventKey = eventKey;
    }

    /**
     * @return The Name of the panel
     */
    final public String getName()
    {
        return name;
    }

    /**
     * @param name The Name of the panel
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get event Start as Joda DateTime object
     *
     * @return The start time of the panel
     */
    final public DateTime getStartDateTime()
    {
        return start;
    }

    /**
     * Set event Start as Joda DateTime object
     *
     * @param start The start time of the panel
     */
    public void setStartDateTime(DateTime start)
    {
        this.start = start;
    }

    /**
     * Get event end as Joda DateTime object
     *
     * @return The end time of the panel
     */
    final public DateTime getEndDateTime()
    {
        return end;
    }

    /**
     * Set event end as Joda DateTime object
     *
     * @param end The end time of the panel
     */
    public void setEndDateTime(DateTime end)
    {
        this.end = end;
    }

    /**
     * @return The Type specified for the event. E.g. 'Programming' or 'Room Parties'
     */
    final public String getEventType()
    {
        return eventType;
    }

    /**
     * @param eventType The Type specified for the event. E.g. 'Programming' or 'Room Parties'
     */
    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    /**
     * @return An optional subtype for the event. E.g. 'Panel' or 'Game'
     */
    final public String getEventSubType()
    {
        return eventSubType;
    }

    /**
     * @param eventSubType An optional subtype for the event. E.g. 'Panel' or 'Game'
     */
    public void setEventSubType(String eventSubType)
    {
        this.eventSubType = eventSubType;
    }

    /**
     * @return The detailed description of the event panel
     */
    final public String getDescription()
    {
        return description;
    }

    /**
     * @param description The detailed description of the event panel
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return The name of the venue location where the panel is located
     */
    final public String getVenue()
    {
        return venue;
    }

    /**
     * @param venue The name of the venue location where the panel is located
     */
    public void setVenue(String venue)
    {
        this.venue = venue;
    }

    /**
     * @return A unique Identifier for the venue location where the panel is located
     */
    final public String getVenueId()
    {
        return venueId;
    }

    /**
     * @param venueId A unique Identifier for the venue location where the panel is located
     */
    public void setVenueId(String venueId)
    {
        this.venueId = venueId;
    }

    /**
     * @return A list of official speakers running the event
     */
    final public String getSpeakers()
    {
        return speakers;
    }

    /**
     * @param speakers A list of official speakers running the event
     */
    public void setSpeakers(String speakers)
    {
        this.speakers = speakers;
    }

    /**
     * @return A URL to a relevant banner image for the event
     */
    final public String getMediaUrl()
    {
        return this.mediaUrl;
    }

    /**
     * Set the event's image.
     *
     * If the image URL set here contains spaces, they will be URLencoded before
     * storing them to the object (as `%20`'s.)
     *
     * @param url A URL to a relevant banner image for the event.
     */
    public void setMediaUrl(String url)
    {
        url = url.replaceAll(" ", "%20");
        this.mediaUrl = url;
    }

    /**
     * @return A list of meta-informational tags about the event.
     */
    public List<String> getTags()
    {
        return Arrays.asList(this.tags.split(","));
    }

    /**
     * @param tags A Comma Separated list of meta-tags for the event.
     */
    public void setTags(String tags)
    {
        this.tags = tags;
    }

    /**
     * @param tags A collection of informational meta-tags for the event.
     */
    public void setTagsList(List<String> tags)
    {
        if (tags.isEmpty()) {
            this.tags = "";
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(tags.get(0));

        for (int c = 1; c < tags.size(); c++) {
            builder.append(",");
            builder.append(tags.get(c));
        }

        this.tags = builder.toString();
    }

    final public DateTime getFetched()
    {
        return this.fetched;
    }

    public void setFetched(DateTime fetched)
    {
        this.fetched = fetched;
    }

    @Override
    public String toString()
    {
        return "Event{"
            + "id='" + id + '\''
            + ", eventKey='" + eventKey + '\''
            + ", name='" + name + '\''
            + ", start=" + start
            + ", end=" + end
            + ", eventType='" + eventType + '\''
            + ", eventSubType='" + eventSubType + '\''
            + ", description='" + description + '\''
            + ", venue='" + venue + '\''
            + ", venueId='" + venueId + '\''
            + ", speakers='" + speakers + '\''
            + ", mediaUrl='" + mediaUrl + '\''
            + ", fetched=" + fetched
            + '}';
    }
}
