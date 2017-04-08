package com.huijiayou.huijiayou.net;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.hsg.sdk.common.util.ConnectionUtil;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.activity.LoginActivity;
import com.huijiayou.huijiayou.config.Constans;
import com.huijiayou.huijiayou.jsonrpc.lib.JSONRPCClient;
import com.huijiayou.huijiayou.jsonrpc.lib.JSONRPCException;
import com.huijiayou.huijiayou.jsonrpc.lib.JSONRPCParams;
import com.huijiayou.huijiayou.utils.DialogLoading;
import com.huijiayou.huijiayou.utils.GsonUtil;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.PreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 在之前的HttpRequest上再次封装，方便调用 by:baozi
 */

public class NewHttpRequest implements Runnable {

    private static final String TAG = NewHttpRequest.class.getSimpleName();
    private String mUrl;
    private Object[] mObjects;
    private Handler mHandler;
    private String type;
    private String mMethod;
    private int taskId;
    private RequestCallback requestCallback;
    private WeakReference<Activity> mactivityWeakReference;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private boolean isShowLoad = true;
    DialogLoading dialogLoading;

    public interface RequestCallback {

        /**
         * 网络连接失败
         */
        void netWorkError();

        /**
         * 成功返回
         *
         * @param jsonObject 返回msg
         */
        void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId);

