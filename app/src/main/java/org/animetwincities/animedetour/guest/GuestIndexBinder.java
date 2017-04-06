package org.animetwincities.animedetour.guest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.ViewGroup;
import com.inkapplications.android.widget.recyclerview.ItemViewBinder;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityScope;

import javax.inject.Inject;

/**
 * Creates and binds new views for the guest items
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@ActivityScope
public class GuestIndexBinder implements ItemViewBinder<GuestItemView, Guest>
{
    final private Activity context;

    @Inject
    public GuestIndexBinder(Activity context)
    {
        this.context = context;
    }

    @Override
    public GuestItemView createView(ViewGroup viewGroup, int i)
    {
        return new GuestItemView(context);
    }

    @Override
    public void bindView(Guest guest, GuestItemView view)
    {
        view.setName(guest.getName());
        view.setImageUri(guest.getImage());
        view.setOnClickListener(clicked -> {
            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view.getSharedView(), "guest_image").toBundle();
            Intent intent = GuestDetailActivity.createIntent(context, guest.getId());
            context.startActivity(intent, bundle);
        });
    }
}
