package org.animetwincities.animedetour.guest;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import org.animetwincities.animedetour.R;

public class GuestItemView extends FrameLayout
{
    final public static PointF IMAGE_FOCUS = new PointF(.5f, .38f);
    /**
     * The avatar for the guest.
     */
    final private SimpleDraweeView image;

    /**
     * The displayed name of the guest.
     */
    final private TextView name;

    public GuestItemView(Context context)
    {
        this(context, null, 0);
    }

    public GuestItemView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public GuestItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.guest_item, this);

        this.image = (SimpleDraweeView) this.findViewById(R.id.guest_item_image);
        this.image.getHierarchy().setActualImageFocusPoint(IMAGE_FOCUS);
        this.name = (TextView) this.findViewById(R.id.guest_item_name);
    }

    /**
     * @param name Name of the guest to be displayed.
     */
    public void setName(String name)
    {
        this.name.setText(name);
    }

    /**
     * @param uri The image to display for the guest.
     */
    public void setImageUri(String uri)
    {
        this.image.setImageURI(Uri.parse(uri));
    }

    public void reset()
    {
        this.image.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.ic_person_black_80dp));
    }

    public View getSharedView()
    {
        return this.image;
    }

}
