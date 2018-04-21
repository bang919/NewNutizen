package com.nutizen.nu.utils;

import android.util.Log;

public class LogUtils {
	/**
	 * 是否启动调试模式
	 */
	public static boolean isDebug = false;

	public static void d(String msg) {
		d("DEBUG", msg);
	}

	public static void d(String tag, String msg) {
		println(Log.DEBUG, tag, msg);
	}

	public static void i(String msg) {
		i("INFO", msg);
	}

	public static void i(String tag, String msg) {
		println(Log.INFO, tag, msg);
	}

	public static void w(String msg) {
		w("warning", msg);
	}

	public static void w(String tag, String msg) {
		println(Log.WARN, tag, msg);
	}

	public static void e(String msg) {
		e("error", msg);
	}

	public static void e(String tag, String msg) {
		println(Log.ERROR, tag, msg);
	}

	public static void v(String msg) {
		v("error", msg);
	}

	public static void v(String tag, String msg) {
		println(Log.VERBOSE, tag, msg);
	}

	private static void println(int level, String tag, String msg) {
		if (isDebug) {
			if (tag == null) {
				switch (level) {
					case Log.ASSERT:
						tag = "assert";
						break;
					case Log.DEBUG:
						tag = "debug";
						break;
					case Log.INFO:
						tag = "info";
						break;
					case Log.WARN:
						tag = "warning";
						break;
					case Log.ERROR:
						tag = "error";
						break;
					case Log.VERBOSE:
						tag = "verbose";
						break;
				}
			}
			Log.println(level, tag, msg);
		}
	}
}