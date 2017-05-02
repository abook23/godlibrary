package com.god.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.god.R;
import com.god.bean.ExpGroupBean;
import com.god.fragmet.FragmentExpListView;

import java.util.List;


public class DialogFragmentExpList extends DialogFragment {

    private List<ExpGroupBean> mData;

    private FragmentExpListView.OnExpListViewSingleListener singleListener;
    private FragmentExpListView.OnExpListViewMultipleListener multipleListener;
    private int choiceMode = ListView.CHOICE_MODE_NONE;
    private boolean groupImage = true, childImage = true;

    public DialogFragmentExpList() {
        // Required empty public constructor
    }

    public static DialogFragmentExpList newInstance(List<ExpGroupBean> data) {
        DialogFragmentExpList fragment = new DialogFragmentExpList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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


    public void setSingleCheckListener(FragmentExpListView.OnExpListViewSingleListener singleListener) {
        this.singleListener = singleListener;
    }

    public void setMultipleCheckListener(FragmentExpListView.OnExpListViewMultipleListener multipleListener) {
        this.multipleListener = multipleListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // mParam1 = getArguments().getString(ARG_PARAM1);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.dialog_fragment_exp_list, container, false);

        FragmentExpListView fragmentExpListView = FragmentExpListView.newInstance(mData);
        fragmentExpListView.setChoiceMode(choiceMode);
        fragmentExpListView.setMultipleCheckListener(multipleListener);
        fragmentExpListView.setSingleCheckListener(singleListener);
        fragmentExpListView.showChildImage(childImage);
        fragmentExpListView.showGroupImage(groupImage);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.fragment_exp_list, fragmentExpListView);
        ft.commit();

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        multipleListener = null;
        singleListener = null;
    }

}
