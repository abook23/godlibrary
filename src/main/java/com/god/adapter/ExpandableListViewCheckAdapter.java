package com.god.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.god.R;
import com.god.bean.ExpChildBean;
import com.god.bean.ExpGroupBean;
import com.god.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2016/9/14.
 */
public class ExpandableListViewCheckAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ExpGroupBean> data;
    private ImageLoader imageLoader;
    private int choiceMode = ListView.CHOICE_MODE_NONE;
    private ArrayMap<Integer, SparseBooleanArray> multipleIds;
    private int singleGroupPosition;
    private int singleId;
    private ExpandableListView expandableListView;
    private OnCheckAllListener mListener;
    private boolean groupImage = true, childImage = true;
    private boolean isCheckTouch;

    public ExpandableListViewCheckAdapter(Context context) {
        this.context = context;
        imageLoader = ImageLoader.build(context);
    }

    /**
     * @param listener 全选时回调
     */
    public void setCheckAllListener(OnCheckAllListener listener) {
        this.mListener = listener;
    }

    public void setData(List<ExpGroupBean> data) {
        this.data = data;
    }

    /**
     * 显示 group 第一个图 图片
     *
     * @param b 1
     */
    public void showGroupImage(boolean b) {
        this.groupImage = b;
    }

    /**
     * 显示 Child 第一个图 图片
     *
     * @param b 1
     */
    public void showChildImage(boolean b) {
        this.childImage = b;
    }


    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getChilds() == null ? 0 :
                data.get(groupPosition).getChilds().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getChilds().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        HolderGroup holderGroup;
        if (expandableListView == null) {
            expandableListView = (ExpandableListView) parent;
            choiceMode = expandableListView.getChoiceMode();
            if (multipleIds == null && choiceMode == ListView.CHOICE_MODE_MULTIPLE) {
                multipleIds = new ArrayMap<>();
            }
        }
        if (convertView == null) {
            holderGroup = new HolderGroup();
            View v = View.inflate(context, R.layout.item_exp_group_check_01, null);
            holderGroup.imageView = (ImageView) v.findViewById(R.id.imageView);
            holderGroup.title = (TextView) v.findViewById(R.id.exp_title);
            holderGroup.describe = (TextView) v.findViewById(R.id.describe);
            holderGroup.time = (TextView) v.findViewById(R.id.exp_time);
            holderGroup.count = (TextView) v.findViewById(R.id.exp_count);
            holderGroup.checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            if (choiceMode == ListView.CHOICE_MODE_NONE || choiceMode == ListView.CHOICE_MODE_SINGLE)
                holderGroup.checkBox.setVisibility(View.GONE);
            if (!groupImage)
                holderGroup.imageView.setVisibility(View.GONE);
            v.setTag(holderGroup);
            convertView = v;
        } else {
            holderGroup = (HolderGroup) convertView.getTag();
        }

        ExpGroupBean group = data.get(groupPosition);
        if (group.getImageUrl() instanceof String)
            imageLoader.bindBitmap((String) group.getImageUrl(), holderGroup.imageView, 80, 80);
        if (group.getImageUrl() instanceof Integer)
            holderGroup.imageView.setImageResource((Integer) group.getImageUrl());

        holderGroup.title.setText(group.getTitle());
        holderGroup.describe.setText(group.getDescribe());
        holderGroup.time.setText(group.getTime());
        holderGroup.count.setText(String.valueOf(group.getCount()));

        isCheckTouch = false;
        if (expandableListView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
            holderGroup.checkBox.setChecked(multipleIds.get(groupPosition) != null && multipleIds.get(groupPosition).size() > 0);
            holderGroup.checkBox.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (v instanceof CheckBox) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            isCheckTouch = true;
                        }
                    }
                    return false;
                }
            });
            holderGroup.checkBox.setOnCheckedChangeListener(new OnGroupCheckedChangeListener(convertView, groupPosition));
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        HolderChild holderChild;
        if (convertView == null) {
            holderChild = new HolderChild();
            View v = View.inflate(context, R.layout.item_exp_child_check_01, null);
            holderChild.imageView = (ImageView) v.findViewById(R.id.imageView);
            holderChild.title = (TextView) v.findViewById(R.id.exp_title);
            holderChild.describe = (TextView) v.findViewById(R.id.describe);
            holderChild.time = (TextView) v.findViewById(R.id.exp_time);
            holderChild.checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            holderChild.radioButton = (RadioButton) v.findViewById(R.id.exp_radioButton);
            holderChild.exp_child_linear_layout = (RelativeLayout) v.findViewById(R.id.exp_child_linear_layout);
            if (choiceMode == ListView.CHOICE_MODE_NONE) {
                holderChild.checkBox.setVisibility(View.GONE);
                holderChild.radioButton.setVisibility(View.GONE);
            } else if (choiceMode == ListView.CHOICE_MODE_MULTIPLE) {
                holderChild.checkBox.setVisibility(View.VISIBLE);
                holderChild.radioButton.setVisibility(View.GONE);
            } else if (choiceMode == ListView.CHOICE_MODE_SINGLE) {
                holderChild.checkBox.setVisibility(View.GONE);
                holderChild.radioButton.setVisibility(View.VISIBLE);
            }
            if (!childImage)
                holderChild.imageView.setVisibility(View.GONE);
            v.setTag(holderChild);
            convertView = v;
        } else {
            holderChild = (HolderChild) convertView.getTag();
        }


        ExpChildBean child = data.get(groupPosition).getChilds().get(childPosition);
        if (child.getImageUrl() instanceof String)
            imageLoader.bindBitmap((String) child.getImageUrl(), holderChild.imageView, 80, 80);
        if (child.getImageUrl() instanceof Integer)
            holderChild.imageView.setImageResource((Integer) child.getImageUrl());

