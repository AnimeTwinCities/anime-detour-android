package org.animetwincities.animedetour.schedule;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.animetwincities.animedetour.framework.AppConfig;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * Adapts a list of event models into row-like card views as an index.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class EventListAdapter extends BaseAdapter
{
    final private AppConfig config;

    private List<Event> events = Collections.emptyList();
    private List<String> favorites = Collections.emptyList();

    @Inject
    public EventListAdapter(AppConfig config) {
        this.config = config;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Event getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        EventRowView eventView = null == view ? new EventRowView(viewGroup.getContext()) : (EventRowView) view;
        Event event = this.getItem(i);

        eventView.bind(event);
        eventView.setLabelColor(config.getEventPalette(event.getCategory()));
        eventView.setStarred(favorites.contains(event.getId()));

        return eventView;
    }

    public void setEvents(List<Event> events)
    {
        this.events = events;
        this.notifyDataSetChanged();
    }

    public void setFavorites(List<String> favorites)
    {
        this.favorites = favorites;
        this.notifyDataSetChanged();
    }
}
