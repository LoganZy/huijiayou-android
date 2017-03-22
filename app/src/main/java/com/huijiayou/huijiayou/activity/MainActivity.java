package com.huijiayou.huijiayou.activity;

import android.content.Intent;
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
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.fragment.HomeFragment;
import com.huijiayou.huijiayou.fragment.OrderFragment;
import com.huijiayou.huijiayou.fragment.UserFragment;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

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

    HomeFragment homeFragment;
    UserFragment userFragment;
    OrderFragment OrderFragment;

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

    //从别的页面跳转到首页 根据传的type值 选择某页
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String type = intent.getStringExtra("type");
        if (HomeFragment.TAG.equals(type)){
            rg_activityMain_menu.check(R.id.rb_activityMain_home);
        }else if (UserFragment.TAG.equals(type)){
            rg_activityMain_menu.check(R.id.rb_activityMain_user);
        }else if (OrderFragment.TAG.equals(type)){
            rg_activityMain_menu.check(R.id.rb_activityMain_order);
        }else{
            rg_activityMain_menu.check(R.id.rb_activityMain_home);
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

    private void checkNewMsg(){
        HashMap<String,Object> hashMap = new HashMap<>();
        String userId = PreferencesUtil.getPreferences("user_id","");
        hashMap.put("user_id",userId);
        new NewHttpRequest(this, Constans.URL_MESSAGE, Constans.message_checkNewMsg, "jsonObject", checkNewMsgTaskId, hashMap, true, this).executeTask();
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
        if (taskId == checkNewMsgTaskId){

        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        if (taskId == checkNewMsgTaskId){
            ToastUtils.createNormalToast(this,msg.getMessage());
        }
    }
}
