package nl.rug.www.rugsummerschools.controller.generalinfo;

import android.content.Intent;
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

    private TextView mTitleTextView;
    private ImageView mImageView;
    protected abstract String[] getStrings();
    protected abstract HashMap<String, Integer> getPicHashMap();

    public GeneralInfoHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_generalinfo, parent, false));

        mTitleTextView = (TextView)itemView.findViewById(R.id.content_title);
        mImageView = (ImageView)itemView.findViewById(R.id.icon_image_view);
        itemView.setOnClickListener(this);
    }

    public void bind(GeneralInfo generalInfo){
        mContent = generalInfo;
        mTitleTextView.setText(mContent.getTitle());
        mTitleTextView.setSelected(true);
        mImageView.setImageResource(selectPicture(mContent.getTitle().toLowerCase()));
    }

    private int selectPicture(String title) {
        String[] strings = getStrings();
        for (String s : strings) {
            if (title.contains(s))
                return getPicHashMap().get(s);
        }

        int idx = Math.abs(title.hashCode() % 4);

        switch (idx) {
            case 0:
                return R.mipmap.ic_gen_info_smile;
            case 1:
                return R.mipmap.ic_gen_info_smile;
            case 2:
                return R.mipmap.ic_gen_info_star;
            case 3:
                return R.mipmap.ic_gen_info_arrowup;
        }
        return 0;
    }
}