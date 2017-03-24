package com.huijiayou.huijiayou.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.huijiayou.huijiayou.activity.LoginActivity;
import com.huijiayou.huijiayou.activity.MessageActivity;
import com.huijiayou.huijiayou.activity.OilActivity;
import com.huijiayou.huijiayou.activity.OilCardActivity;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.widget.MyImageView;
import com.huijiayou.huijiayou.widget.PopuDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
    private int statusIsLogin;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        MyImageView myImageView = (MyImageView) view.findViewById(R.id.my_image_head);
        myImageView.setImageView((ImageView) view.findViewById(R.id.img_fragmentUser_backgroud));
        ButterKnife.bind(this, view);
        startAnimation();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_login_default_avatar)
                .showImageForEmptyUri(R.mipmap.ic_login_default_avatar)
                .build();
        return view;
    }

    public void startAnimation() {
        // 动画
        animationDrawable = (AnimationDrawable) imgbtFragmentUserMessage.getBackground();
        animationDrawable.start();

    }

    private void isLoginOrNo() {
        new NewHttpRequest(getActivity(), Constans.URL_wyh + Constans.ACCOUNT, Constans.LOGINSTATUS, Constans.JSONOBJECT, 1,true, new NewHttpRequest.RequestCallback() {
            @Override
            public void netWorkError() {

            }

            @Override
            public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                try {
                    statusIsLogin = jsonObject.getInt("status");
                    //判断是否登录
                    if (statusIsLogin ==1){
                        String name = PreferencesUtil.getPreferences(Constans.NICKNAME, "nickname");
                        String user_head = PreferencesUtil.getPreferences(Constans.HEADIMGURL, "false");
                        ImageLoader.getInstance().displayImage(user_head, imgFragmentHead,options);
                        if (TextUtils.isEmpty(name)||name==null){
                            name = "nickname";
                        }
                        tvFragmentName.setText(name);
                        tvFragmentName.setVisibility(View.VISIBLE);
                        btFragmentUserLogin.setVisibility(View.GONE);
                        //请求签到油滴的数量并显示出来
                        //当签到的code是已经签到状态的时候
                        if(PreferencesUtil.getPreferences("sigincode",0)!=1310){
                            new NewHttpRequest(getActivity(), Constans.URL_wyh + Constans.ACCOUNT, Constans.checkIn, Constans.JSONOBJECT, 2,  true, new NewHttpRequest.RequestCallback() {
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
                                }
                            }).executeTask();
                        }

                        //显示可用的油滴数量
                        String id = PreferencesUtil.getPreferences(Constans.USER_ID, "0");
                        HashMap<String, Object> map4 = new HashMap<>();
                        map4.put(Constans.USER_ID, id);
                        new NewHttpRequest(getActivity(), Constans.URL_wyh + Constans.ACCOUNT, Constans.UserEnableOil, Constans.JSONOBJECT, 4,map4,true, new NewHttpRequest.RequestCallback() {
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

                    } else {
                        tvFragmentName.setVisibility(View.GONE);
                        btFragmentUserLogin.setVisibility(View.VISIBLE);
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(int code, MessageEntity msg, int taskId) {

            }
        }).executeTask();

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    //判断是否登录
    @Override
    public void onStart() {
        super.onStart();
        isLoginOrNo();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void showOil(String oil) {
        PopuDialog popuDialog = new PopuDialog(getActivity());
        popuDialog.setMessage(oil);
        popuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popuDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        popuDialog.show();

    }

    @OnClick({R.id.ll_fragmentUser_oilCard, R.id.ll_fragmentUser_coupon, R.id.ll_fragment_frends, R.id.ll_fragment_helps, R.id.ll_fragment_setting,R.id.bt_fragmentUser_login, R.id.imgBtn_fragmentUser_award, R.id.imgbt_fragmentUser_message})
    public void onClick(View view) {
        if (statusIsLogin ==0) {
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
                break;
            case R.id.ll_fragment_helps:

                //伪代码测试接口  可去掉
                String id = PreferencesUtil.getPreferences("id", "0");
                LogUtil.i("++++++++++++++++++"+id+"++++++++++++++++");
                HashMap<String, Object> map4 = new HashMap<>();
                map4.put("user_id", id);
                new NewHttpRequest(getActivity(), Constans.URL_wyh + Constans.ACCOUNT, Constans.UserEnableOil, Constans.JSONOBJECT, 4,map4,true, new NewHttpRequest.RequestCallback() {
                    @Override
                    public void netWorkError() {

                    }

                    @Override
                    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                        if (taskId == 4) {
                            try {
                              Object oil = jsonObject.getString("enableOil");
                                //显示油滴
                               // showOil(oil.toString());
                                LogUtil.i("++++++++++++++++++"+oil+"++++++++++++++++");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    @Override
                    public void requestError(int code, MessageEntity msg, int taskId) {
                        LogUtil.i("+++++++++++++++++++"+msg.getMessage()+"+++++++++++++++");
                    }
                }).executeTask();
                break;
            case R.id.ll_fragment_setting:
                startActivity(new Intent(getActivity(), CancelActivity.class));
                //伪代码测试接口 可去掉
                new NewHttpRequest(getActivity(), Constans.URL_wyh + Constans.ACCOUNT, Constans.CHECKIN, Constans.JSONOBJECT, 2,  true, new NewHttpRequest.RequestCallback() {
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void requestError(int code, MessageEntity msg, int taskId) {
                        LogUtil.i("+++++++++++++++++++"+msg.getMessage()+"+++++++++++++++");
                    }
                }).executeTask();

                break;
            case R.id.bt_fragmentUser_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.imgBtn_fragmentUser_award:
                PopuDialog popuDialog = new PopuDialog(getActivity());
                popuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                popuDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                popuDialog.show();
                break;
            case R.id.imgbt_fragmentUser_message:
                animationDrawable.stop();
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case tv_activity_wxbind_oil:
                startActivity(new Intent(getActivity(), OilActivity.class));
                break;
        }
    }




}
