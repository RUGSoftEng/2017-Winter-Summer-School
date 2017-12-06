package nl.rug.www.rugsummerschools.controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import nl.rug.www.rugsummerschools.model.Content;

/**
 * Created by jk on 17. 12. 6.
 */

public abstract class ContentHolder<T extends Content> extends RecyclerView.ViewHolder {

    protected T mContent;

    public ContentHolder(View itemView) {
        super(itemView);
    }

    abstract public void bind(T content);
}
