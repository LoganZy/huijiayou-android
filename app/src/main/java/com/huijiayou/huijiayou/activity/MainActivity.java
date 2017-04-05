package com.huijiayou.huijiayou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.fragment.HomeFragment;
import com.huijiayou.huijiayou.fragment.OrderFragment;
import com.huijiayou.huijiayou.fragment.UserFragment;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener ,NewHttpRequest.RequestCallback{

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

    @Bind(R.id.view_activityMain_line)
    View view_activityMain_line;

    HomeFragment homeFragment;
    UserFragment userFragment;
    OrderFragment orderFragment;

    FragmentManager fragmentManager;

    public static final int requestCode_login = 0;
    public static final int requestCode_coupon = 1;
    public static final int requestCode_Oil = 2;

    int checkNewMsgTaskId = 1;

    private long exitTime = 0; // 按返回键的间隔
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        homeFragment = new HomeFragment();
        userFragment = new UserFragment();
        orderFragment = new OrderFragment();
        fragmentManager = getSupportFragmentManager();

        rg_activityMain_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                List<Fragment> fragments = fragmentManager.getFragments();
                switch (checkedId){
                    case R.id.rb_activityMain_home:
                        if (fragments != null && fragments.contains(homeFragment)){
                            fragmentManager.beginTransaction().show(homeFragment).hide(orderFragment).hide(userFragment).commit();
                        }else{
                            fragmentManager.beginTransaction().add(R.id.fl_mainActivity_fragmentShell,homeFragment)
                                    .show(homeFragment).hide(orderFragment).hide(userFragment).commit();
                        }
                        if (homeFragment.isAdded()){
                            homeFragment.getCity();
                        }
                        break;
                    case R.id.rb_activityMain_order:
                        if (fragments != null && fragments.contains(orderFragment)){
                            fragmentManager.beginTransaction().show(orderFragment).hide(homeFragment).hide(userFragment).commit();
                        }else{
                            fragmentManager.beginTransaction().add(R.id.fl_mainActivity_fragmentShell,orderFragment)
                                    .show(orderFragment).hide(homeFragment).hide(userFragment).commit();
                        }
                        if(orderFragment.isAdded()){
                            orderFragment.orderFragmentIsLoginOrno();
                        }
//                            fragmentManager.beginTransaction().replace(R.id.fl_mainActivity_fragmentShell,orderFragment).commit();
                        break;
                    case R.id.rb_activityMain_user:
                        if (fragments != null && fragments.contains(userFragment)){
                            fragmentManager.beginTransaction().show(userFragment).hide(orderFragment).hide(homeFragment).commit();
                        }else{
                            fragmentManager.beginTransaction().add(R.id.fl_mainActivity_fragmentShell,userFragment)
                                    .show(userFragment).hide(orderFragment).hide(homeFragment).commit();
                        }
                        if (userFragment.isAdded()){
                            userFragment.userFragmentIsLoginOrNo();
                        }
//                        fragmentManager.beginTransaction().replace(R.id.fl_mainActivity_fragmentShell,userFragment).commit();
                        break;
                }
            }
        });
        rb_activityMain_home.setChecked(true);

        tv_activityMain_cover.setOnClickListener(this);
    }

    //从别的页面跳转到首页 根据传的type值 选择某页
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String type = intent.getStringExtra("type");
        if (HomeFragment.TAG.equals(type)){
            rb_activityMain_home.setChecked(true);
        }else if (UserFragment.TAG.equals(type)){
            rb_activityMain_user.setChecked(true);
        }else if (OrderFragment.TAG.equals(type)){
            rb_activityMain_order.setChecked(true);
        }else{
            rb_activityMain_home.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNewMsg();
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
        view_activityMain_line.setBackgroundColor(getResources().getColor(R.color.transparent_half));
    }

    public void hideCover(){
        tv_activityMain_cover.setVisibility(View.GONE);
        view_activityMain_line.setBackgroundColor(getResources().getColor(R.color.line_color));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_activityMain_cover:
                homeFragment.hideCover();
                break;
        }
    }

    private void checkNewMsg(){
        HashMap<String,Object> hashMap = new HashMap<>();
        String userId = PreferencesUtil.getPreferences(Constans.USER_ID,"");
        if (!TextUtils.isEmpty(userId)){
            hashMap.put(Constans.USER_ID,userId);
            new NewHttpRequest(this, NetConfig.MESSAGE, NetConfig.message_checkNewMsg, "jsonObject", checkNewMsgTaskId, hashMap, true, this).executeTask();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCode_coupon && resultCode == RESULT_OK){
            rg_activityMain_menu.check(R.id.rb_activityMain_home);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        try {
            if (taskId == checkNewMsgTaskId){
                int data = Integer.parseInt(jsonObject.get("data").toString());
                if (data > 0){
                    MyApplication.isNewMessage = true;
//                    if (userFragment.isAdded()){
//                        userFragment.startAnimation();
//                    }
//                    if (homeFragment.isAdded()){
//                        homeFragment.animationDrawable.start();
//                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        if (taskId == checkNewMsgTaskId){
            LogUtil.i("====checkNewMsg::::"+msg.getMessage());
        }
    }

    public void setHomeCheck(){
        rb_activityMain_home.setChecked(true);
    }
}
