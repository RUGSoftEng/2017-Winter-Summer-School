package nl.rug.www.rugsummerschools.controller.generalinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.model.GeneralInfo;

/**
 * Created by jk on 17. 12. 6.
 */

public abstract class GeneralInfoHolder extends ContentHolder<GeneralInfo> implements View.OnClickListener {

    private static final int CATEGORY_FOOD = 0;
    private static final int CATEGORY_LOCATION = 1;
    private static final int CATEGORY_INTERNET = 2;
    private static final int CATEGORY_ACCOMODATION = 3;
    private static final int CATEGORY_INFO = 4;

    private Context mContext;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private ImageView mImageView;

    public GeneralInfoHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.list_item_generalinfo, parent, false));

        mContext = context;
        mTitleTextView = (TextView)itemView.findViewById(R.id.content_title);
        mContentTextView = (TextView)itemView.findViewById(R.id.content_detail);
        mImageView = (ImageView)itemView.findViewById(R.id.icon_image_view);
        itemView.setOnClickListener(this);
    }

    public void bind(GeneralInfo generalInfo){
        mContent = generalInfo;
        mTitleTextView.setText(mContent.getTitle());
        mTitleTextView.setSelected(true);
        mContentTextView.setText(mContent.getDescription());

        switch (mContent.getCategory()) {
            case CATEGORY_FOOD :
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_food));
                break;
            case CATEGORY_LOCATION :
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_map));
                break;
            case CATEGORY_INTERNET:
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_internet));
                break;
            case CATEGORY_ACCOMODATION:
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_accomodation));
                break;
            case CATEGORY_INFO:
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_info));
                break;
        }
    }
}