package org.animetwincities.animedetour.guests.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.guests.model.Guest;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * RecyclerView Adapter to display Guest information.
 *
 * Should be initialized with a {@link android.support.v7.widget.GridLayoutManager}
 */
public class GuestsAdapter extends RecyclerView.Adapter<GuestViewHolder> {

    private final Context context;
    private List<Guest> guestList;

    private final PublishSubject<Guest> onClickSubject = PublishSubject.create();

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
        holder.itemView.setOnClickListener(view -> onClickSubject.onNext(guestList.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.guestList != null ? this.guestList.size() : 0;
    }

    /**
     * @return
     *          Observable which will emit specific guests in accordance with onClickEvents
     *          on the adapter.
     */
    public Observable<Guest> getOnClickObservable() {
        return this.onClickSubject;
    }
}
