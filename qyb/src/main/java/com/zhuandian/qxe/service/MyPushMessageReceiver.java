package com.zhuandian.qxe.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.zhuandian.qxe.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;


/**
 * Created by 谢栋 on 2016/10/2.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
    private String pushInfo;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)  //版本兼容问题
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Log.d("xiedong", "客户端收到推送内容：" + intent.getStringExtra("msg"));


//            解析得到的json字符串，提取文字{"alert":"更新新版本啦"}
            try {
                JSONObject obj = new JSONObject(intent.getStringExtra("msg"));

                pushInfo = obj.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.icon_notification, "曲园帮", System.currentTimeMillis());

        /********************************   新版本6.0之后setLatestEventInfo废弃使用
                 notification.setLatestEventInfo(context,"曲园帮",pushInfo,null);
                 manager.notify(R.drawable.icon_notification,notification);
         *************************************************************************************/


        Notification noti = new Notification.Builder(context)
                .setContentTitle("曲园帮")
                .setContentText(pushInfo.toString())
                .setSmallIcon(R.drawable.icon_notification)
//                .setLargeIcon(aBitmap)
                .build();

        manager.notify(1, noti);

    }

}