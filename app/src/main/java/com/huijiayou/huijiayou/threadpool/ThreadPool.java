package com.huijiayou.huijiayou.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	public static ExecutorService pool;
	private static int mThreadSize = 10;
	public static ExecutorService getThreadPool() {
		if (pool == null) {
			pool = Executors.newFixedThreadPool(mThreadSize);
		}
		return pool;
	}

	public static void shutdown() {
		if (pool != null) {
			synchronized (pool) {
				if (!pool.isShutdown()) {
					pool.shutdownNow();
					pool = null;
				}
			}
		}
		
	}
}
