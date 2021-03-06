package nl.rug.www.rugsummerschools.controller.generalinfo;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.model.GeneralInfo;

/**
 * ViewHolder class for general information recycler view.
 * Its role is to bind a model to the view
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 06/12/17
 */

public abstract class GeneralInfoHolder extends ContentHolder<GeneralInfo> implements View.OnClickListener {

    private Context mContext;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private ImageView mImageView;

    public GeneralInfoHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.list_item_generalinfo, parent, false));

        mContext = context;
        mTitleTextView = itemView.findViewById(R.id.content_title);
        mContentTextView = itemView.findViewById(R.id.content_detail);
        mImageView = itemView.findViewById(R.id.icon_image_view);
        itemView.setOnClickListener(this);
    }

    public void bind(GeneralInfo generalInfo) {
        mContent = generalInfo;
        mTitleTextView.setText(mContent.getTitle());
        mTitleTextView.setSelected(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mContentTextView.setText(Html.fromHtml(mContent.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mContentTextView.setText(Html.fromHtml(mContent.getDescription()));
        }

        String category = mContent.getCategory();
        if ("Food".equals(category)) {
            Glide.with(mContext).load(R.drawable.bg_food).into(mImageView);
        } else if ("Location".equals(category)) {
            Glide.with(mContext).load(R.drawable.bg_map).into(mImageView);
        } else if ("Internet".equals(category)) {
            Glide.with(mContext).load(R.drawable.bg_internet).into(mImageView);
        } else if ("Accommodation".equals(category)) {
            Glide.with(mContext).load(R.drawable.bg_accomodation).into(mImageView);
        } else {
            Glide.with(mContext).load(R.drawable.bg_info).into(mImageView);
        }
    }
}