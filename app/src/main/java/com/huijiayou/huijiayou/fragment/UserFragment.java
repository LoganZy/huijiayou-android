package com.huijiayou.huijiayou.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.CancelActivity;
import com.huijiayou.huijiayou.activity.CouponActivity;
import com.huijiayou.huijiayou.activity.HelpActivity;
import com.huijiayou.huijiayou.activity.InvitationActivity;
import com.huijiayou.huijiayou.activity.LoginActivity;
import com.huijiayou.huijiayou.activity.MessageActivity;
import com.huijiayou.huijiayou.activity.OilActivity;
import com.huijiayou.huijiayou.activity.OilCardActivity;
import com.huijiayou.huijiayou.activity.WelfareActivity;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.widget.MyImageView;
import com.huijiayou.huijiayou.widget.PopuDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by lugg on 2017/2/24.
 */


public class UserFragment extends Fragment {
    public static final String TAG = "UserFragment";

    @Bind(R.id.bt_fragmentUser_login)
    Button btFragmentUserLogin;
    @Bind(R.id.imgBtn_fragmentUser_award)
    ImageButton imgBtnFragmentUserAward;
    @Bind(R.id.imgbt_fragmentUser_message)
    ImageButton imgbtFragmentUserMessage;
    @Bind(R.id.tv_fragmentUser_name)
    TextView tvFragmentName;
    @Bind(R.id.tv_activity_wxbind_oil)
    TextView tvActivityWxbindOil;
    @Bind(R.id.img_fragment_head)
    ImageView imgFragmentHead;

