package com.huijiayou.huijiayou.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.fragment.OrderFragment;
import com.huijiayou.huijiayou.fragment.HomeFragment;
import com.huijiayou.huijiayou.fragment.UserFragment;
import com.huijiayou.huijiayou.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.fl_mainActivity_fragmentShell)
    FrameLayout fl_mainActivity_fragmentShell;

    @Bind(R.id.rg_activityMain_menu)
    RadioGroup rg_activityMain_menu;

    @Bind(R.id.rb_activityMain_home)
    RadioButton rb_activityMain_home;

    @Bind(R.id.rb_activityMain_order)
    RadioButton rb_activityMain_order;

    @Bind(R.id.rb_activityMain_user)
    RadioButton rb_activityMain_user;

    @Bind(R.id.tv_activityMain_cover)
    TextView tv_activityMain_cover;

    HomeFragment homeFragment;
    UserFragment userFragment;
    OrderFragment OrderFragment;

    FragmentManager fragmentManager;

    private static final int requestCode_login = 0;

    private long exitTime = 0; // 按返回键的间隔
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        homeFragment = new HomeFragment();
        userFragment = new UserFragment();
        OrderFragment = new OrderFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_mainActivity_fragmentShell,homeFragment)
                .add(R.id.fl_mainActivity_fragmentShell,OrderFragment)
                .add(R.id.fl_mainActivity_fragmentShell,userFragment)
                .commit();

        rg_activityMain_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_activityMain_home:
                        fragmentManager.beginTransaction().show(homeFragment).hide(userFragment).hide(OrderFragment).commit();
                        break;
                    case R.id.rb_activityMain_order:
                        fragmentManager.beginTransaction().show(OrderFragment).hide(userFragment).hide(homeFragment).commit();
                        break;
                    case R.id.rb_activityMain_user:
                        fragmentManager.beginTransaction().show(userFragment).hide(homeFragment).hide(OrderFragment).commit();
                        break;
                }
            }
        });
        rg_activityMain_menu.check(R.id.rb_activityMain_home);

        tv_activityMain_cover.setOnClickListener(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.createNormalToast(this, "再按一次退出应用");
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                myApplication.exit();
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void showCover(){
        tv_activityMain_cover.setVisibility(View.VISIBLE);
    }

    public void hideCover(){
        tv_activityMain_cover.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_activityMain_cover:
                homeFragment.hideCover();
                break;
        }
    }
}
