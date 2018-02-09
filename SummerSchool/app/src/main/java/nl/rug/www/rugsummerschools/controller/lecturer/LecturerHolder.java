package nl.rug.www.rugsummerschools.controller.lecturer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.model.Lecturer;

/**
 * Created by jk on 17. 12. 6.
 */

public abstract class LecturerHolder extends ContentHolder<Lecturer> implements View.OnClickListener {

    private Context mContext;
    private TextView mTitleTextView;
    private ImageView mLecturerImageView;

    public LecturerHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.list_item_lecturer, parent, false));
        mContext = context;
        mTitleTextView = (TextView)itemView.findViewById(R.id.lecturer_item_name_text_view);
        mLecturerImageView = (ImageView)itemView.findViewById(R.id.lecturer_image_view);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(Lecturer lecturer){
        mContent = lecturer;
        mTitleTextView.setText(mContent.getTitle());

        String url = mContent.getImgurl();
        if (!url.equals(""))
            Glide.with(mContext).load(mContent.getImgurl()).into(mLecturerImageView);
    }
}