        /**
         * 成功返回
         *
         * @param code 错误代码
         * @param msg  返回msg
         */
        void requestError(int code, MessageEntity msg, int taskId);

    }


    /**
     * 显示dialog  带参数的构造方法
     *
     * @param activity
     * @param url
     * @param method
     * @param type
     * @param taskid
     * @param params
     * @param requestCallback
     */
    public NewHttpRequest(Activity activity, String url, String method, String type, int taskid,
                          HashMap<String, Object> params, RequestCallback requestCallback) {
        this.mUrl = url;
        this.mMethod = method;
        this.mHandler = new MyNetHandle(activity);
        this.type = type;
        this.taskId = taskid;
        this.requestCallback = requestCallback;
        getParams(ParamUtil.getJSONObjectParams(params));

        if (activity != null) {
            mactivityWeakReference = new WeakReference<Activity>(activity);
        }

        LogUtil.e("请求地址url------->" + url);
    }

    /**
     * 不显示加载dialog 带参数的构造方法
     *
     * @param activity
     * @param url
     * @param method
     * @param type
     * @param taskid
     * @param params
     * @param isShowLoad
     * @param requestCallback
     */
    public NewHttpRequest(Activity activity, String url, String method, String type, int taskid,
                          HashMap<String, Object> params, boolean isShowLoad, RequestCallback requestCallback) {
        this.mUrl = url;
        this.mMethod = method;
        this.mHandler = new MyNetHandle(activity);
        this.type = type;
        this.taskId = taskid;
        this.requestCallback = requestCallback;
        this.isShowLoad = isShowLoad;
        getParams(ParamUtil.getJSONObjectParams(params));

        if (activity != null) {
            mactivityWeakReference = new WeakReference<>(activity);
        }
        LogUtil.e("请求地址url------->" + url);
    }

    /**
     * 无参数 不显示dialog 构造方法
     *
     * @param activity
     * @param url      请求地址
     * @param method   jsonrpc后台调用方法名
     * @param type     区分是jsonObject或jsonArray
     * @param taskid   对应的任务id
     * @param params   可变参数 无参数传null
     */
    public NewHttpRequest(Activity activity, String url, String method, String type, int taskid, boolean isShowLoad,
                          RequestCallback requestCallback, Object... params) {

        this.mUrl = url;
        this.mMethod = method;
        this.mHandler = new MyNetHandle(activity);
        this.type = type;
        this.requestCallback = requestCallback;
        this.taskId = taskid;
        this.mObjects = params;
        this.isShowLoad = isShowLoad;

        if (activity != null) {
            mactivityWeakReference = new WeakReference<>(activity);
        }

        if (null != params && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                //LogUtil.e("params--->" + params[i]);
            }
        }
        LogUtil.e("请求地址url------->" + url);
    }


    /**
     * @param activity
     * @param url      请求地址
     * @param method   jsonrpc后台调用方法名
     * @param type     区分是jsonObject或jsonArray
     * @param taskid   对应的任务id
     * @param params   可变参数 无参数传null
     */
    public NewHttpRequest(Activity activity, String url, String method, String type, int taskid,
                          RequestCallback requestCallback, Object... params) {

        this.mUrl = url;
        this.mMethod = method;
        this.mHandler = new MyNetHandle(activity);
        this.type = type;
        this.requestCallback = requestCallback;
        this.taskId = taskid;
        this.mObjects = params;

        if (activity != null) {
            mactivityWeakReference = new WeakReference<>(activity);
        }

        if (null != params && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                //LogUtil.e("params--->" + params[i]);
            }
        }
        LogUtil.e("请求地址url------->" + url);
    }

    private void getParams(Object... params) {
        this.mObjects = params;
    }


    @Override
    public void run() {

        JSONRPCClient client = JSONRPCClient.create(mUrl, JSONRPCParams.Versions.VERSION_2);
        client.setDebug(Constans.DEBUG);
        client.setConnectionTimeout(1000 * 60);
        client.setSoTimeout(1000 * 60);

        //LogUtil.e("taskid-------->" + taskId);

        try {
            if ("jsonObject".equals(type)) {
                // 返回数据格式为jsonObject
                jsonObject = client.callJSONObject(mMethod, mObjects);
                LogUtil.e("NewHttpRequestRunnable---taskid----->" + taskId + "---jsonObject---->" + jsonObject.toString());
            } else {
                // 返回数据格式为jsonArray
                jsonArray = client.callJSONArray(mMethod, mObjects);
                LogUtil.e("jsonArray---->" + jsonArray.toString());
            }
            if (null == mactivityWeakReference.get() || mactivityWeakReference.get().isFinishing()) {
                loadingDialogDismiss();
                return;
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (null != requestCallback) {
                        requestCallback.requestSuccess(jsonObject, jsonArray, taskId);
                        loadingDialogDismiss();

                    } else {
                        throw new NullPointerException("RequestCallback param is null, you must new RequestCallback param in the Constructor");
                    }
                }
            });


        } catch (JSONRPCException e) {
            e.printStackTrace();
            loadingDialogDismiss();
            String jsonStr = e.toString();
            LogUtil.e("NewHttpRequestRunnable-----error--->" + e.toString());
            if (jsonStr.contains("{") && jsonStr.contains("}")) {
                String resultJson = jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1);
                LogUtil.e("NewHttpRequestRunnable-----resultJson--->" + resultJson);
                solveError(resultJson);
            } else if (jsonStr.contains("Invalid JSON response")) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        errorMsg(-1, new MessageEntity(-1, "服务器异常"));
                    }
                });
            } else if (jsonStr.contains("IO error")) { // 包含了超时异常
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        errorMsg(-1, new MessageEntity(-1, "网络不给力，请稍后尝试"));
                    }
                });
            }

        }

    }

    /**
     * code码处理 by:baozi
     *
     * @param resultJson
     */
    private void solveError(String resultJson) {
        final MessageEntity messageEntity = GsonUtil.gsonToBean(resultJson, MessageEntity.class);
        final int code = messageEntity.getCode();
        LogUtil.d("错误信息 ---------------> " + messageEntity.getMessage());
        if (null == mactivityWeakReference.get() || mactivityWeakReference.get().isFinishing()) {
            loadingDialogDismiss();
            return;
        }
        if (code == 1106 || code == 1510){ //用户未登录
            PreferencesUtil.putPreferences(Constans.ISLOGIN,false);
            Message message = new Message();
            message.what = 11;
            mHandler.sendMessage(message);
        }
//        if (code == 1670) {//切换通道
//            try {
//                JSONObject jsonObject = new JSONObject(resultJson);
//                if (jsonObject.has("data")) {
//                    JSONObject data = jsonObject.getJSONObject("data");
//                    if (data.has("showChannelBtn")) {
//                        Boolean showChannelBtn = data.getBoolean("showChannelBtn");
//                        PreferencesUtil.putPreferences("showChannelBtn", showChannelBtn);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            PreferencesUtil.putPreferences("showChannelBtn", false);
//        }

        // 用户登录过期与被挤下线单独处理 无需回调error信息
//        if (code == 1106) {//用户未登录
//            setJPAlias();
//            // ========== 获得当前Activity名字 ===========
//            ActivityManager am = (ActivityManager) mactivityWeakReference.get().getSystemService(Context.ACTIVITY_SERVICE);
//            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//            String className = cn.getClassName();
//            Class clazz = null;
//            try {
//                clazz = Class.forName(className);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            //判断当前activity是不是mainactivity，防止某些情况下弹出未登录框
//            if (className.equals(MainActivity.class.getName())) {
//                if (clazz != null) {
//                    return;
//                }
//            }
//            final String message = messageEntity.getMessage();
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    synchronized (NewHttpRequest.class) {
//                        if (loggin_overdue_dialog == null) {
//                            loggin_overdue_dialog = userLoginDialog(mactivityWeakReference.get(), message, false, false);
//                            if (!loggin_overdue_dialog.isShowing()) {
//                                loggin_overdue_dialog.show();
//                            }
//                        } else {
//                            if (!loggin_overdue_dialog.isShowing()) {
//                                loggin_overdue_dialog.show();
//                            }
//                        }
//                        loadingDialogDismiss();
//                    }
//                }
//            });
//
//        }
//        else
//        if (code == 1510) {//其他设备登录下线处理
//            setJPAlias();
//            final String message = messageEntity.getMessage();
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    synchronized (NewHttpRequest.class) {
//                        if (loggin_invalid_dialog == null) {
//                            loggin_invalid_dialog = userLoginDialog(mactivityWeakReference.get(), message, false, false);
//                            if (!loggin_invalid_dialog.isShowing()) {
//                                loggin_invalid_dialog.show();
//                            }
//                        } else {
//                            if (!loggin_invalid_dialog.isShowing()) {
//                                loggin_invalid_dialog.show();
//                            }
//                        }
//                        loadingDialogDismiss();
//                    }
//                }
//            });
//        }
        else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    errorMsg(code, messageEntity);
                }
            });
        }
    }

    private void errorMsg(int code, MessageEntity messageEntity) {
        if (null != requestCallback) {
            requestCallback.requestError(code, messageEntity, taskId);
            loadingDialogDismiss();

        } else {
            throw new NullPointerException("RequestCallback param is null, you must new RequestCallback param in the Constructor");
        }
    }


    public void executeTask() {
        if (mactivityWeakReference == null || null == mactivityWeakReference.get() || mactivityWeakReference.get().isFinishing()) {
            return;
        }

        if (ConnectionUtil.isConnected(mactivityWeakReference.get())) {
            if (isShowLoad) {
                showLoadingDialog();
            }
            DefaultThreadPool.getInstance().execute(this);
        } else {
            if (null != requestCallback) {
                requestCallback.netWorkError();
            } else {
                throw new NullPointerException("RequestCallback param is null, you must new RequestCallback param in the Constructor");
            }
        }
    }


    public class MyNetHandle extends Handler {

        WeakReference<Activity> mActivityReference;

        MyNetHandle(Activity activity) {
            mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
                if (msg.what == 11){
                    final Dialog dialog = new Dialog(mactivityWeakReference.get(), R.style.dialog_bgTransparent);
                    View view = LayoutInflater.from(mactivityWeakReference.get()).inflate(R.layout.dialog_login_error, null);
                    ImageButton imgBenClose = (ImageButton) view.findViewById(R.id.imgBtn_dialogLoginError_close);
                    Button btn = (Button) view.findViewById(R.id.btn_dialogLoginError_readLogin);
                    imgBenClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mactivityWeakReference.get().startActivity(new Intent(mactivityWeakReference.get(), LoginActivity.class));
                        }
                    });
                    dialog.setContentView(view);
                    dialog.show();
                }
            }
        }

    }

    /**
     * 显示加载动画
     */
    private void showLoadingDialog() {
        if (dialogLoading == null){
            dialogLoading = new DialogLoading(mactivityWeakReference.get());
        }
        if (!dialogLoading.isShow()){
            dialogLoading.show();
        }

    }

    /**
     * 加载动画消失
     */
    private void loadingDialogDismiss() {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
            dialogLoading = null;
        }
    }

    //防止异步请求造成提示文本信息错乱
    static String false_login_content;
    static Dialog dialog_out;

