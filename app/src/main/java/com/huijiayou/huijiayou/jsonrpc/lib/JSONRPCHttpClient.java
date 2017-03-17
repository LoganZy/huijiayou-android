package com.huijiayou.huijiayou.jsonrpc.lib;

import android.text.TextUtils;
import android.util.Log;

import com.huijiayou.huijiayou.MyApplication;
import com.huijiayou.huijiayou.net.DeviceUtils;
import com.huijiayou.huijiayou.utils.LogUtil;
import com.huijiayou.huijiayou.utils.PreferencesUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Implementation of JSON-RPC over HTTP/POST
 */
public class JSONRPCHttpClient extends com.huijiayou.huijiayou.jsonrpc.lib.JSONRPCClient {

    /*
     * HttpClient to issue the HTTP/POST request
     */
    private HttpClient httpClient;
    /*
     * Service URI
     */
    private String serviceUri;

    // HTTP 1.0
    private static final ProtocolVersion PROTOCOL_VERSION = new ProtocolVersion("HTTP", 1, 0);

    /**
     * Construct a JsonRPCClient with the given httpClient and service uri
     *
     * @param client httpClient to use
     * @param uri    uri of the service
     */
    public JSONRPCHttpClient(HttpClient client, String uri) {
        httpClient = client;
        serviceUri = uri;
    }

    /**
     * Construct a JsonRPCClient with the given service uri
     *
     * @param uri uri of the service
     */
    public JSONRPCHttpClient(String uri) {
        this(new DefaultHttpClient(), uri);
    }

    protected JSONObject doJSONRequest(JSONObject jsonRequest) throws com.huijiayou.huijiayou.jsonrpc.lib.JSONRPCException {
        // Create HTTP/POST request with a JSON entity containing the request
        HttpPost request = new HttpPost(serviceUri);
 /*       String head = SharedPreferenceUtil.getToket(huijiayouApplication.getInstance());
        if (!TextUtils.isEmpty(head)) {
            request.setHeader("Authorization", "Token " + head);
        }*/
        String lastSessionId = PreferencesUtil.getPreferences("session_id","");
        LogUtil.e("jsonrpc------session_id---->" + lastSessionId);
        if (!TextUtils.isEmpty(lastSessionId)) {
            request.setHeader("Cookie", lastSessionId);
        }
        String headInfo = DeviceUtils.getHeadInfo(MyApplication.getContext());
        LogUtil.e("jsonrpc------headInfo---->" + headInfo);
        request.setHeader("User-Agent", headInfo);

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, getConnectionTimeout());
        HttpConnectionParams.setSoTimeout(params, getSoTimeout());
        HttpProtocolParams.setVersion(params, PROTOCOL_VERSION);
        request.setParams(params);

        if (_debug) {
            Log.i(JSONRPCHttpClient.class.toString(), "Request: " + jsonRequest.toString());
        }

        HttpEntity entity;

        try {
            if (encoding.length() > 0) {
                entity = new JSONEntity(jsonRequest, encoding);
            } else {
                entity = new JSONEntity(jsonRequest);
            }
        } catch (UnsupportedEncodingException e1) {
            throw new com.huijiayou.huijiayou.jsonrpc.lib.JSONRPCException("Unsupported encoding", e1);
        }
        request.setEntity(entity);
//        if (_debug) {
//            Log.i(HttpEntity.class.toString(), "HttpEntity: " + entity.toString());
//        }
        try {
            // Execute the request and try to decode the JSON Response
            long t = System.currentTimeMillis();
            HttpResponse response = httpClient.execute(request);


            t = System.currentTimeMillis() - t;
            String responseString = EntityUtils.toString(response.getEntity());

            Header[] headers = response.getAllHeaders();
            responseString = responseString.trim();

            if (_debug) {
                Log.i(JSONRPCHttpClient.class.toString(), "Response: " + responseString);
            }

            String cookieName = "";
            String cookieValue = "";

            // 获取cookie
            List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                LogUtil.e("cookies is  null");
            } else {
                // 有cookie 再进行存储
                for (int i = 0; i < cookies.size(); i++) {
                    LogUtil.e("cookies is not  null");
//                    LogUtil.e("cookies------>" + cookies.get(i).toString());
                    cookieName = cookies.get(i).getName();
                    cookieValue = cookies.get(i).getValue();
                }
                LogUtil.e("sessionId++++++++++++++++++++++++"+cookies.toString());
                if(lastSessionId.contains(cookieName)) {
                    // 保存cookie   替换之前的cooke
                    PreferencesUtil.putPreferences("session_id",cookieName + "=" + cookieValue);
                } else {
                    // 用户cooke + 运营...
                    PreferencesUtil.putPreferences("session_id",lastSessionId + "; "+cookieName + "=" + cookieValue);
                }
            }




            JSONObject jsonResponse = new JSONObject(responseString);
            // Check for remote errors
            if (jsonResponse.has("error")) {
                Object jsonError = jsonResponse.get("error");
                if (!jsonError.equals(null)) {
                    JSONObject errorObj = jsonResponse.getJSONObject("error");
                    int code = errorObj.getInt("code");
                    if (code >= -32099 && code <= -32000)
                        return jsonResponse;
                    else
                        throw new com.huijiayou.huijiayou.jsonrpc.lib.JSONRPCException(jsonResponse.get("error"));
                }
                return jsonResponse; // JSON-RPC 1.0
            } else {
                return jsonResponse; // JSON-RPC 2.0
            }
        }
        // Underlying errors are wrapped into a JSONRPCException instance
        catch (ClientProtocolException e) {
            throw new com.huijiayou.huijiayou.jsonrpc.lib.JSONRPCException("HTTP error", e);
        } catch (IOException e) {
            throw new com.huijiayou.huijiayou.jsonrpc.lib.JSONRPCException("IO error", e);
            //TODO 超时回调
        } catch (JSONException e) {
            throw new com.huijiayou.huijiayou.jsonrpc.lib.JSONRPCException("Invalid JSON response", e);
        }
    }
}
