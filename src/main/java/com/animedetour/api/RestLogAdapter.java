/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.api;

import monolog.Monolog;
import retrofit.RestAdapter;

/**
 * Adapt's Retrofit's Logger to our common logger interface.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class RestLogAdapter implements RestAdapter.Log
{
    final private Monolog logger;

    public RestLogAdapter(Monolog logger)
    {
        this.logger = logger;
    }

    @Override
    public void log(String message)
    {
        this.logger.debug(message);
    }
}
