package com.wanglibao.huijiayou.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.wanglibao.huijiayou.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/2/24.
 */

public class UserFragment extends Fragment {

    @Bind(R.id.imbtn_fragmet_user)
    ImageButton imbtnFragmetUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    //判断是否登录
    @Override
    public void onStart() {
        super.onStart();

        AnimationDrawable animationDrawable = (AnimationDrawable) imbtnFragmetUser.getBackground();
        animationDrawable.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
