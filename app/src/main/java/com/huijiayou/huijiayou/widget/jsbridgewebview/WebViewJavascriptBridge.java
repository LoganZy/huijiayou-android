package com.huijiayou.huijiayou.widget.jsbridgewebview;


public interface WebViewJavascriptBridge {
	
	public void send(String data);
	public void send(String data, CallBackFunction responseCallback);
	
	

}
