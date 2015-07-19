/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule.favorite;

import com.animedetour.api.sched.model.Event;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * A record that indicates the user has marked a scheduled event as a favorite
 * or starred to keep track of for later.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@DatabaseTable
public class Favorite
{
    /**
     * Locally unique ID for storage purposes. Can be null if it is a new record.
     */
    @DatabaseField(generatedId = true)
    private Integer id;

    /**
     * The event that was favorited.
     */
    @DatabaseField(foreign = true, unique = true, index = true, foreignAutoRefresh = true)
    private Event event;

    /**
     * @return Locally unique ID for storage purposes. Can be null if it is a
     *     new record.
     */
    final public Integer getId()
    {
        return id;
    }

    /**
     * @param id Locally unique ID for storage purposes. Can be null if it is a
     *           new record.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return The event that was favorited.
     */
    final public Event getEvent()
    {
        return event;
    }

    /**
     * @param event The event that was favorited.
     */
    public void setEvent(Event event)
    {
        this.event = event;
    }
}
