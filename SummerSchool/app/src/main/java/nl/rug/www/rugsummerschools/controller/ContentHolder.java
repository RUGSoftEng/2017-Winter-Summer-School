package nl.rug.www.rugsummerschools.controller;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import nl.rug.www.rugsummerschools.model.Content;

/**
 * This class is general contents view holder for recycler view of contents for main activity.
 *
 * @since 06/12/2017
 * @author Jeongkyun Oh
 * @version 2.0.0
 */

public abstract class ContentHolder<T extends Content> extends RecyclerView.ViewHolder {

    protected T mContent;

    public ContentHolder(View itemView) {
        super(itemView);
    }

    abstract public void bind(T content);
}
