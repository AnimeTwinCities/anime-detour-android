package org.animetwincities.animedetour.schedule;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import inkapplicaitons.android.logger.Logger;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeParseException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Transform Event data to and from Firebase models into local ones.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@Singleton
public class EventTransformer
{
    final private Logger logger;

    @Inject
    public EventTransformer(Logger logger) {
        this.logger = logger;
    }

    /**
     * Deserialize a Firebase data snapshot into a list of events.
     */
    public List<Event> snapshotToEventList(DataSnapshot data) {
        HashMap<String, FirebaseEvent> map = data.getValue(new GenericTypeIndicator<HashMap<String, FirebaseEvent>>(){});
        if (map == null) {
            return Collections.emptyList();
        }

        List<Event> events = new ArrayList<>(map.size());

        for (String key : map.keySet()) {
            FirebaseEvent eventData = map.get(key);
            try {
                events.add(new Event(key, eventData));
            } catch (DateTimeParseException pareError) {
                logger.error(pareError, "Problem deserializing event times.");
            }
        }

        return events;
    }
}
