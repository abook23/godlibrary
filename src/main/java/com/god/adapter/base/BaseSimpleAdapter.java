
package com.god.adapter.base;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * listView adapter 2016年8月5日15:23:59
 *
 * @param <T>
 */
public abstract class BaseSimpleAdapter<T> extends BaseAdapter {
    private List<T> mData;
    private int mResource;

    public BaseSimpleAdapter(@LayoutRes int resource, List<T> data) {
        this.mResource = resource;
        this.mData = data;
    }

    private View createViewFromResource(int position, View view, ViewGroup viewGroup, int resource) {
        BaseViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
            viewHolder = new BaseViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (BaseViewHolder) view.getTag();
        }

        this.convert(viewHolder, position, this.mData.get(position));
        return view;
    }

    public void addData(int var1, T var2) {
        this.mData.add(var1, var2);
        this.notifyDataSetChanged();
    }

    public void addData(int index, List<T> data) {
        this.mData.addAll(index, data);
    }

    public void addData(T t) {
        this.mData.add(t);
    }

    public void addData(List<T> data) {
        this.mData.addAll(data);
    }

    public abstract void convert(BaseViewHolder holder, int position, T t);

    public int getCount() {
        return this.mData == null ? 0 : this.mData.size();
    }

    public List<T> getData() {
        return this.mData;
    }

    public T getItem(int index) {
        return this.mData.get(index);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View v, ViewGroup viewGroup) {
        return this.createViewFromResource(position, v, viewGroup, this.mResource);
    }

    public void remove(int position) {
        this.mData.remove(position);
    }

    public void setData(int i, T t) {
        this.mData.set(i, t);
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    public void setNewData(List<T> newData) {
        this.mData = newData;
        this.notifyDataSetChanged();
    }
}
