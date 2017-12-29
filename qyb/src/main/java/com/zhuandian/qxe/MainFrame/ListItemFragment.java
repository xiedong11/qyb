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
 * Created by 谢栋 on 2016/8/31.
 */
public class ListItemFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.itemactivity,null);
        ( (TextView)  getActivity().findViewById(R.id.navigation_text)).setText("商品详情");


        return view;
    }
}
