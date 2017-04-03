package org.animetwincities.animedetour.guests.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.animetwincities.animedetour.R;
import org.animetwincities.animedetour.guests.model.Guest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ViewHolder representing guest information.
 *
 * Intended to be used with {@link GuestsAdapter}
 */
public class GuestViewHolder extends RecyclerView.ViewHolder
{

    @BindView(R.id.guest_list_item_guest_image)
    SimpleDraweeView guestImage;

    @BindView(R.id.guest_list_item_guest_name)
    TextView guestName;

    public GuestViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Guest guest) {
        this.guestName.setText(guest.getName());
        loadImage(guest.getImage());
    }

    public ImageView getGuestImageView() {
        return guestImage;
    }

    /**
     * Loads the guest's image into the viewholder's imageview via Fresco
     *
     * Broke into its own method just in case we need to do any special logic via this later.
     *
     * @param imageUrl
     *          String URL to the Guest's image.
     */
    private void loadImage(String imageUrl) {
        this.guestImage.setImageURI(imageUrl);
    }

}
