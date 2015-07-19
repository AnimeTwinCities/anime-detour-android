/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.framework.dependencyinjection.module;

import android.content.res.Resources;
import dagger.Module;
import dagger.Provides;
import monolog.LogLevel;
import monolog.Monolog;
import monolog.handler.Handler;
import monolog.handler.console.ConsoleHandler;
import monolog.handler.crashy.CrashyHandler;

import javax.inject.Singleton;

/**
 * This Log Module provides the console (logcat) logger used in debug builds.
 *
 * This is not to be made available in a production build. A different
 * logger can be found in the release folder.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Module(library = true, complete = false)
@SuppressWarnings("UnusedDeclaration")
public class LogModule
{
    @Provides
    @Singleton
    public Monolog provideLogger(Resources resources)
    {
        Handler[] handlers = new Handler[] {
            new ConsoleHandler("AnimeDetour", LogLevel.ALL),
            new CrashyHandler(new LogLevel[] { LogLevel.ERROR, LogLevel.FATAL}),
        };

        return new Monolog(handlers, false);
    }
}
