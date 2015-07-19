/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.framework.dependencyinjection.module;

import android.app.Application;
import com.animedetour.android.framework.NetworkSlowdown;
import com.facebook.stetho.okhttp.StethoInterceptor;
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
    public OkHttpClient provideOkHttp(
        Application application,
        Cache cache,
        NetworkSlowdown slowdownInterceptor
    ) {
        OkHttpClient client = new OkHttpClient();
        client.setCache(cache);
        client.networkInterceptors().add(new StethoInterceptor());
        client.networkInterceptors().add(slowdownInterceptor);
        return client;
    }
}
