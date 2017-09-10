package utlis;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class DubFiles {
    //得到进程名字
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if(null == runningApps)return null;
        for(ActivityManager.RunningAppProcessInfo appInfo : runningApps)if(appInfo.pid == pid)return appInfo.processName;
        return null;
    }
}
