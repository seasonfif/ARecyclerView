package com.seasonfif.arecyclerviewdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 创建时间：2017年06月24日10:51 <br>
 * 作者：zhangqiang <br>
 * 描述：
 */
public class Utils {

    /**
     * 获取屏幕宽高
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

    /**
     * 网络是否已经连接
     */
    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = null;
            try {
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            } catch (Exception e) {
                // ignore
            }
            if (cm != null) {
                NetworkInfo[] infos = cm.getAllNetworkInfo();
                if (infos != null) {
                    for (NetworkInfo ni : infos) {
                        if (ni.isConnected()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
