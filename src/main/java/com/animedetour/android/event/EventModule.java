package com.animedetour.android.event;

import dagger.Module;

@Module(
    injects = {
        EventActivity.class,
    },
    complete = false,
    library = true
)
final public class EventModule {}