    public  AnimationDrawable animationDrawable;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        MyImageView myImageView = (MyImageView) view.findViewById(R.id.my_image_head);
        myImageView.setImageView((ImageView) view.findViewById(R.id.img_fragmentUser_backgroud));
        ButterKnife.bind(this, view);
        animationDrawable = (AnimationDrawable) imgbtFragmentUserMessage.getBackground();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_popup_huijiayou)
                .showImageForEmptyUri(R.mipmap.ic_popup_huijiayou)
                .build();
       // userFragmentIsLoginOrNo();
        return view;
    }

    public void startAnimation() {
        // 动画
        animationDrawable = (AnimationDrawable) imgbtFragmentUserMessage.getBackground();
        animationDrawable.start();

    }

    public void userFragmentIsLoginOrNo() {
        if (animationDrawable != null && MyApplication.isNewMessage)
            animationDrawable.start();
        if (animationDrawable != null && !MyApplication.isNewMessage)
            animationDrawable.stop();
         if (PreferencesUtil.getPreferences(Constans.ISLOGIN,false)){
            String name = PreferencesUtil.getPreferences(Constans.NICKNAME, "nickname");
            String user_head = PreferencesUtil.getPreferences(Constans.HEADIMGURL, "false");
             String phone = PreferencesUtil.getPreferences("phone","nickname");
            imageLoader.displayImage(user_head, imgFragmentHead,options);
             if(TextUtils.isEmpty(name)||name==null){
                 tvFragmentName.setText(phone);
             }else{
                 tvFragmentName.setText(name);
             }
            tvFragmentName.setVisibility(View.VISIBLE);
            btFragmentUserLogin.setVisibility(View.GONE);
            //请求签到油滴的数量并显示出来

            new NewHttpRequest(getActivity(), NetConfig.ACCOUNT, NetConfig.checkIn, Constans.JSONOBJECT, 2,  true, new NewHttpRequest.RequestCallback() {
                @Override
                public void netWorkError() {
                    LogUtil.i("++++++++++++++++++++++++++++++++++");
                }

                @Override
                public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                    if (taskId == 2) {
                        try {
                            String oil = jsonObject.getString("oildrop_num");
                            //显示油滴
                            showOil(oil);
                            LogUtil.i("++++++++++++++++++"+oil+"++++++++++++++++");
                            //getView().invalidate();
                            //显示可用的油滴数量
                            String id = PreferencesUtil.getPreferences(Constans.USER_ID, "0");
                            HashMap<String, Object> map4 = new HashMap<>();
                            map4.put(Constans.USER_ID, id);
                            new NewHttpRequest(getActivity(), NetConfig.ACCOUNT, NetConfig.UserEnableOil, Constans.JSONOBJECT, 4,map4,true, new NewHttpRequest.RequestCallback() {
                                @Override
                                public void netWorkError() {

                                }

                                @Override
                                public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                                    if (taskId == 4) {
                                        try {
                                            String oil = jsonObject.getString("enableOil");
                                            //显示油滴
                                            // showOil(oil.toString());
                                            tvActivityWxbindOil.setText(oil);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                                @Override
                                public void requestError(int code, MessageEntity msg, int taskId) {
                                }
                            }).executeTask();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void requestError(int code, MessageEntity msg, int taskId) {
                    LogUtil.i("+++++++++++++++++++"+msg.getMessage()+"+++++++++++++++");
                    PreferencesUtil.putPreferences("sigincode",code);
                    // 当应用退出的时候设置其code为0
                    //显示可用的油滴数量
                    String id = PreferencesUtil.getPreferences(Constans.USER_ID, "0");
                    HashMap<String, Object> map4 = new HashMap<>();
                    map4.put(Constans.USER_ID, id);
                    new NewHttpRequest(getActivity(), NetConfig.ACCOUNT, NetConfig.UserEnableOil, Constans.JSONOBJECT, 4,map4,true, new NewHttpRequest.RequestCallback() {
                        @Override
                        public void netWorkError() {

                        }

                        @Override
                        public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                            if (taskId == 4) {
                                try {
                                    String oil = jsonObject.getString("enableOil");
                                    //显示油滴
                                    // showOil(oil.toString());
                                    tvActivityWxbindOil.setText(oil);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        @Override
                        public void requestError(int code, MessageEntity msg, int taskId) {
                        }
                    }).executeTask();


                }
            }).executeTask();



        } else {
            tvActivityWxbindOil.setText("- - -");
            tvFragmentName.setVisibility(View.GONE);
            btFragmentUserLogin.setVisibility(View.VISIBLE);
            imgFragmentHead.setImageResource(R.mipmap.ic_login_default_avatar);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    //判断是否登录
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!this.isHidden()){
            userFragmentIsLoginOrNo();
        }
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    private void showOil(String oil) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        final PopuDialog popuDialog = new PopuDialog(getActivity());
        popuDialog.setMessage(oil);
        popuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popuDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Runnable runner = new Runnable() {
            public void run() {
               popuDialog.dismiss();
            }
        };
        executor.schedule(runner, 3000, TimeUnit.MILLISECONDS);
        popuDialog.show();


    }

    @OnClick({R.id.ll_fragmentUser_oilCard, R.id.ll_fragmentUser_coupon, R.id.ll_fragment_frends, R.id.ll_fragment_helps, R.id.ll_fragment_setting,R.id.bt_fragmentUser_login, R.id.imgBtn_fragmentUser_award, R.id.imgbt_fragmentUser_message,R.id.ll_activity_wxbind_oil})
    public void onClick(View view) {
        if (!PreferencesUtil.getPreferences(Constans.ISLOGIN,false)) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        switch (view.getId()) {
            case R.id.ll_fragmentUser_oilCard:
                startActivity(new Intent(getActivity(), OilCardActivity.class));
                break;
            case R.id.ll_fragmentUser_coupon:
                startActivity(new Intent(getActivity(), CouponActivity.class));
                break;
            case R.id.ll_fragment_frends:
                startActivity(new Intent(getActivity(), InvitationActivity.class));
                break;
            case R.id.ll_fragment_helps:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case R.id.ll_fragment_setting:
                startActivity(new Intent(getActivity(), CancelActivity.class));
                break;
            case R.id.bt_fragmentUser_login:
                //startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.imgBtn_fragmentUser_award:
                Intent intent1 = new Intent(getActivity(),WelfareActivity.class);
                startActivity(intent1);
                break;
            case R.id.imgbt_fragmentUser_message:
                animationDrawable.selectDrawable(0);
                animationDrawable.stop();

                MyApplication.isNewMessage = false;
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.ll_activity_wxbind_oil:
                startActivity(new Intent(getActivity(), OilActivity.class));
                break;
        }
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

}
