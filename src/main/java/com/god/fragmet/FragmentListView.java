package com.god.fragmet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.god.R;
import com.god.adapter.AdapterCheckLin;
import com.god.listener.OnItemViewListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentListView extends Fragment implements OnItemViewListener {

    public ListView listView;
    private AdapterCheckLin adapterCheckLin;
    private List<String> mData;
    private OnFragmentListListener mListener;
    private SparseBooleanArray checkIds = new SparseBooleanArray();
    private int mChoiceMode;

    public FragmentListView() {
        // Required empty public constructor
    }

    public static FragmentListView newInstance(List<String> data, int listViewChoiceMode) {
        FragmentListView fragment = new FragmentListView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.mData = data;
        fragment.mChoiceMode = listViewChoiceMode;
        return fragment;
    }

    public void setData(final List<String> data) {
        mData = data;
    }

    public void setData(String[] data) {
        List<String> list = Arrays.asList(data);
        setData(list);
    }

    public void setListListener(OnFragmentListListener listListener) {
        this.mListener = listListener;
    }

    public void setCheckBox(SparseBooleanArray ids) {
        this.checkIds = ids;
    }

    public void checkAll(boolean b) {
        for (int i = 0; i < adapterCheckLin.getCount(); i++) {
            if (b) {
                checkIds.put(i, !checkIds.get(i));
            } else {
                checkIds.put(i, false);
            }
        }
        adapterCheckLin.setCheckBox(checkIds);
        adapterCheckLin.updateItemView();
    }

    public long[] getCheckedItemIds() {
        SparseBooleanArray ids = adapterCheckLin.getCheckedItemIds();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            if (ids.valueAt(i)) {
                list.add(ids.keyAt(i));
            }
        }
        long[] _ids = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            _ids[i] = list.get(i);
        }
        return _ids;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_view, container, false);
        listView = (ListView) v.findViewById(R.id.listView);
        listView.setChoiceMode(mChoiceMode);
        initView();
        return v;
    }

    private void initView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onCheckedItemIds(new long[]{position});
            }
        });
        adapterCheckLin = new AdapterCheckLin(getContext());
        adapterCheckLin.setData(mData);
        adapterCheckLin.setItemListener(this);
        adapterCheckLin.setCheckBox(checkIds);
        listView.setAdapter(adapterCheckLin);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemViewClick(View parentView, View childView, int position) {
        mListener.onCheckedItemIds(getCheckedItemIds());
    }

    public interface OnFragmentListListener {
        void onCheckedItemIds(long[] ids);
    }
}
