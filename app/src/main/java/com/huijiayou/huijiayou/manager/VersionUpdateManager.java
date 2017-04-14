package com.huijiayou.huijiayou.manager;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.R;
import com.huijiayou.huijiayou.bean.Version;
import com.huijiayou.huijiayou.config.NetConfig;
import com.huijiayou.huijiayou.net.MessageEntity;
import com.huijiayou.huijiayou.net.NewHttpRequest;
import com.huijiayou.huijiayou.utils.CommitUtils;
import com.huijiayou.huijiayou.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by lugg on 2017/4/13.
 */

public class VersionUpdateManager implements NewHttpRequest.RequestCallback{
    boolean isShow; // 是否显示dialog和Toast
    Activity activity;
    Version version;
    private String mSavePath; /* 下载保存路径 */

    private NotificationManager manager;
    private Notification notif;
    private android.widget.RemoteViews remoteViews;
    private PendingIntent pendingIntent;

    private Intent updateIntent;

    /* 下载中 */
    private static final int DOWNLOAD = 1;

    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;

    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    /* 记录进度条数量 */
    private int progress;

    public VersionUpdateManager(Activity activity){
        this.activity = activity;
    }

    public void checkVersionUpdate(boolean isShow){
        this.isShow = isShow;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("platform", "2");
        new NewHttpRequest(activity, NetConfig.ACCOUNT, NetConfig.appVersionSee, "jsonObject", 1, hashMap, isShow, this).executeTask();
    }

    private void showUpdateDialog(){
        final Dialog noticeDialog = new Dialog(activity,R.style.dialog_bgTransparent);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_version_update, null);
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.getWindow().setContentView(view);
        noticeDialog.show();
        TextView newVersion = (TextView) noticeDialog.findViewById(R.id.tv_dialogVersionUpdat_newVersion);
        TextView currentVersion = (TextView) noticeDialog.findViewById(R.id.tv_dialogVersionUpdate_currentVersion);
        TextView appSize = (TextView) noticeDialog.findViewById(R.id.tv_dialogVersionUpdate_appSize);
        TextView content = (TextView) noticeDialog.findViewById(R.id.tv_dialogVersionUpdate_content);
        Button noUpdate = (Button) noticeDialog.findViewById(R.id.btn_dialogVersionUpdate_noUpdate);
        Button update = (Button) noticeDialog.findViewById(R.id.btn_dialogVersionUpdate_update);
        newVersion.setText("V " + version.getVersion());
        currentVersion.setText("当前版本: V" + CommitUtils.getVersion(activity));
        appSize.setText("应用大小: " + version.getSize());
        content.setText(Html.fromHtml(version.getDescription()));
        if ("1".equals(version.getForce())){
            noUpdate.setVisibility(View.GONE);
            noticeDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
                        ((MyApplication)activity.getApplication()).exit();
                        return true;
                    }else{
                        return false;
                    }
                }
            });
        }else{
            noUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticeDialog.dismiss();
                }
            });
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
                ToastUtils.createNormalToast(activity, "开始下载");
                showDownloadProcessDialog();
            }
        });

    }

    @Override
    public void netWorkError() {}

    @Override
    public void requestSuccess(JSONObject jsonObject, JSONArray jsonArray, int taskId) {
        try {
            Object code = jsonObject.get("code");
            if (code != null && code.toString().equals("0")){ //获取成功
                version = new Gson().fromJson(jsonObject.get("result").toString(), Version.class);
                String currentVersion = CommitUtils.getVersion(activity);
                if (currentVersion.compareTo(version.getVersion()) < 0){
                    showUpdateDialog();
                }else{
                    if (isShow){
                        ToastUtils.createNormalToast(activity, "当前已是最新版本");
                    }
                }
            }else { //获取失败
                if (isShow){
                    ToastUtils.createNormalToast(activity, "当前已是最新版本");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestError(int code, MessageEntity msg, int taskId) {
        ToastUtils.createNormalToast(activity, msg.getMessage());
    }


    long currentTime = 0;
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获取SD卡路径
                    mSavePath = Environment.getExternalStorageDirectory() + "/download/";
                    File file = new File(mSavePath);
                    // 如果SD卡目录不存在创建
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    URL url = new URL(version.getUrl());
//                  URL url = new URL("http://42.56.65.144/imtt.dd.qq.com/16891/7796A5284E2D479659DA55F6D6B89208.apk?mkey=57b3fe4384484ace&f=d287&c=0&fsname=com.alex.lookwifipassword_2.9.8_36.apk&csr=4d5s&p=.apk");
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    currentTime = System.currentTimeMillis();
                    File apkFile = new File(mSavePath + "huijiayou" +version.getVersion() + currentTime + ".apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    int lastProgress = 0;
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        if (progress != lastProgress || progress == 100) {
                            lastProgress = progress;
                            handler.sendEmptyMessage(DOWNLOAD);
                            if (numread <= 0) {
                                // 下载完成
                                handler.sendEmptyMessage(DOWNLOAD_FINISH);
                                break;
                            }
                        }
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    notif.contentView.setProgressBar(R.id.progressBar_notification_progress, 100, progress, false);
                    manager.notify(0, notif);
                    break;
                case DOWNLOAD_FINISH:
                    installApk();
                    break;
                default:
                    break;
            }

            return false;
        }
    });

    /**
     * 显示软件下载对话框
     */
    private void showDownloadProcessDialog() {
        updateIntent = new Intent();
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(activity, 0, updateIntent, 0);

        manager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
        notif = new Notification();
        notif.icon = R.mipmap.ic_launcher;
        notif.tickerText = "开始下载";

        remoteViews = new RemoteViews(activity.getPackageName(), R.layout.progress_version_update);
        remoteViews.setProgressBar(R.id.progressBar_notification_progress, 100, 0, false);

        notif.contentView = remoteViews;
        notif.contentIntent = pendingIntent;
        manager.notify(0, notif);

        downloadApkThread task = new downloadApkThread();
        task.start();
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File file = new File(mSavePath + "huijiayou" +version.getVersion() + currentTime + ".apk");
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT>=24) { //判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(activity, "com.huijiayou.huijiayou.fileprovider", file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
        manager.cancelAll();
        ((MyApplication)activity.getApplication()).exit();
    }
}
