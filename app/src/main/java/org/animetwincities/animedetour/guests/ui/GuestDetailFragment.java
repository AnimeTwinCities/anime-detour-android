package org.animetwincities.animedetour.guests.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.animetwincities.animedetour.guests.model.Guest;
import org.animetwincities.animedetour.guests.model.GuestRepository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment to display detail about a specific Guest
 */
public class GuestDetailFragment extends BaseFragment {

    private static String KEY_GUEST_ID = "key_guest_id";

    @BindView(R.id.main_backdrop)
    SimpleDraweeView guestImageBackdrop;

    @BindView(R.id.main_toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.guest_detail_bio)
    TextView bio;

    @Inject
    GuestRepository guestRepository;

    /**
     * Creates a new instance of {@link GuestDetailFragment} instantiated with
     * the guest ID you wish to show detail for (passed in).
     *
     * Call this rather than instantiating from new.
     *
     * @param guestId
     *          int ID for the Guest to show detail for.
     * @return
     *          Instantiated fragment
     */
    public static GuestDetailFragment newInstance(String guestId) {
        GuestDetailFragment newInstance = new GuestDetailFragment();

        Bundle extras = new Bundle();
        extras.putString(KEY_GUEST_ID, guestId);

        newInstance.setArguments(extras);

        return newInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_detail, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity().getIntent().getExtras().containsKey(KEY_GUEST_ID)) {
            this.loadGuest(getActivity().getIntent().getExtras().getString(KEY_GUEST_ID));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadGuest(String id) {
        this.guestRepository.observeGuest(id).subscribe(this::onGuestLoaded);
    }

    private void onGuestLoaded(Guest guest) {
        this.bio.setText(guest.getBio());
        this.initToolbar(guest.getName());

        this.guestImageBackdrop.setImageURI(guest.getImage());
    }

    private void initToolbar(String title) {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setSupportActionBar(this.toolbar);

        ActionBar actionBar =  baseActivity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