//    /**
//     * 登录失效或者被挤掉线
//     */
//    private static Dialog userLoginDialog(final Context context, String content, boolean iscenter, boolean flag) {
//        if (TextUtils.isEmpty(false_login_content))
//            false_login_content = content;
//        if (dialog_out == null)
//            dialog_out = new Dialog(context, R.style.myStyleDialog);
//        dialog_out.setCanceledOnTouchOutside(flag);
//        dialog_out.setCancelable(flag);
////        LayoutInflater inflater = LayoutInflater.from(context);
////        View window = inflater.inflate(R.layout.dialog_know, null);// 得到加载view
//        Window window = dialog_out.getWindow();
//        window.setContentView(R.layout.dialog_know);
//        WindowManager.LayoutParams params = window.getAttributes();
//        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
//        params.width = widthPixels / 10 * 8;
//        window.setAttributes(params);
//        TextView tv_msg = (TextView) window.findViewById(R.id.tv_msg);
//        if (iscenter) {
//            tv_msg.setGravity(Gravity.CENTER);
//        } else {
//            tv_msg.setGravity(Gravity.NO_GRAVITY);
//        }
//        if (false_login_content.equals(content)) {
//            tv_msg.setText(content);
//        } else {
//            tv_msg.setText(false_login_content);
//        }
//        TextView confirm = (TextView) window.findViewById(R.id.confirm);
//        confirm.setText("重新登录");
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog_out.dismiss();
//                false_login_content = "";
//                SharedPreferenceUtil.putUserPreferences("isLogin", false);
//                if (null == context || ((Activity) context).isFinishing()) {
//                    return;
//                }
//                // ========== 获得当前Activity名字 ===========
//                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//                String className = cn.getClassName();
//                Class clazz = null;
//                try {
//                    clazz = Class.forName(className);
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                /** 不是登陆界面  就跳转到登陆界面 */
//                if (!className.equals(UserLoginNewActivity.class.getName()) || !className.equals(InputPhoneActivity.class.getName())) {
//                    if (!className.equals(MainActivity.class.getName())) {
//                        if (clazz != null) {
//                            AppManager.getAppManager().findFinishActivity(clazz);
//                        }
//                    }
//                    SharedPreferenceUtil.getSpInstance(context).edit().clear().commit();
//                    //调用登录方法
//                    String account = SharedPreferenceUtil.getLoginAccount(context);
//                    Intent intent = null;
//                    if (!TextUtils.isEmpty(account)) {
//                        intent = new Intent(context, UserLoginNewActivity.class);
//                        intent.putExtra("phone", account);
//                    } else {
//                        intent = new Intent(context, InputPhoneActivity.class);
//                    }
//                    context.startActivity(intent);
//                }
//            }
//        });
//        return dialog_out;
//    }

    /**
     * 取消极光推送
     */
    private void setJPAlias() {
        JPushInterface.setAlias(mactivityWeakReference.get(), "", new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i == 0) {
                    LogUtil.i("手机别名被取消");
                }
            }
        });
    }

}



