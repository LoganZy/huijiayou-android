package com.wanglibao.huijiayou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.activity.PaymentActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/2/24.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{

    @Bind(R.id.tv_fragmentHome_openRegionChoice)
    TextView tv_fragmentHome_openRegionChoice;

    @Bind(R.id.imgBtn_fragmentHome_message)
    ImageButton imgBtn_fragmentHome_message;

    @Bind(R.id.tv_fragmentHome_addGasoline)
    TextView tv_fragmentHome_addGasoline;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        initView();
        return view;
    }

    private void initView() {
        tv_fragmentHome_openRegionChoice.setOnClickListener(this);
        imgBtn_fragmentHome_message.setOnClickListener(this);
        tv_fragmentHome_addGasoline.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_fragmentHome_openRegionChoice:

                break;
            case R.id.imgBtn_fragmentHome_message:

                break;
            case R.id.tv_fragmentHome_addGasoline:
                startActivity(new Intent(getActivity(), PaymentActivity.class));
                break;
        }
    }
}
