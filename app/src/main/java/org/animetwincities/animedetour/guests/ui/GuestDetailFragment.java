package org.animetwincities.animedetour.guests.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;

/**
 * Fragment to display detail about a specific Guest
 */
public class GuestDetailFragment extends BaseFragment {

    private static String KEY_GUEST_ID = "key_guest_id";

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
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

}
