package nl.rug.www.rugsummerschools.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import nl.rug.www.rugsummerschools.model.Content;

/**
 * Created by jk on 17. 12. 6.
 */

public abstract class ContentAdapter<T extends Content, K extends ContentHolder<T>> extends RecyclerView.Adapter<K> {

    private Context mContext;
    private List<T> mContents;
//    TODO: where to get thumbnaildownloader
//    private ThumbnailDownloader mThumbnailDownloader;
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
//        TODO: url is wrong so that it needs to be changed
//        mThumbnailDownloader.queueThumbnail(holder, "url");
        holder.bind(content);
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }
}