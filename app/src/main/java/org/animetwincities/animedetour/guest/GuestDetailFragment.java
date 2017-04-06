package org.animetwincities.animedetour.guest;

import android.animation.Animator;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import icepick.State;
import inkapplications.android.layoutinjector.Layout;
import io.reactivex.disposables.CompositeDisposable;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.Animations;
import org.animetwincities.animedetour.framework.Transitions;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import inkapplicaitons.android.logger.Logger;

import static org.animetwincities.animedetour.guest.GuestItemView.IMAGE_FOCUS;

/**
 * Fragment to display detail about a specific Guest
 */
@Layout(R.layout.guest_detail)
public class GuestDetailFragment extends BaseFragment
{
    @BindView(R.id.main_backdrop)
    SimpleDraweeView guestImageBackdrop;

    @BindView(R.id.main_backdrop_full)
    SimpleDraweeView guestImageBackdropFull;

    @BindView(R.id.main_toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.guest_detail_bio)
    TextView bio;

    @BindView(R.id.guest_index_spinner)
    ProgressBar progressSpinner;

    @BindView(R.id.guest_detail_scrim)
    View scrim;

    @Inject
    GuestRepository guestRepository;

    @Inject
    Logger logger;

    @State
    String guestId;

    @State
    boolean transitionComplete = false;

    private CompositeDisposable disposables;

    public GuestDetailFragment() {}

    public GuestDetailFragment(String guestId)
    {
        this.guestId = guestId;
    }

    @Override
    public void injectSelf(ActivityComponent component)
    {
        component.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        guestImageBackdropFull.getHierarchy().setActualImageFocusPoint(IMAGE_FOCUS);
        guestImageBackdrop.getHierarchy().setActualImageFocusPoint(IMAGE_FOCUS);

        disposables = new CompositeDisposable();
        disposables.addAll(
            guestRepository.observeGuest(guestId).subscribe(this::onGuestLoaded, this::onGuestFailedToLoad)
        );
    }

    @Override
    public void onStop() {
        super.onStop();

        disposables.dispose();
    }

    private void onGuestFailedToLoad(Throwable error)
    {
        this.progressSpinner.setVisibility(View.GONE);

        this.logger.error(error, "Error occurred loading Guest from Firebase on GuestDetailFragment");

    }

    private void onGuestLoaded(Guest guest)
    {
        progressSpinner.setVisibility(View.GONE);
        bio.setText(guest.getBio());
        initToolbar(guest.getName());
        guestImageBackdropFull.setImageURI(guest.getImage());
        guestImageBackdrop.setController(createGuestImageController(guest.getImage()));
    }

    private void animateImageIn()
    {
        if (transitionComplete) {
            scrim.setVisibility(View.VISIBLE);
            guestImageBackdropFull.setVisibility(View.VISIBLE);
            return;
        }
        transitionComplete = true;

        Animations.reveal(guestImageBackdropFull, 200, 200);
        Animations.fadeIn(scrim, 500, 400);
    }

    private void initToolbar(String title)
    {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setSupportActionBar(this.toolbar);

        ActionBar actionBar =  baseActivity.getSupportActionBar();
        setHasOptionsMenu(true);

        actionBar.setTitle(title);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Creates a new {@link DraweeController} with a listener which will appropriately start
     * the delayed shared element transition as soon as the image is loaded.
     */
    private DraweeController createGuestImageController(String uri)
    {
        ControllerListener<ImageInfo> listener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                Transitions.startPostponedEnterTransition(getActivity());
                animateImageIn();
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                Transitions.startPostponedEnterTransition(getActivity());
            }
        };

        return Fresco.newDraweeControllerBuilder().setControllerListener(listener).setUri(uri).build();
    }
}
