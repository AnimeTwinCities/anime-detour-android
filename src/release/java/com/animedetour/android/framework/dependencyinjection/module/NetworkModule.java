/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.framework.dependencyinjection.module;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(library = true, complete = false)
@SuppressWarnings("UnusedDeclaration")
public class NetworkModule
{
    @Provides
    @Singleton
    public OkHttpClient okHttp(Cache cache)
    {
        OkHttpClient client = new OkHttpClient();
        client.setCache(cache);

        return client;
    }
}
