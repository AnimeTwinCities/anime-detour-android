/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2014-2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.framework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.squareup.otto.Bus;
import icepick.Icepick;
import monolog.Monolog;
import prism.framework.Layout;
import prism.framework.PrismFacade;

import javax.inject.Inject;

public class BaseFragment extends android.support.v4.app.Fragment
{
    @Inject
    Monolog logger;

    @Inject
    Bus applicationBus;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        PrismFacade.bootstrap(this);
        ButterKnife.bind(this, this.getView());
        this.logger.trace(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (this.getClass().isAnnotationPresent(Layout.class)) {
            int layoutId = this.getClass().getAnnotation(Layout.class).value();
            return inflater.inflate(layoutId, container, false);
        }

        return super.getView();
    }

    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Icepick.restoreInstanceState(this, savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        this.applicationBus.register(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.applicationBus.unregister(this);
    }

    @Override public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
