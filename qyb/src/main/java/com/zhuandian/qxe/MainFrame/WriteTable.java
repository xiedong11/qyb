package com.zhuandian.qxe.MainFrame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.utils.MyView.WriteTableView;

/**
 * 写字板功能
 * Created by 谢栋 on 2017/1/6.
 */
public class WriteTable extends Activity {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)  //关闭低版本不兼容的编译警告
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(this);
        FloatingActionButton fab = new FloatingActionButton(this);
//        fab.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear_bitmap_fab_bg));

        fab.setBackgroundResource(R.drawable.clear_bitmap_fab_bg);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getRealMetrics(displayMetrics);

        //获取当前设备的宽高
        final int width = displayMetrics.widthPixels;
        final int height = displayMetrics.heightPixels;
        final WriteTableView tableView = new WriteTableView(this, width, height);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tableView.clearCurrentBitmap();

            }
        });

        //添加布局
        coordinatorLayout.addView(tableView);
        coordinatorLayout.addView(fab);
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(fab.getLayoutParams());
        lp.gravity= Gravity.CENTER|Gravity.BOTTOM;
        fab.setLayoutParams(lp);
        setContentView(coordinatorLayout);
    }
}
