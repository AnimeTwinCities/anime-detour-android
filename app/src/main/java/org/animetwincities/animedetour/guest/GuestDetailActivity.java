package org.animetwincities.animedetour.guest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import inkapplicaitons.android.logger.Logger;
import inkapplications.android.layoutinjector.Layout;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.Transitions;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;

import javax.inject.Inject;

/**
 * Activity created to host the {@link GuestDetailFragment}, which shows details
 * on a {@link Guest}.
 *
 * The fragment will instantiated and added automatically.
 */
@Layout(R.layout.fragment_container_notoolbar)
public class GuestDetailActivity extends BaseActivity
{
    private static String EXTRA_GUEST_ID = "extra_guest_id";

    @Inject
    Logger logger;

    public static Intent createIntent(Context context, String guestId)
    {
        Intent intent = new Intent(context, GuestDetailActivity.class);
        intent.putExtra(EXTRA_GUEST_ID, guestId);

        return intent;
    }

    @Override
    public void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override
    public void onBackPressed() {
        Transitions.finishAfterTransition(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transitions.postponeEnterTransition(this);
        Transitions.draweeCropTransition(this);

        if (savedInstanceState == null && getGuestId() != null) {
            GuestDetailFragment fragment = new GuestDetailFragment(getGuestId());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_notoolbar_fragment, fragment).commit();
        }
    }

    private String getGuestId()
    {
        if (getIntent().getExtras().containsKey(EXTRA_GUEST_ID)) {
            return getIntent().getExtras().getString(EXTRA_GUEST_ID);
        }
        logger.error("Loaded Guest Details without an ID!");
        finish();
        return null;
    }
}
