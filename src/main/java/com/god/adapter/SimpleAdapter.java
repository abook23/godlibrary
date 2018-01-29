package com.god.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Dimension;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abook23 on 2016/4/6.
 * 请使用 BaseSimpleAdapter
 */
@Deprecated
public class SimpleAdapter extends BaseAdapter {

    private List<? extends Map<String, ?>> mData;
    private String[] mFrom;
    private int[] mTo;
    private LayoutInflater mInflater;
    private int mResource;


    public SimpleAdapter(Context context, @LayoutRes int resource, @IdRes int[] to, String[] from) {
        mResource = resource;
        mFrom = from;
        mTo = to;
        mInflater = LayoutInflater.from(context);
    }

    public SimpleAdapter(Context context, @LayoutRes int resource, @IdRes int[] to, int[] from) {
        mResource = resource;
        mTo = to;
        mInflater = LayoutInflater.from(context);
        mFrom = new String[from.length];
        for (int i = 0; i < from.length; i++) {
            mFrom[i] = String.valueOf(from[i]);
        }
    }

    public SimpleAdapter(Context context, @LayoutRes int resource, @IdRes int[] to, String[] from,
                         List<? extends Map<String, ?>> data) {
        mData = data;
        mResource = resource;
        mFrom = from;
        mTo = to;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<? extends Map<String, ?>> data) {
        mData = data;
    }

    /**
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @see android.widget.Adapter#getView(int, View, ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    private View createViewFromResource(LayoutInflater inflater, int position, View convertView, ViewGroup parent, int resource) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(resource, parent, false);
            bindView(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindData(viewHolder, position, mData);
        return convertView;
    }

    private void bindData(ViewHolder viewHolder, int position, List<? extends Map<String, ?>> mData) {
        Map<String, ?> map = mData.get(position);
        final int count = mTo.length;
        for (int i = 0; i < count; i++) {
            Object v = viewHolder.clientView[i];
            Object data = map.get(mFrom[i]);
            String text = data == null ? "" : data.toString();
            if (text == null) {
                text = "";
            }
            if (v instanceof Checkable) {
                if (data instanceof Boolean) {
                    ((Checkable) v).setChecked((Boolean) data);
                } else if (v instanceof TextView) {
                    // Note: keep the instanceof TextView check at the bottom of these
                    // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                    setViewText((TextView) v, text);
                } else {
                    throw new IllegalStateException(v.getClass().getName() +
                            " should be bound to a Boolean, not a " +
                            (data == null ? "<unknown type>" : data.getClass()));
                }
            } else if (v instanceof TextView) {
                // Note: keep the instanceof TextView check at the bottom of these
                // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                setViewText((TextView) v, text);
            } else if (v instanceof ImageView) {
                if (data instanceof Integer) {
                    setViewImage((ImageView) v, (Integer) data);
                } else {
                    setViewImage((ImageView) v, text);
                }
            } else {
                throw new IllegalStateException(v.getClass().getName() + " is not a " +
                        " view that can be bounds by this SimpleAdapter");
            }
        }
    }

    private void setViewImage(ImageView v, Integer value) {
        if (value == 0)
            return;
        if (value == -1)
            v.setVisibility(View.INVISIBLE);
        else {
            v.setImageResource(value);
            v.setVisibility(View.VISIBLE);
        }

    }

    private void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    private void setViewText(TextView v, String text) {
        v.setText(text);
    }

    private void bindView(ViewHolder viewHolder, View v) {
        final int count = mTo.length;
        viewHolder.clientView = new Object[count];
        for (int i = 0; i < count; i++) {
            final View view = v.findViewById(mTo[i]);
            viewHolder.clientView[i] = view;
        }
    }

    public List<Map<String, ?>> bindData(Object[][] data, String[] key) {
        List<Map<String, ?>> mData = new ArrayList<>();
        final int count = key.length;
        for (Object[] objects : data) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < count; i++) {
                map.put(key[i], objects[i]);
            }
            mData.add(map);
        }
        setData(mData);
        return mData;
    }

    public List<Map<String, ?>> bindData(Object[][] data) {
        List<Map<String, ?>> mData = new ArrayList<>();
        final int count = mFrom.length;
        for (Object[] objects : data) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < count; i++) {
                map.put(mFrom[i], objects[i]);
            }
            mData.add(map);
        }
        setData(mData);
        return mData;
    }

    public class ViewHolder {
        public Object[] clientView;
    }
}
