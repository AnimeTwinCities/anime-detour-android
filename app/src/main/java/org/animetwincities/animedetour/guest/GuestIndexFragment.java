package org.animetwincities.animedetour.guest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import com.inkapplications.android.widget.recyclerview.SimpleRecyclerView;
import inkapplications.android.layoutinjector.Layout;
import io.reactivex.disposables.CompositeDisposable;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.BaseFragment;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import inkapplicaitons.android.logger.Logger;

/**
 * Primary fragment for displaying a list of Anime Detour guest information. Designed to be hosted
 * the {@link org.animetwincities.animedetour.MainActivity}
 */
@Layout(R.layout.guest_index)
public class GuestIndexFragment extends BaseFragment {
    @Inject
    GuestRepository guestRepository;

    @Inject
    Logger logger;

    @Inject
    GuestIndexBinder binder;

    @BindView(R.id.guest_index_recyclerview)
    SimpleRecyclerView<GuestItemView, Guest> guestList;

    @BindView(R.id.guest_index_spinner)
    ProgressBar progressSpinner;

    private CompositeDisposable disposables;
    private GridLayoutManager manager = new GridLayoutManager(getContext(), 1);

    private ViewTreeObserver.OnGlobalLayoutListener resizeGrid = () -> {
        int spanCount = (int) Math.max(1, guestList.getWidth() / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()));
        manager.setSpanCount(spanCount);
    };

    @Override
    public void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        guestList.setLayoutManager(manager);
        guestList.init(Collections.emptyList(), binder);
        guestList.getViewTreeObserver().addOnGlobalLayoutListener(this.resizeGrid);
    }

    @Override
    public void onStart() {
        super.onStart();

        disposables = new CompositeDisposable();
        disposables.addAll(
            guestRepository.observeGuests().subscribe(this::onGuestsLoaded, this::onGuestsFailedToLoad)
        );
    }

    @Override
    public void onStop() {
        super.onStop();

        disposables.dispose();
        guestList.getViewTreeObserver().removeOnGlobalLayoutListener(this.resizeGrid);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        guestList.getViewTreeObserver().removeOnGlobalLayoutListener(this.resizeGrid);
    }

    /**
     * When Guests are successfully loaded from Firebase, take any actions to initialize the UI
     * with that information.
     *
     * @param guests List of {@link Guest} loaded from Firebase
     */
    private void onGuestsLoaded(List<Guest> guests) {
        this.progressSpinner.setVisibility(View.GONE);
        this.guestList.getItemAdapter().setItems(guests);
    }

    /**
     * If an error occurs trying to load list of {@link Guest} from Firebase, take action.
     *
     * @param error Error encountered loading Guests from Firebase
     */
    private void onGuestsFailedToLoad(Throwable error) {
        this.progressSpinner.setVisibility(View.GONE);

        this.logger.error(error, "Failed to load Guests from Firebase on GuestIndexFragment");
    }
}
