package com.wanglibao.huijiayou.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.wanglibao.huijiayou.R;
import com.wanglibao.huijiayou.utils.ToastUtils;
import com.wanglibao.huijiayou.widget.MyImageView;
import com.wanglibao.huijiayou.widget.PopuDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lugg on 2017/2/24.
 */

public class UserFragment extends Fragment {


    private ImageButton imbtnFragmetUser;
    private ImageButton imbtnAward;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        MyImageView myImageView = (MyImageView) view.findViewById(R.id.my_image_head);
        myImageView.setImageView((ImageView) view.findViewById(R.id.img_fragmentUser_backgroud));
        imbtnFragmetUser = (ImageButton) view.findViewById(R.id.imgbt_fragmentUser_message);
        imbtnAward = (ImageButton) view.findViewById(R.id.imgBtn_fragmentUser_award);
        imbtnAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ToastUtils.createNormalToast(getActivity(),"哈哈哈哈被点到了");
                PopuDialog popuDialog = new PopuDialog(getActivity());
                popuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                popuDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                popuDialog.show();

            }
        });

           return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
