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

import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.LoginActivity;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.PreferencesUtil;
import com.huijiayou.huijiayou.widget.MyImageView;
import com.huijiayou.huijiayou.widget.PopuDialog;
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

public class UserFragment extends Fragment implements NewHttpRequest.RequestCallback {


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
    private int status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        MyImageView myImageView = (MyImageView) view.findViewById(R.id.my_image_head);
        myImageView.setImageView((ImageView) view.findViewById(R.id.img_fragmentUser_backgroud));
        ButterKnife.bind(this, view);


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

        AnimationDrawable animationDrawable = (AnimationDrawable) imgbtFragmentUserMessage.getBackground();
        animationDrawable.start();
        HashMap<String, Object> map = new HashMap<>();
        new NewHttpRequest(getActivity(), Constans.URL_wyh + Constans.ACCOUNT, Constans.LOGINSTATUS, Constans.JSONOBJECT, 1, map, this).executeTask();


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


    @OnClick({R.id.bt_fragmentUser_login, R.id.imgBtn_fragmentUser_award, R.id.imgbt_fragmentUser_message})
    public void onClick(View view) {
        if (status == 0) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        switch (view.getId()) {
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
                break;
        }
    }

    @Override
    public void netWorkError() {

    }

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        switch (taskId) {
            case 1:
                try {
                    status = jsonObject.getInt("status");
                    if (status == 0) {
                        tvFragmentName.setVisibility(View.GONE);
                        btFragmentUserLogin.setVisibility(View.VISIBLE);
                    } else if (status == 1) {
                        String name = PreferencesUtil.getPreferences(Constans.NICKNAME,"nickname");
                        String user_head = PreferencesUtil.getPreferences(Constans.HEADIMGURL,"false");
                        if(TextUtils.equals(user_head,"false")){
                           imgFragmentHead.setImageResource(R.mipmap.ic_login_default_avatar);
                        }else {
                            ImageLoader.getInstance().loadImage(user_head, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    imgFragmentHead.setImageBitmap(loadedImage);
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {

                                }
                            });
                        }
                        tvFragmentName.setText(name);
                        tvFragmentName.setVisibility(View.VISIBLE);
                        btFragmentUserLogin.setVisibility(View.GONE);
                        HashMap<String, Object> map = new HashMap<>();
                        //请求签到油滴的数量并显示出来
                        new NewHttpRequest(getActivity(), Constans.URL_wyh + Constans.ACCOUNT, Constans.CHECKIN, Constans.JSONOBJECT, 2, map, true, new NewHttpRequest.RequestCallback() {
                            @Override
                            public void netWorkError() {

                            }

                            @Override
                            public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                                if (taskId == 2) {
                                    try {
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                        String oil = jsonObject1.getString("oildrop_num");
                                        //显示油滴
                                        showOil(oil);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            @Override
                            public void requestError(int code, MessageEntity msg, int taskId) {

                            }
                        });

                        //显示可用的油滴数量
                        String id = PreferencesUtil.getPreferences("id", "-1");
                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("user_id", id);
                        new NewHttpRequest(getActivity(), Constans.URL_wyh + Constans.ACCOUNT, Constans.USERENABLEOIL, Constans.JSONOBJECT, 3, map1, true, new NewHttpRequest.RequestCallback() {
                            @Override
                            public void netWorkError() {

                            }

                            @Override
                            public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
                                if (taskId == 3) {
                                    try {
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                        String oilNum = jsonObject1.getString("enableOil");
                                        tvActivityWxbindOil.setText(oilNum);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void requestError(int code, MessageEntity msg, int taskId) {

                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    private void showOil(String oil) {
        PopuDialog popuDialog = new PopuDialog(getActivity());
        popuDialog.setMessage(oil);
        popuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popuDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        popuDialog.show();

    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {

    }
}
