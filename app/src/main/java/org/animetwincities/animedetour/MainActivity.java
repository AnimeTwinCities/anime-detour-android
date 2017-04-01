package org.animetwincities.animedetour;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import inkapplicaitons.android.logger.Logger;
import inkapplications.android.layoutinjector.Layout;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.animetwincities.animedetour.guests.ui.GuestsFragment;
import org.animetwincities.animedetour.schedule.ScheduleFragment;

import javax.inject.Inject;

@Layout(R.layout.activity_main)
public class MainActivity extends BaseActivity
{
    @Inject
    Logger logger;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ScheduleFragment scheduleFragment = new ScheduleFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, scheduleFragment).commit();
        this.initializeNavigation();
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

    /** Show the full app fragment_schedule. */
    private boolean showSchedule(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing Schedule");
        return false;
    }

    /** Show the "What's New" page. */
    private boolean showNew(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing What's New");
        return false;
    }

    /** Show the Guests List. */
    private boolean showGuests(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing Guest List");

        getSupportFragmentManager().beginTransaction().addToBackStack("")
                .replace(R.id.fragment_container, GuestsFragment.newInstance()).commit();

        return false;
    }

    /** Show the User Favorites. */
    private boolean showFavorites(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing User Favorites");
        return false;
    }

    /** Show the Con Map. */
    private boolean showMap(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing Map");
        return false;
    }

    /** Show the Settings Page. */
    private boolean showSettings(View view, int i, IDrawerItem iDrawerItem)
    {
        this.logger.trace("Showing Settings Page");
        return false;
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
                createNavigationItem(R.string.main_menu_new, R.drawable.ic_home_black_24dp, this::showNew),
                createNavigationItem(R.string.main_menu_guests, R.drawable.ic_people_black_24dp, this::showGuests),
                createNavigationItem(R.string.main_menu_favorites, R.drawable.ic_stars_black_24dp, this::showFavorites),
                createNavigationItem(R.string.main_menu_schedule, R.drawable.ic_list_black_24dp, this::showSchedule),
                createNavigationItem(R.string.main_menu_map, R.drawable.ic_map_black_24dp, this::showMap)
            )
            .withStickyFooter(
                (ViewGroup) createNavigationItem(R.string.main_menu_settings, R.drawable.ic_home_black_24dp, this::showSettings).generateView(this)
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
