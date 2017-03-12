package org.animetwincities.animedetour.guests;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.animetwincities.animedetour.R;

import java.util.List;

/**
 * RecyclerView Adapter to display Guest information.
 *
 * Should be initialized with a {@link android.support.v7.widget.GridLayoutManager}
 */
public class GuestsAdapter extends RecyclerView.Adapter<GuestViewHolder> {

    private Context context;
    private List<Guest> guestList;

    public GuestsAdapter(Context context) {
        this.context = context;
    }

    /**
     * Sets a list of {@link Guest} on the Adapter, and refreshes the adapter.
     *
     * @param guestList
     *          List of Guests to render into the adapter.
     */
    public void setGuestList(List<Guest> guestList) {
        this.guestList = guestList;
        notifyDataSetChanged();
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GuestViewHolder(View.inflate(this.context, R.layout.list_item_guest, null));
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        holder.bind(this.guestList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.guestList != null ? this.guestList.size() : 0;
    }
}
