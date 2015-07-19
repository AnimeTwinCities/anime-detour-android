/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.framework.dependencyinjection.module;

import com.google.android.gms.analytics.Tracker;

import dagger.Module;
import dagger.Provides;
import monolog.LogLevel;
import monolog.Monolog;
import monolog.handler.Handler;
import monolog.handler.analytics.AnalyticsHandler;
import monolog.handler.console.ConsoleHandler;

import javax.inject.Singleton;

/**
 * This logger module provides a logger for production builds that will send
 * error reports to Google Analytics.
*
 * This won't be much help for debugging live, so this is only to be available
 * in non-debug builds.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Module(library = true, complete = false)
@SuppressWarnings("UnusedDeclaration")
public class LogModule
{
    @Provides @Singleton
    public Monolog logger(Tracker analytics)
    {
        Handler[] handlers = new Handler[] {
            new AnalyticsHandler(analytics),
            new ConsoleHandler("AnimeDetour", new LogLevel[] { LogLevel.ERROR, LogLevel.FATAL }),
        };

        return new Monolog(handlers);
    }
}