//        if (expandableListView.getChoiceMode() != ListView.CHOICE_MODE_NONE)
//            holderChild.exp_child_linear_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    updateChildItem(v, groupPosition, childPosition);
//                }
//            });

        holderChild.title.setText(child.getTitle());
        holderChild.describe.setText(child.getDescribe());
        holderChild.time.setText(child.getTime());
        if (choiceMode == ListView.CHOICE_MODE_MULTIPLE)
            holderChild.checkBox.setChecked(getCheckedItemPositions(groupPosition, childPosition));
        else if (choiceMode == ListView.CHOICE_MODE_SINGLE) {
            if (groupPosition == singleGroupPosition && singleId == childPosition)
                holderChild.radioButton.setChecked(true);
            else
                holderChild.radioButton.setChecked(false);
        } else {
            holderChild.checkBox.setChecked(false);
            holderChild.radioButton.setChecked(false);
        }
        return convertView;
    }

    public void updateChildItem(View view, int groupPosition, int childPosition) {

        HolderChild holderChild = (HolderChild) view.getTag();
        boolean checkBoxState = !holderChild.checkBox.isChecked();
        if (choiceMode == ListView.CHOICE_MODE_SINGLE) {
            singleId = childPosition;
            this.singleGroupPosition = groupPosition;
            int fp = expandableListView.getFirstVisiblePosition();
            int lp = expandableListView.getLastVisiblePosition();
            for (int i = 0; i < lp - fp; i++) {
                View v = expandableListView.getChildAt(i);
                if (v != null) {
                    Object holder = v.getTag();
                    if (holder instanceof HolderChild) {
                        HolderChild _holderChild = (HolderChild) holder;
                        _holderChild.radioButton.setChecked(false);
                    }
                }
            }
            holderChild.radioButton.setChecked(checkBoxState);

        } else if (choiceMode == ListView.CHOICE_MODE_MULTIPLE) {
            putMultipleIds(groupPosition, childPosition, checkBoxState);
            holderChild.checkBox.setChecked(checkBoxState);
        }

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private class OnGroupCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private int groupPosition;
        private View convertView;

        public OnGroupCheckedChangeListener(View convertView, int groupPosition) {
            this.groupPosition = groupPosition;
            this.convertView = convertView;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isCheckTouch)
                checkAll(convertView, groupPosition, isChecked);
        }
    }

    public void checkAll(View convertView, int groupPosition, boolean isChecked) {
        int fp = expandableListView.getFirstVisiblePosition();
        int lp = expandableListView.getLastVisiblePosition();
        int size = getChildrenCount(groupPosition);
        SparseBooleanArray ids = getMultipleIds(groupPosition);
        int selectGroupPosition = -1;//所选择的groupPosition
       // int selectChildPosition = -1;
        for (int i = 0; i <= lp - fp + 1; i++) {//组的第一条 到 size
            View v = expandableListView.getChildAt(i);
            if (v == convertView) {//所选择的groupPosition 在屏幕的位置
                selectGroupPosition = i;
                continue;
            }
            if (selectGroupPosition != -1 && v != null) {//超过屏幕可见就是null
                Object holder = v.getTag();
                if (holder instanceof HolderChild) {
                    HolderChild _holderChild = (HolderChild) holder;
                    if (isChecked) {
                        if (ids.size() > 0) {
                            boolean b = _holderChild.checkBox.isChecked();
                            _holderChild.checkBox.setChecked(!b);
                        } else {
                            _holderChild.checkBox.setChecked(true);
                        }
                    } else {
                        _holderChild.checkBox.setChecked(false);
                    }
                } else if (holder instanceof HolderGroup) {//到第二个 group 退出循环
                        break;
                }
            }
        }
        if (isChecked) {
            if (ids.size() > 0) {
                for (int i = 0; i < size; i++) {
                    boolean b = ids.get(i);
                    ids.put(i, !b);
                }
            } else {
                for (int i = 0; i < size; i++) {
                    ids.put(i, true);
                }
            }
        } else {
            ids = new SparseBooleanArray();
        }
        multipleIds.put(groupPosition, ids);
        if (mListener != null)
            mListener.onCheckAll(isChecked);
    }

    public void putMultipleIds(int groupPosition, int childPosition, boolean checkBoxState) {
        SparseBooleanArray ids = multipleIds.get(groupPosition);
        if (ids == null) {
            ids = new SparseBooleanArray();
        }
        ids.append(childPosition, checkBoxState);
        multipleIds.put(groupPosition, ids);
    }

    public SparseBooleanArray getMultipleIds(int groupPosition) {
        SparseBooleanArray ids = multipleIds.get(groupPosition);
        if (ids == null) {
            ids = new SparseBooleanArray();
            multipleIds.put(groupPosition, ids);
        }
        return ids;
    }

    public boolean getCheckedItemPositions(int groupPosition, int childPosition) {
        SparseBooleanArray ids = multipleIds.get(groupPosition);
        return ids != null && ids.get(childPosition);
    }

    public ArrayMap<Integer, long[]> getIds() {
        ArrayMap<Integer, long[]> arrayMap = new ArrayMap<>();
        if (choiceMode == ListView.CHOICE_MODE_MULTIPLE) {
            for (Map.Entry<Integer, SparseBooleanArray> entry : multipleIds.entrySet()) {
                List<Integer> l = new ArrayList<>();
                SparseBooleanArray ids = entry.getValue();
                for (int i = 0; i < ids.size(); i++) {
                    if (ids.valueAt(i)) {
                        l.add(ids.keyAt(i));
                    }
                }
                long[] _ids = new long[l.size()];
                for (int i = 0; i < l.size(); i++) {
                    _ids[i] = l.get(i);
                }
                arrayMap.put(entry.getKey(), _ids);
            }
        }
        return arrayMap;
    }

    public class HolderGroup {
        ImageView imageView;
        TextView title, describe;
        TextView time, count;
        CheckBox checkBox;
    }

    public class HolderChild {
        ImageView imageView;
        TextView title, describe;
        TextView time;
        CheckBox checkBox;
        RadioButton radioButton;
        RelativeLayout exp_child_linear_layout;
    }

    public interface OnCheckAllListener {
        void onCheckAll(boolean isChecked);
    }

}
