package org.animetwincities.animedetour.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;
import inkapplicaitons.android.logger.Logger;
import inkapplications.android.layoutinjector.Layout;
import io.reactivex.disposables.CompositeDisposable;
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;
import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.framework.AppConfig;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.auth.AuthRepository;
import org.animetwincities.animedetour.framework.auth.User;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;
import org.animetwincities.animedetour.schedule.notification.EventNotificationManager;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import javax.inject.Inject;
import java.util.List;

@Layout(R.layout.event_detail)
public class EventDetailActivity extends BaseActivity
{
    final private static String EXTRA_EVENT_ID = "event_id";

    @Inject
    ScheduleRepository scheduleRepository;

    @Inject
    AuthRepository authRepository;

    @Inject
    Logger logger;

    @Inject
    AppConfig config;

    @Inject
    EventNotificationManager notificationManager;

    @BindView(R.id.event_detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.event_detail_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.event_detail_description)
    TextView bio;

    @BindView(R.id.event_detail_time_place)
    TextView timePlace;

    @BindView(R.id.event_detail_progress)
    ProgressBar progressSpinner;

    @BindView(R.id.event_detail_favorite_button)
    FloatingActionButton favoriteButton;

    @BindView(R.id.event_hoh_message)
    View hohMessage;

    @BindView(R.id.event_age_warning)
    TextView ageWarning;

    @BindView(R.id.event_detail_type)
    TextView eventType;

    @BindView(R.id.event_detail_speakers)
    TextView speakers;

    @BindView(R.id.event_detail_type_container)
    View detailStrip;

    @State
    boolean favorite;

    private CompositeDisposable disposables;

    public static Intent createIntent(Context context, String eventId)
    {
        Intent intent = new Intent(context, EventDetailActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);

        return intent;
    }

    @Override
    public void injectSelf(ActivityComponent component)
    {
        component.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        toolbar.setTitle("");
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        disposables = new CompositeDisposable();
        disposables.addAll(
            scheduleRepository.observeEvent(getEventId()).subscribe(this::onNewEvent, this::onEventError)
        );
    }

    @Override
    protected void onNewUser(@NonNull User user) {
        super.onNewUser(user);

        disposables.add(
            scheduleRepository.observeFavorites(user.getId()).subscribe(this::onNewFavorites, this::onEventError)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();

        disposables.dispose();
    }

    @OnClick(R.id.event_detail_favorite_button)
    void clickFavorite()
    {
        User user = authRepository.getCurrentUser();
        if (user == null) {
            logger.error("Tried to favorite without a user!");
            return;
        }
        if (favorite) {
            scheduleRepository.unfavoriteEvent(user.getId(), getEventId());
            notificationManager.cancelNotification(getEventId());
        } else {
            scheduleRepository.favoriteEvent(user.getId(), getEventId());
            disposables.add(notificationManager.scheduleNotification(getEventId()));
        }
    }

    public String getEventId()
    {
        return getIntent().getExtras().getString(EXTRA_EVENT_ID);
    }

    private void onNewEvent(Event event)
    {
        progressSpinner.setVisibility(View.GONE);
        collapsingToolbarLayout.setTitle(event.getName());
        toolbar.setTitle(event.getName());
        bio.setText(event.getDescription());
        timePlace.setText(getEventDetailsString(event));
        eventType.setText(event.getCategory());
        detailStrip.setBackgroundColor(config.getEventPalette(event.getCategory()));
        setAgeWarning(event);
        setTags(event);
        setHosts(event);
    }

    private void setAgeWarning(Event event)
    {
        if (event.getTags() == null) {
            return;
        }
        if (event.getTags().contains("21+")) {
            ageWarning.setVisibility(View.VISIBLE);
            ageWarning.setText(getString(R.string.event_age_warning, "21+"));
        } else if (event.getTags().contains("18+")) {
            ageWarning.setVisibility(View.VISIBLE);
            ageWarning.setText(getString(R.string.event_age_warning, "18+"));
        }
    }

    private void setTags(Event event)
    {
        if (event.getTags() == null) {
            return;
        }
        if (event.getTags().contains("asl")) {
            this.hohMessage.setVisibility(View.VISIBLE);
        }
    }

    private void onNewFavorites(List<String> favorites)
    {
        if (favorites.contains(getEventId())) {
            favorite = true;
            this.favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            favorite = false;
            this.favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private void setHosts(Event event)
    {
        if (event.getHosts() == null || event.getHosts().isEmpty()) {
            return;
        }

        this.speakers.setText(TextUtils.join(", ", event.getHosts()));
    }

    /**
     * Get details string for event
     *
     * The details for an event include the time and location for the event.
     *
     * @return The details string for the event
     */
    protected String getEventDetailsString(Event event)
    {
        LocalDateTime start = event.getStart();
        LocalDateTime end = event.getEnd();
        String room = event.getRoom();
        DateTimeFormatter dayTimeFormat = DateTimeFormatter.ofPattern(getResources().getString(R.string.event_day_time_format));
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(getResources().getString(R.string.event_time_format));

        if (start.getDayOfYear() == end.getDayOfYear() && room != null && !room.isEmpty()) {
            return String.format(getString(R.string.event_detail_time_place_format), dayTimeFormat.format(start), timeFormat.format(end), room);
        } else if (start.getDayOfYear() != end.getDayOfYear() && room != null && !room.isEmpty()) {
            return String.format(getString(R.string.event_detail_time_place_format), dayTimeFormat.format(start), dayTimeFormat.format(end), room);
        } else if (start.getDayOfYear() == end.getDayOfYear() && (room == null || room.isEmpty())) {
            return String.format(getString(R.string.event_detail_time_place_format_room_missing), dayTimeFormat.format(start), timeFormat.format(end));
        } else {
            return String.format(getString(R.string.event_detail_time_place_format_room_missing), dayTimeFormat.format(start), dayTimeFormat.format(end));
        }
    }

    private void onEventError(Throwable error)
    {
        logger.error(error, "Problem loading Event Data");
    }
}
