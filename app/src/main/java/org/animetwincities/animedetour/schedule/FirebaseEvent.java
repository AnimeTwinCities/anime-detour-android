package org.animetwincities.animedetour.schedule;

import java.util.List;

/**
 * A 1:1 exact representation of the Event structure on firebase.
 *
 * This will be mostly stringly typed, and transformed into a local model
 * for use in the application. Do not use this model directly.
 */
class FirebaseEvent
{
    public String name;
    public String start;
    public String end;
    public String room;
    public List<String> tags;
    public String category;
    public List<String> hosts;
    public String description;
}
