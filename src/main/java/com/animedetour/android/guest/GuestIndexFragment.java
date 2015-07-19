/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.guest;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import com.animedetour.android.R;
import com.animedetour.android.database.guest.GuestRepository;
import com.animedetour.android.framework.BaseFragment;
import com.animedetour.api.guest.model.Guest;
import com.inkapplications.android.widget.recyclerview.SimpleRecyclerView;
import com.inkapplications.groundcontrol.SubscriptionManager;
import monolog.LogName;
import monolog.Monolog;
import prism.framework.DisplayName;
import prism.framework.Layout;
import rx.Subscription;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Displays a grid of the guests at Detour.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@DisplayName(R.string.guests_title)
@LogName("Guests")
@Layout(R.layout.guest_category_index)
public class GuestIndexFragment extends BaseFragment
{
    @Inject
    GuestRepository repository;

    @Bind(R.id.guest_category_index)
    SimpleRecyclerView<GuestWidgetView, Guest> categoryList;

    @Inject
    Monolog log;

    @Inject
    GuestIndexBinder binder;

    @Inject
    SubscriptionManager subscriptionManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.categoryList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        this.categoryList.init(new ArrayList<Guest>(), this.binder);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        CategoryUpdateObserver observer = new CategoryUpdateObserver(this.log, this.categoryList);
        Subscription subscription = this.repository.findAllCategories(observer);
        this.subscriptionManager.add(subscription);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        this.subscriptionManager.unsubscribeAll();
    }
}
