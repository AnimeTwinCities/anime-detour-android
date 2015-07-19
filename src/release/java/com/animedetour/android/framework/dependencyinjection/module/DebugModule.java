/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.framework.dependencyinjection.module;

import com.inkapplications.prism.EmptyApplicationCallback;
import com.inkapplications.prism.ApplicationCallback;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.lang.SuppressWarnings;

@Module(library = true, complete = false)
@SuppressWarnings("UnusedDeclaration")
final public class DebugModule
{
    @Provides
    @Singleton
    public ApplicationCallback debugCallbacks()
    {
        return new EmptyApplicationCallback();
    }
}
