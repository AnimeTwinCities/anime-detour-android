package org.animetwincities.animedetour.schedule;

/**
 * A single event on the schedule.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class Event
{
    final private String name;

    public Event(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Event{" +
            "name='" + name + '\'' +
            '}';
    }
}
