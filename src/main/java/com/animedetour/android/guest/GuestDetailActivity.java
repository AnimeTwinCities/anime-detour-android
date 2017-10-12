/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.guest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.*;
import com.animedetour.android.R;
import com.animedetour.android.framework.BaseActivity;
import com.animedetour.android.view.fader.ToolbarFader;
import com.animedetour.android.view.fader.ToolbarFaderFactory;
import com.animedetour.android.view.scrim.ImageScrim;
import com.animedetour.api.guest.model.Guest;
import monolog.LogName;
import monolog.Monolog;
import prism.framework.DisplayName;
import prism.framework.Layout;

import javax.inject.Inject;

/**
 * A detailed view of a particular guest.
 *
 * This activity REQUIRES the following arguments to start:
 *  * ARG_GUEST
 *
 * @see #ARG_GUEST
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
@Layout(R.layout.guest_detail)
@DisplayName(R.string.guest_detail_title)
@LogName("Guest Detail")
final public class GuestDetailActivity extends BaseActivity
{
    @BindView(R.id.guest_avatar)
    ImageScrim avatar;

    @BindView(R.id.guest_bio)
    TextView bio;

    @BindView(R.id.guest_category)
    TextView category;

    @BindView(R.id.guest_action_bar)
    Toolbar actionBar;

    @BindView(R.id.guest_scroll)
    ScrollView detailsContainer;

    @Inject
    ToolbarFaderFactory faderFactory;

    @Inject
    Monolog log;

    /**
     * Argument flag for the serialized guest object extra to be passed into the
     * intent starting this activity.
     */
    final private static String ARG_GUEST = "guest";

    /**
     * The guest object being displayed on the activity.
     */
    private Guest guest;

    /**
     * Properly constructs the guest details event, basically a constructor for the activity.
     *
     * @param guest The guest to display on the activity page.
     */
    public static Intent createIntent(Context context, Guest guest)
    {
        Intent intent = new Intent(context, GuestDetailActivity.class);
        intent.putExtra(ARG_GUEST, guest);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.guest = (Guest) this.getIntent().getSerializableExtra(ARG_GUEST);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        this.actionBar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        this.actionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                GuestDetailActivity.this.finish();
            }
        });

        this.bio.setText(Html.fromHtml(this.guest.getBio()));
        this.bio.setMovementMethod(LinkMovementMethod.getInstance());
        this.category.setText(this.guest.getCategory().getName());
        this.avatar.setTitle(this.guest.getFullName());
        this.avatar.expandImage();
        this.avatar.setImage(this.guest.getFullPhoto());

        ToolbarFader fader = this.faderFactory.create(this.avatar, this.detailsContainer, this.actionBar);
        this.detailsContainer.getViewTreeObserver().addOnScrollChangedListener(fader);
    }
}
