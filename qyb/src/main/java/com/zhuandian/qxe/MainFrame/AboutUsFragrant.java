package com.zhuandian.qxe.MainFrame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuandian.qxe.R;

/**
 * Created by 谢栋 on 2016/8/10.
 */
public class AboutUsFragrant extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.aboutus,null);
        ( (TextView)  getActivity().findViewById(R.id.navigation_text)).setText("关于我们");


        return view;

    }
}
