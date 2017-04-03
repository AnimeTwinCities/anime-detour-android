package org.animetwincities.animedetour.guests.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;

import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;

/**
 * Activity created to host the {@link GuestDetailFragment}, which shows details
 * on a {@link org.animetwincities.animedetour.guests.model.Guest}.
 *
 * The id of the guest to show should be passed in as an extra using the
 * {@link GuestDetailActivity#KEY_GUEST_ID} constant as a key.
 *
 * The fragment will instantiated and added automatically.
 */
public class GuestDetailActivity extends BaseActivity {

    public static String KEY_GUEST_ID = "key_guest_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_detail);
        Bundle extras = getIntent().getExtras();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
                    ScalingUtils.ScaleType.CENTER_CROP));
            getWindow().setSharedElementReturnTransition(DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
                    ScalingUtils.ScaleType.CENTER_CROP));
            postponeEnterTransition();
        }

        if (extras.containsKey(KEY_GUEST_ID)) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    GuestDetailFragment.newInstance(extras.getString(KEY_GUEST_ID)), "GUEST_DETAIL")
            .commit();
        }
    }

    @Override
    public void injectSelf(ActivityComponent component) {
        component.inject(this);
    }
}
