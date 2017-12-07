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
            case 0 :
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_food));
                break;
            case 1 :
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_map));
                break;
            case 2:
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_internet));
                break;
            case 3:
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_accomodation));
                break;
            case 4:
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_info));
                break;
        }
    }
}