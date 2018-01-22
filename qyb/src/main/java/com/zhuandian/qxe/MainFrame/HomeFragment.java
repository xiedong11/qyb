package com.zhuandian.qxe.MainFrame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.special.ResideMenu.ResideMenu;
import com.zhuandian.qxe.ExtralModule.CommunityActivity;
import com.zhuandian.qxe.ExtralModule.Postgraduate_Plan;
import com.zhuandian.qxe.ExtralModule.StudyCountDown;
import com.zhuandian.qxe.ExtralModule.TestCalendar;
import com.zhuandian.qxe.ExtralModule.gradeQuery.QueryGradeActivity;
import com.zhuandian.qxe.ExtralModule.heartshare.HeartShareActivity;
import com.zhuandian.qxe.ExtralModule.secretNote.Secret_note;
import com.zhuandian.qxe.ExtralModule.wkzxs.WSLActivity;
import com.zhuandian.qxe.ExtralModule.wkzxs.XLActivity;
import com.zhuandian.qxe.ExtralModule.wkzxs.ZHLActivity;
import com.zhuandian.qxe.MenuActivity;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.entity.LostAndFoundEntity;
import com.zhuandian.qxe.utils.MyView.AlwaysMarqueeTextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 软件主界面的相关配置
 *
 * @author 谢栋
 *         2016年8月14日21:11:16
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home_2, container, false);
        ((TextView) getActivity().findViewById(R.id.navigation_text)).setText("曲园帮");

        setLostInfo();  //设置主界面上的失物招领信息
        syncLostInfo();  //同步失物招领信息
        initListener(); //绑定主界面上的各个控件的监听事件
        setUpViews();

//
        return parentView;
    }

    /**
     * 同步失物招领信息
     */
    private void syncLostInfo() {
        BmobQuery<LostAndFoundEntity> query = new BmobQuery<LostAndFoundEntity>();
        query.order("-updatedAt");
        query.setLimit(3);
        query.findObjects(new FindListener<LostAndFoundEntity>() {
            @Override
            public void done(List<LostAndFoundEntity> object, BmobException e) {
                StringBuilder sb = new StringBuilder();

                if (e == null) {
                    for (LostAndFoundEntity info : object) {
                        sb.append(info.getUsername() + " 说 :    ");
                        sb.append(info.getBroadcastContent() + ".          ");
                    }
                } else {
//                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }

                ((AlwaysMarqueeTextView) parentView.findViewById(R.id.qy_broadcast_info)).setText(sb);
            }
        });
    }

    private void setLostInfo() {
        //设置用户需要发起失物招领广播时的跳转监听事件
        parentView.findViewById(R.id.bang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "需要小园帮您发起广播吗...", Snackbar.LENGTH_SHORT)
                        .setAction("去发布！", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeActivity(new com.zhuandian.qxe.MainFrame.LostAndFound());  //跳转到失物招领界面
                            }
                        })
                        .setActionTextColor(Color.parseColor("#E91E63"));  //设置Action字体颜色
                setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));  //设置Snackbar上的字体颜色
                snackbar.show();
//
            }
        });

    }

    /**
     * 设置Snackbar上的字体颜色
     *
     * @param snackbar
     * @param color
     */
    public static void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }

    /**
     * 初始化各个控件上的监听事件
     */
    private void initListener() {
        //自习室点击监听事件
        ((LinearLayout) parentView.findViewById(R.id.xl)).setOnClickListener(this);
        ((LinearLayout) parentView.findViewById(R.id.wsl)).setOnClickListener(this);
        ((LinearLayout) parentView.findViewById(R.id.zhl)).setOnClickListener(this);

        //附加功能点击监听事件
        ((LinearLayout) parentView.findViewById(R.id.community_activity)).setOnClickListener(this);
        ((LinearLayout) parentView.findViewById(R.id.join_us)).setOnClickListener(this);
        ((LinearLayout) parentView.findViewById(R.id.work)).setOnClickListener(this);

        //app主打功能监听事件注册
        ((LinearLayout) parentView.findViewById(R.id.query)).setOnClickListener(this);
        ((LinearLayout) parentView.findViewById(R.id.test)).setOnClickListener(this);
        ((LinearLayout) parentView.findViewById(R.id.game)).setOnClickListener(this);
        ((LinearLayout) parentView.findViewById(R.id.secret)).setOnClickListener(this);
        ((LinearLayout) parentView.findViewById(R.id.heart_share)).setOnClickListener(this);

    }

    private void setUpViews() {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
//
//        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
//            }
//        });

        // 添加滑动操作忽视区域
        HorizontalScrollView ignored_view = (HorizontalScrollView) parentView.findViewById(R.id.ignored_view);
        resideMenu.addIgnoredView(ignored_view);
    }


    /**
     * 点击监听事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.zhl:
                changeActivity(new ZHLActivity());
                break;
            case R.id.xl:
                changeActivity(new XLActivity());
                break;
            case R.id.wsl:
                changeActivity(new WSLActivity());
                break;
            case R.id.query:
                changeActivity(new QueryGradeActivity());
                break;
            case R.id.community_activity:
                changeActivity(new CommunityActivity());
                break;
            case R.id.work:
                changeActivity(new StudyCountDown());
                break;
            case R.id.join_us:
                changeActivity(new Postgraduate_Plan());
                break;
            case R.id.test:
                changeActivity(new TestCalendar());
                break;
            case R.id.game:
                changeActivity(new WriteTable());
                break;
            case R.id.secret:
                changeActivity(new Secret_note());
                break;
            case R.id.heart_share:
                changeActivity(new HeartShareActivity());
                break;
        }

    }


    /**
     * 传入指定的activity可跳转
     *
     * @param activity
     */
    private void changeActivity(Activity activity) {

        Intent intent = new Intent(getActivity(), activity.getClass());
        startActivity(intent);
    }
}
