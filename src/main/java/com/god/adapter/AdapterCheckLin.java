package com.god.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.god.R;
import com.god.dialog.DialogFragmentList;
import com.god.listener.OnItemViewListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by abook23 on 2015/7/15.
 */
public class AdapterCheckLin extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> list;
    private boolean isCheckBox;
    private SparseBooleanArray ids;
    private ListView listView;
    private OnItemViewListener itemListener;

    public AdapterCheckLin(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<String> list) {
        this.list = list;
    }

    public void clearList() {
        this.list.clear();
    }

    /**
     * @param ids position , Boolean 是否选择
     */
    public void setCheckBox(SparseBooleanArray ids) {
        if (ids == null) {
            ids = new SparseBooleanArray();
        }
        this.ids = ids;
    }

    public void setItemListener(OnItemViewListener listener) {
        this.itemListener = listener;
    }


    public SparseBooleanArray getCheckedItemIds() {
        return ids;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (listView == null) {
            listView = (ListView) parent;
            if (listView.getChoiceMode() != ListView.CHOICE_MODE_NONE)
                isCheckBox = true;
        }
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_check_lin_02, parent, false);
            holder.tv1 = (TextView) convertView.findViewById(R.id.textView);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.gb_df_ll);
            holder.checkBox.setVisibility(View.GONE);
            holder.radioButton.setVisibility(View.GONE);
            if (isCheckBox) {
                if (listView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE)
                    holder.checkBox.setVisibility(View.VISIBLE);
                else if (listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE)
                    holder.radioButton.setVisibility(View.VISIBLE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv1.setText(list.get(position));
        if (isCheckBox) {
            holder.checkBox.setChecked(ids.get(position));
            holder.radioButton.setChecked(ids.get(position));
        }
        holder.linearLayout.setOnClickListener(new LLOnClickListener(position));
        return convertView;
    }

    public void updateItemView() {
        int fp = listView.getFirstVisiblePosition();
        int lp = listView.getLastVisiblePosition();
        for (int i = 0; i <= lp - fp; i++) {
            View view = listView.getChildAt(i);
            Object o = view.getTag();
            if (o instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) o;
                holder.radioButton.setChecked(ids.get(i + fp));
                holder.checkBox.setChecked(ids.get(i + fp));
            }
        }
    }

    public void checkLinearLayout(int position) {
        if (listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE)
            ids.clear();
        boolean check = ids.get(position);
        ids.put(position, !check);
        updateItemView();
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    public class ViewHolder {
        public TextView tv1;
        public CheckBox checkBox;
        public RadioButton radioButton;
        public LinearLayout linearLayout;
    }

    private class LLOnClickListener implements View.OnClickListener {
        int position;

        public LLOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            checkLinearLayout(position);
            if (itemListener != null)
                itemListener.onItemViewClick(null, v, position);
        }
    }
}
