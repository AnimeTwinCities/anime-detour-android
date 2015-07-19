/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.api.sched;

import com.animedetour.api.sched.model.Event;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

import java.util.List;

public interface ScheduleEndpoint
{
    @GET("/sched_events")
    public void getSchedule(Callback<List<Event>> callback);

    @GET("/sched_events")
    public List<Event> getSchedule();

    @GET("/sched_events")
    public List<Event> getSchedule(@Query("since") long sinceTimeStamp);

    @GET("/sched_events")
    public List<Event> getSchedule(@Query("since") long sinceTimeStamp, @Query("status") String status);
}
