package com.wanglibao.huijiayou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanglibao.huijiayou.R;

import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/2/24.
 */

public class GasFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gas, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
