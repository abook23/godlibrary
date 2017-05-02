package com.god.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.god.R;
import com.god.adapter.AdapterCheckLin;
import com.god.fragmet.FragmentListView;
import com.god.widget.CheckLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.god.R.id.listView;

public class DialogFragmentList extends DialogFragment implements FragmentListView.OnFragmentListListener {

    private CheckBox checkBoxAll;
    private List<String> mData;
    private CheckMode mCheckMode = CheckMode.CHOICE_MODE_NONE;
    private boolean isCheckAll = true;
    private OnDialogFragmentListListener mListener;
    private SparseBooleanArray checkIds = new SparseBooleanArray();
    private Button buttonOk;
    private int listViewChoiceMode;
    private FragmentListView fragmentListView;

    public DialogFragmentList() {
        // Required empty public constructor
    }

    public static DialogFragmentList newInstance() {
        DialogFragmentList fragment = new DialogFragmentList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public void setData(final List<String> data) {
        mData = data;
    }

    public void setData(String[] data) {
        List<String> list = Arrays.asList(data);
        setData(list);
    }

    public void setCheckBox(SparseBooleanArray ids) {
        this.checkIds = ids;
    }

    public long[] getCheckedItemIds() {
        return fragmentListView.getCheckedItemIds();
    }

    public void setCheckMode(CheckMode checkMode) {
        mCheckMode = checkMode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_list, container, false);
        checkBoxAll = (CheckBox) v.findViewById(R.id.checkBox_all);
        checkBoxAll.setVisibility(View.GONE);
        buttonOk = (Button) v.findViewById(R.id.but_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCheckedIds(getCheckedItemIds());
                dismiss();
            }
        });
        initView();
        return v;
    }

    private void initView() {
        if (mCheckMode == CheckMode.CHOICE_MODE_MULTIPLE) {
            listViewChoiceMode = ListView.CHOICE_MODE_MULTIPLE;
            checkBoxAll.setVisibility(View.VISIBLE);
            checkBoxAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentListView.checkAll(isCheckAll);
                    isCheckAll = !isCheckAll;
                }
            });
        } else if (mCheckMode == CheckMode.CHOICE_MODE_SINGLE) {
            listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
            checkBoxAll.setVisibility(View.GONE);
        } else if (mCheckMode == CheckMode.CHOICE_MODE_NONE) {
            listViewChoiceMode = ListView.CHOICE_MODE_NONE;
            checkBoxAll.setVisibility(View.GONE);
            buttonOk.setVisibility(View.GONE);
        }

        fragmentListView = FragmentListView.newInstance(mData, listViewChoiceMode);
        fragmentListView.setListListener(this);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.gb_df_fl, fragmentListView);
        ft.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogFragmentListListener) {
            mListener = (OnDialogFragmentListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDialogFragmentListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCheckedItemIds(long[] ids) {

        if (mCheckMode == CheckMode.CHOICE_MODE_NONE)
            mListener.onCheckedItemIds(ids);
    }

    public enum CheckMode {
        CHOICE_MODE_NONE, CHOICE_MODE_MULTIPLE, CHOICE_MODE_SINGLE
    }

    public interface OnDialogFragmentListListener {
        void onCheckedItemIds(long[] ids);

        void onCheckedIds(long[] ids);
    }
}
