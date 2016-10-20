package com.lovearthstudio.duaui.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lovearthstudio.duaui.R;

/**
 * Author：Mingyu Yi on 2016/5/11 10:19
 * Email：461072496@qq.com
 */
public class AlertUtil {
    public static void showToast(final Context context, final CharSequence text, final int duration ) {
        ThreadUtil.runOnMainThread(new Runnable() {
            public void run() {
                Toast.makeText(context, text, duration).show();
            }
        });
    }
    public static void showToast(Context context,int resId, int duration) {
        showToast(context,context.getText(resId), duration);
    }

    public static void showToast(Context context,CharSequence text) {
        showToast(context,text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context,int resId) {
        showToast(context,context.getText(resId));
    }

    public static void showDialog(final AlertDialog dlg, String title, int id, String[] content, final View.OnClickListener listener ) {
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.dua_alert_dialog);
        if(title!=null&&!title.equals("")){
            LinearLayout ll_title = (LinearLayout) window.findViewById(R.id.ll_title);
            ll_title.setVisibility(View.VISIBLE);
            TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
            tv_title.setText(title);
            tv_title.setId(id);
        }
        LinearLayout ll_dialog=(LinearLayout) window.findViewById(R.id.dua_dialog_ll);
        LayoutInflater inflater = window.getLayoutInflater();
        for (int i = 0; i <content.length ; i++) {
            View view=inflater.inflate(R.layout.dua_dialog_content_ll,null);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_content.setId(id+i+1);
            tv_content.setText(content[i]);
            tv_content.setOnClickListener(listener);
            ll_dialog.addView(view);
        }
    }
}
