package com.zhuandian.qxe.ExtralModule.gradeQuery;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.QYBActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 谢栋 on 2016/10/18.
 */
public class QueryGradeActivity extends QYBActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    public void setContent() {
        setContentView(R.layout.activity_query_grade);
    }

    @Override
    public void setupView() {
        toolbarTitle.setText("成绩查询");
    }

    @Override
    public void setModle() {

    }

    @OnClick({R.id.cet_4_6, R.id.computer_grade, R.id.putonghua_grade, R.id.spoken_grade, R.id.interrupt_grade, R.id.iv_back})
    public void btnListener(View v) {
        switch (v.getId()) {

            case R.id.cet_4_6:
                changeFragment(QueryFragment.getInstance("英语四六级"));
                break;
            case R.id.spoken_grade:
                changeFragment(QueryFragment.getInstance("英语口语"));
                break;
            case R.id.computer_grade:
                changeFragment(QueryFragment.getInstance("计算机"));
                break;
            case R.id.putonghua_grade:
                changeFragment(QueryFragment.getInstance("普通话"));
                break;
            case R.id.interrupt_grade:
                changeFragment(QueryFragment.getInstance("翻译"));
                break;
            case R.id.iv_back:
                finish();
                break;

        }

    }


    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.query_fragment, fragment, "fragment")
                .addToBackStack("fragment")
                .commit();
    }
}
