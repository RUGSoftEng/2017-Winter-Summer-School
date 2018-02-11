package nl.rug.www.rugsummerschools.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import nl.rug.www.rugsummerschools.model.Content;

/**
 * This class is general contents adapter for recycler view of contents for main activity.
 *
 * @since 06/12/2017
 * @author Jeongkyun Oh
 * @version 2.0.0
 */

public abstract class ContentAdapter<T extends Content, K extends ContentHolder<T>> extends RecyclerView.Adapter<K> {

    private Context mContext;
    private List<T> mContents;
    protected abstract K createHolder(LayoutInflater layoutInflater, ViewGroup parent);

    public ContentAdapter(List<T> contents, Context context) {
        mContext = context;
        mContents = contents;
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return createHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        T content = mContents.get(position);
        holder.bind(content);
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }
}