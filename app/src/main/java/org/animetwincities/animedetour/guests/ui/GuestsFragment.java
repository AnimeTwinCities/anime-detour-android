package org.animetwincities.animedetour.guests.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.animetwincities.animedetour.guests.model.Guest;
import org.animetwincities.animedetour.guests.model.GuestRepository;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Primary fragment for displaying a list of Anime Detour guest information. Designed to be hosted
 * the {@link org.animetwincities.animedetour.MainActivity}
 */
public class GuestsFragment extends BaseFragment
{

    final static private int GUEST_GRID_SPAN_COUNT = 2;

    @Inject
    GuestRepository guestRepository;

    @BindView(R.id.guests_guest_list)
    RecyclerView guestList;

    private GuestsAdapter guestsAdapter;

    public static GuestsFragment newInstance() {
        return new GuestsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_guests, container, false);
        ButterKnife.bind(this, layout);

        return layout;
    }

    @Override
    public void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeAdapterAndRecyclerView();
        guestRepository.observeGuests().subscribe(this::loadGuestsIntoUi);
    }


    private void initializeAdapterAndRecyclerView() {
        this.guestsAdapter = new GuestsAdapter(getContext());
        this.guestsAdapter.getOnClickObservable().subscribe(this::onClickGuest);
        this.guestList.setLayoutManager(new GridLayoutManager(getContext(), GUEST_GRID_SPAN_COUNT));
        this.guestList.setAdapter(guestsAdapter);
    }

    private void loadGuestsIntoUi(List<Guest> guests) {
        this.guestsAdapter.setGuestList(guests);
    }

    private void onClickGuest(Pair<View, Guest> viewGuestPair) {
        Intent detailIntent = new Intent(getContext(), GuestDetailActivity.class);
        detailIntent.putExtra(GuestDetailActivity.KEY_GUEST_ID, viewGuestPair.second.getId());
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                viewGuestPair.first, "guest_image");
        startActivity(detailIntent, optionsCompat.toBundle());
    }

}
