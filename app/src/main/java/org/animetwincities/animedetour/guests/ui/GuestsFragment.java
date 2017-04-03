package org.animetwincities.animedetour.guests.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.animetwincities.animedetour.guests.model.Guest;
import org.animetwincities.animedetour.guests.model.GuestRepository;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import inkapplicaitons.android.logger.Logger;

/**
 * Primary fragment for displaying a list of Anime Detour guest information. Designed to be hosted
 * the {@link org.animetwincities.animedetour.MainActivity}
 */
public class GuestsFragment extends BaseFragment
{

    final static private int GUEST_GRID_SPAN_COUNT = 2;

    @Inject
    GuestRepository guestRepository;

    @Inject
    Logger logger;

    @BindView(R.id.guests_guest_list)
    RecyclerView guestList;

    @BindView(R.id.guest_progress_spinner)
    ProgressBar progressSpinner;

    private GuestsAdapter guestsAdapter;

    public static GuestsFragment newInstance() {
        return new GuestsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_guests, container, false);
        ButterKnife.bind(this, layout);

        return layout;
    }

    @Override
    public void injectSelf(ActivityComponent component)
    {
        component.inject(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        initializeAdapterAndRecyclerView();

        this.progressSpinner.setVisibility(View.VISIBLE);

        guestRepository.observeGuests().subscribe(this::onGuestsLoaded, this::onGuestsFailedToLoad);
    }


    private void initializeAdapterAndRecyclerView()
    {
        this.guestsAdapter = new GuestsAdapter(getContext());
        this.guestsAdapter.getOnClickObservable().subscribe(this::onClickGuest);
        this.guestList.setLayoutManager(new GridLayoutManager(getContext(), GUEST_GRID_SPAN_COUNT));
        this.guestList.setAdapter(guestsAdapter);
    }

    /**
     * When Guests are successfully loaded from Firebase, take any actions to initialize the UI
     * with that information.
     *
     * @param guests
     *          List of {@link Guest} loaded from Firebase
     */
    private void onGuestsLoaded(List<Guest> guests)
    {
        this.progressSpinner.setVisibility(View.GONE);
        this.guestsAdapter.setGuestList(guests);
    }

    /**
     * If an error occurs trying to load list of {@link Guest} from Firebase, take action.
     *
     * @param error
     *          Error encountered loading Guests from Firebase
     */
    private void onGuestsFailedToLoad(Throwable error)
    {
        this.progressSpinner.setVisibility(View.GONE);

        this.logger.error(error, "Failed to load Guests from Firebase on GuestsFragment");
    }

    private void onClickGuest(Pair<View, Guest> viewGuestPair)
    {
        Intent detailIntent = new Intent(getContext(), GuestDetailActivity.class);
        detailIntent.putExtra(GuestDetailActivity.KEY_GUEST_ID, viewGuestPair.second.getId());
        startActivity(detailIntent, createActivityOptionsCompatForTransition(viewGuestPair.first));
    }

    /**
     * Prepared the {@link ActivityOptionsCompat} for the possibility of a SharedElementTransition.
     *
     * If the API level is > 21 (and therefore supports shared element transitions), we prepare
     * the appropriate Bundle. Otherwise, we return null, which will be gracefully handled
     * by the Android API on Activity start.
     *
     * @param sharedViewForTransition
     *          View which is the start of the shared transition, should be returned from
     *          {@link GuestsAdapter} onClick.
     * @return
     *          Bundle transformed from prepared ActivityOptionsCompat, or {@code null}
     */
    private Bundle createActivityOptionsCompatForTransition(View sharedViewForTransition)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    sharedViewForTransition, "guest_image").toBundle();
        } else {
            return null;
        }
    }

}
