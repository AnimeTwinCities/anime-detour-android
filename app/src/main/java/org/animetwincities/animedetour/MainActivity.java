package org.animetwincities.animedetour;

import android.os.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;

import com.mikepenz.materialdrawer.*;
import com.mikepenz.materialdrawer.model.*;
import com.mikepenz.materialdrawer.model.interfaces.*;

import org.animetwincities.animedetour.framework.*;
import org.animetwincities.animedetour.framework.dependencyinjection.*;
import org.animetwincities.animedetour.guest.*;
import org.animetwincities.animedetour.map.*;
import org.animetwincities.animedetour.privacypolicy.PrivacyPolicyFragment;
import org.animetwincities.animedetour.schedule.*;
import org.animetwincities.animedetour.settings.*;

import javax.inject.*;

import butterknife.*;
import inkapplicaitons.android.logger.*;
import inkapplications.android.layoutinjector.*;

@Layout(R.layout.fragment_container)
public class MainActivity extends BaseActivity
{
    final private static String MAIN_FRAGMENT_TAG = "main_fragment";

    @Inject
    Logger logger;

    @BindView(R.id.fragment_container_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Transitions.draweeCropTransition(this);
        initializeNavigation();

        if (null == savedInstanceState) {
            this.showSchedule(null, 0, null);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    public void injectSelf(ActivityComponent component)
    {
        component.inject(this);
    }

    /** Show the full app schedule. */
    private boolean showSchedule(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing Schedule");
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_fragment, scheduleFragment, MAIN_FRAGMENT_TAG).commit();

        return false;
    }

    /** Show the Guests List. */
    private boolean showGuests(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing Guest List");

        GuestIndexFragment fragment = new GuestIndexFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_fragment, fragment, MAIN_FRAGMENT_TAG).commit();

        return false;
    }

    /** Show the User Favorites. */
    private boolean showFavorites(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing User Favorites");
        FavoritesFragment fragment = new FavoritesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_fragment, fragment, MAIN_FRAGMENT_TAG).commit();
        return false;
    }

    /** Show the Con Map. */
    private boolean showMap(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing Map");

        HotelMapFragment fragment = new HotelMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_fragment, fragment, MAIN_FRAGMENT_TAG).commit();
        return false;
    }

    /** Show the Settings Page. */
    private boolean showSettings(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing Settings Page");
        SettingsFragment fragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_fragment, fragment, MAIN_FRAGMENT_TAG).commit();
        return false;
    }

    private boolean showPrivacyPolicy(View view, int i, IDrawerItem iDrawerItem) {
        PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_fragment, fragment, MAIN_FRAGMENT_TAG).commit();
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG) instanceof ScheduleFragment) {
            super.onBackPressed();
        } else {
            showSchedule(null, 0, null);
        }
    }

    /**
     * Set-up the Toolbar and the Navigation Drawer
     */
    private void initializeNavigation()
    {
        this.setSupportActionBar(toolbar);

        new DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .addDrawerItems(
                createNavigationItem(R.string.main_menu_schedule, R.drawable.ic_list_black_24dp, this::showSchedule),
                createNavigationItem(R.string.main_menu_favorites, R.drawable.ic_stars_black_24dp, this::showFavorites),
                createNavigationItem(R.string.main_menu_guests, R.drawable.ic_people_black_24dp, this::showGuests),
                createNavigationItem(R.string.main_menu_map, R.drawable.ic_map_black_24dp, this::showMap),
                new DividerDrawerItem(),
                createNavigationItem(R.string.main_menu_settings, R.drawable.ic_settings_black_24dp, this::showSettings),
                    createNavigationItem(R.string.main_menu_privacy_policy, R.drawable.ic_lock_black_24dp, this::showPrivacyPolicy)
            )
            .withAccountHeader(
                new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.drawable.ic_banner_2018)
                    .build()
            )
            .build();
    }

    /**
     * Create a navigation item for the drawer.
     */
    private PrimaryDrawerItem createNavigationItem(@StringRes int label, @DrawableRes int icon, Drawer.OnDrawerItemClickListener listener)
    {
        return new PrimaryDrawerItem()
            .withName(label)
            .withIcon(icon)
            .withOnDrawerItemClickListener(listener)
            .withTextColor(getResources().getColor(R.color.drawer_text_color))
            .withSelectedTextColor(getResources().getColor(R.color.drawer_text_color))
            .withIconColor(getResources().getColor(R.color.drawer_icon_color));
    }
}
