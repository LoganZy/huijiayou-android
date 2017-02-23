package com.wanglibao.huijiayou.jsonrpc;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.wanglibao.huijiayou.jsonrpc.lib.JSONRPCClient;
import com.wanglibao.huijiayou.jsonrpc.lib.JSONRPCException;
import com.wanglibao.huijiayou.jsonrpc.lib.JSONRPCParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * JsonRPC 请求通用异步请求
 *
 * @Author: Jason
 * @Date: 15/12/4
 * @Time: 10:16
 */

public class JsonRPCAsyncTask extends AsyncTask<Void, Void, Message> {

    private static final String TAG = JsonRPCAsyncTask.class.getSimpleName();

    private String mUrl;
    private Object[] mObjects;
    private Handler mHandler;
    private String type;
    private String mMethod;
    private int taskId;


    private Fragment mFragmentWeakReference;
    private Activity mBaseActivityWeakReference;

    Dialog dialog = null;

    /**
     * @param url     url地址
     * @param handler handler
     * @param type    jsonObject or jsonArray
     * @param taskId  对应的任务id
     * @param params  可变参数 (如没有参数 不用填写)
     * @oaram method  jsonrpc后台调用的方法名
     */
    public JsonRPCAsyncTask(Fragment fragment, String url, String method, Handler handler, String type, int taskId, Object... params) {

        this.mUrl = url;
        this.mMethod = method;
        this.mObjects = params;
        this.mHandler = handler;
        this.type = type;
        this.taskId = taskId;
        if (fragment != null) {
            mFragmentWeakReference = fragment;
        }
//        dialog = DialogUtil.createLoadingDialog(fragment.getActivity());
    }

    public JsonRPCAsyncTask(boolean dialogNoShow, Fragment fragment, String url, String method, Handler handler, String type, int taskId, Object... params) {

        this.mUrl = url;
        this.mMethod = method;
        this.mObjects = params;
        this.mHandler = handler;
        this.type = type;
        this.taskId = taskId;
        if (fragment != null) {
            mFragmentWeakReference = fragment;
        }
//        if (dialogNoShow) dialog = DialogUtil.createLoadingDialog(fragment.getActivity());
    }

    /**
     * @param url     url地址
     * @param handler handler
     * @param type    jsonObject or jsonArray
     * @param taskId  对应的任务id
     * @param params  可变参数 (如没有参数 不用填写)
     * @oaram method  jsonrpc后台调用的方法名
     */
    public JsonRPCAsyncTask(Activity activity, String url, String method,
                            Handler handler, String type, int taskId, Object... params) {

        this.mUrl = url;
        this.mMethod = method;
        this.mObjects = params;
        this.mHandler = handler;
        this.type = type;
        this.taskId = taskId;
        if (activity != null) {
            mBaseActivityWeakReference = activity;
        }
//        dialog = DialogUtil.createLoadingDialog(mBaseActivityWeakReference);
    }

    public JsonRPCAsyncTask(boolean dialogNoShow, Activity activity, String url, String method,
                            Handler handler, String type, int taskId, Object... params) {

        this.mUrl = url;
        this.mMethod = method;
        this.mObjects = params;
        this.mHandler = handler;
        this.type = type;
        this.taskId = taskId;
        if (activity != null) {
            mBaseActivityWeakReference = activity;
        }
//        if (dialogNoShow) dialog = DialogUtil.createLoadingDialog(mBaseActivityWeakReference);
    }


    @Override
    protected Message doInBackground(Void... params) {
        JSONRPCClient client = JSONRPCClient.create(mUrl, JSONRPCParams.Versions.VERSION_2);

        client.setConnectionTimeout(1000 * 60);
        client.setSoTimeout(1000 * 60);
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putInt("taskid", taskId);

        try {
            if ("jsonObject".equals(type)) {
                JSONObject jsonObject = client.callJSONObject(mMethod, mObjects);
                msg.what = 0x10100;
                bundle.putString("jsonObject", jsonObject.toString());
                msg.obj = bundle;

            } else {
                JSONArray jsonArray = client.callJSONArray(mMethod, mObjects);
                msg.what = 0x20100;
                bundle.putString("jsonObject", jsonArray.toString());
                msg.obj = bundle;
            }
            Thread.sleep(10);
        } catch (JSONRPCException e) {
            e.printStackTrace();
            try {
                JSONObject jsonObject = new JSONObject(e.getMessage());
                msg.what = 0x30100;
                bundle.putString("message", jsonObject.optString("message"));
                msg.obj = bundle;
            } catch (JSONException e1) {
                e1.printStackTrace();
                msg.what = 0x40100;
                msg.obj = bundle;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        return msg;
    }

    @Override
    protected void onPostExecute(Message message) {
        super.onPostExecute(message);
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
        if (mFragmentWeakReference != null) {
            if (mFragmentWeakReference == null || !mFragmentWeakReference.isAdded() || mFragmentWeakReference.getActivity().isFinishing()) {
                return;
            }
        } else if (mBaseActivityWeakReference != null) {
            if (mBaseActivityWeakReference == null || mBaseActivityWeakReference.isFinishing()) {
                return;
            }
        }

        if (message.obj != null) {
            Bundle bundle = (Bundle) message.obj;
            Object object = bundle.get("jsonObject");
            JSONObject jsonObject = null;
            Object errorCode = null;
            Object msg = null;
            if (object != null) {
                try {
                    jsonObject = new JSONObject(object.toString());
                    errorCode = jsonObject.get("error_code");
                    msg = jsonObject.get("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                message.arg1 = bundle.getInt("taskid");
                if (errorCode != null && msg != null) {
                    message.arg2 = 2;
                    message.obj = msg;
                } else {
                    message.obj = object;
                }
                mHandler.sendMessage(message);
            } else {
//                showError(bundle);
            }
        }
    }

    public void showError(Bundle bundle) {
        int taskId = bundle.getInt("taskid");
//        String tag = "";
//        if (taskId <= NetConfig.functionList.size() - 1) {
//            tag = NetConfig.functionList.get(taskId);
//        }
        if (mBaseActivityWeakReference != null) {
//            ToastUtil.createNormalToast("服务器出问题了");
        } else if (mFragmentWeakReference != null) {
//            ToastUtil.createNormalToast("服务器出问题了");
        }

    }
}
