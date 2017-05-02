package com.god.fragmet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.god.R;
import com.god.adapter.ExpandableListViewCheckAdapter;
import com.god.bean.ExpGroupBean;

import java.util.List;
import java.util.Map;

public class FragmentExpListView extends Fragment implements ExpandableListViewCheckAdapter.OnCheckAllListener {

    private static int MESSAGE_MULTIPLE_CALL_BACK = 0x1;

    private ExpandableListViewCheckAdapter mAdapter;
    private List<ExpGroupBean> mData;

    private OnExpListViewSingleListener singleListener;
    private OnExpListViewMultipleListener multipleListener;
    private boolean isMultipleMsgSend;
    private int choiceMode = ListView.CHOICE_MODE_NONE;
    private boolean groupImage = true, childImage = true;

    public FragmentExpListView() {
        // Required empty public constructor
    }

    public static FragmentExpListView newInstance(List<ExpGroupBean> data) {
        FragmentExpListView fragment = new FragmentExpListView();
        fragment.mData = data;
        return fragment;
    }

    /**
     * ListView.CHOICE_MODE_MULTIPLE    //多选
     * ListView.CHOICE_MODE_SINGLE      //单选
     * ListView.CHOICE_MODE_NONE        //默认
     *
     * @param choiceMode 类型
     */
    public void setChoiceMode(int choiceMode) {
        this.choiceMode = choiceMode;
    }

    /**
     * 显示 group 第一个图 图片
     *
     * @param b
     */
    public void showGroupImage(boolean b) {
        this.groupImage = b;
    }

    /**
     * 显示 Child 第一个图 图片
     *
     * @param b
     */
    public void showChildImage(boolean b) {
        this.childImage = b;
    }


    public void setSingleCheckListener(OnExpListViewSingleListener singleListener) {
        this.singleListener = singleListener;
    }

    public void setMultipleCheckListener(OnExpListViewMultipleListener multipleListener) {
        this.multipleListener = multipleListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_MULTIPLE_CALL_BACK) {
                if (multipleListener != null & isMultipleMsgSend) {
                    multipleListener.onExpListViewMultiple((Map<Integer, long[]>) msg.obj);
                }
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exp_list, container, false);
        ExpandableListView mExpListView = (ExpandableListView) v.findViewById(R.id.exp_listView);
        mExpListView.setGroupIndicator(null);
        mAdapter = new ExpandableListViewCheckAdapter(getContext());
        mAdapter.setData(mData);
        mAdapter.showChildImage(childImage);
        mAdapter.showGroupImage(groupImage);
        mAdapter.setCheckAllListener(this);
        mExpListView.setAdapter(mAdapter);
        mExpListView.setChoiceMode(choiceMode);
        mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (choiceMode == ListView.CHOICE_MODE_SINGLE && singleListener != null) {
                    mAdapter.updateChildItem(v, groupPosition, childPosition);
                    singleListener.onExpListViewSingle(groupPosition, childPosition);
                } else if (choiceMode == ListView.CHOICE_MODE_MULTIPLE) {
                    mAdapter.updateChildItem(v, groupPosition, childPosition);
                    isMultipleMsgSend = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<Integer, long[]> ids = mAdapter.getIds();
                            isMultipleMsgSend = true;
                            handler.obtainMessage(MESSAGE_MULTIPLE_CALL_BACK, ids).sendToTarget();
                        }
                    }).start();
                }

                return false;
            }
        });
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        multipleListener = null;
        singleListener = null;
    }

    @Override
    public void onCheckAll(boolean isChecked) {
        isMultipleMsgSend = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<Integer, long[]> ids = mAdapter.getIds();
                isMultipleMsgSend = true;
                handler.obtainMessage(MESSAGE_MULTIPLE_CALL_BACK, ids).sendToTarget();
            }
        }).start();
    }

    public void getItemIds() {
        mAdapter.getIds();
    }

    public interface OnExpListViewSingleListener {
        void onExpListViewSingle(int groupPosition, int childPosition);
    }

    public interface OnExpListViewMultipleListener {
        void onExpListViewMultiple(Map<Integer, long[]> ids);
    }
}
