package org.animetwincities.animedetour.schedule;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeParseException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A single event on the fragment_schedule.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class Event
{
    /**
     * Sort Event objects by their start/end times.
     *
     * Sort events by the time that they start, ascending.
     * If they're at the same time, they'll be sorted by their end time instead,
     * with the event ending first being considered earlier.
     */
    final public static Comparator<Event> TIME_SORT = (a, b) -> {
        if (a.getStart().equals(b.getStart())) {
            if (a.getEnd().equals(b.getEnd())) {
                return 0;
            }
            if (a.getEnd().isBefore(b.getEnd())) {
                return -1;
            } else {
                return 1;
            }
        }
        if (a.getStart().isBefore(b.getStart())) {
            return -1;
        } else {
            return 1;
        }
    };

    final private String id;
    final private String name;
    final private LocalDateTime start;
    final private LocalDateTime end;
    final private String room;
    final private String category;
    final private List<String> tags;
    final private String description;
    final private List<String> hosts;

    public Event(String id, String name, LocalDateTime start, LocalDateTime end, String room, String category, List<String> tags, String description, List<String> hosts)
    {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.room = room;
        this.category = category;
        this.tags = tags == null ? Collections.emptyList() : tags;
        this.description = description;
        this.hosts = hosts;
    }

    public Event(String key, FirebaseEvent firebaseData) throws DateTimeParseException
    {
        this(
            key,
            firebaseData.name,
            LocalDateTime.parse(firebaseData.start),
            LocalDateTime.parse(firebaseData.end),
            firebaseData.room,
            firebaseData.category,
            firebaseData.tags,
            firebaseData.description,
            firebaseData.hosts
        );
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public LocalDateTime getStart()
    {
        return start;
    }

    public LocalDateTime getEnd()
    {
        return end;
    }

    public String getRoom() {
        return room;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public String getDescription() {
        return description;
    }

    public List<String> getHosts() {
        return hosts;
    }

    @Override
    public String toString() {
        return "Event{" +
            "id='" + id + "'" +
            "name='" + name + '\'' +
            '}';
    }
}
