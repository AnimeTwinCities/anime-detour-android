/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.api;

import com.animedetour.api.guest.GuestEndpoint;
import com.animedetour.api.sched.ScheduleEndpoint;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import monolog.Monolog;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;

import javax.inject.Singleton;

@Module(complete = false, library = true)
@SuppressWarnings("UnusedDeclaration")
final public class ApiModule
{
    @Provides
    @Singleton
    public ScheduleEndpoint scheduleEndoint(RestAdapter adapter)
    {
        return adapter.create(ScheduleEndpoint.class);
    }

    @Provides
    @Singleton
    public GuestEndpoint guestEndpoint(RestAdapter adapter)
    {
        return adapter.create(GuestEndpoint.class);
    }

    @Provides
    @Singleton
    public RestAdapter restAdapter(Monolog logger, OkHttpClient client)
    {
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setEndpoint("http://animedetour.com");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        builder.setConverter(new JacksonConverter(mapper));
        builder.setLog(new RestLogAdapter(logger));
        builder.setLogLevel(RestAdapter.LogLevel.BASIC);

        builder.setClient(new OkClient(client));

        RestAdapter adapter = builder.build();

        return adapter;
    }
}
