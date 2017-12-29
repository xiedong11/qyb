package com.zhuandian.qxe.MainFrame.esGoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zhuandian.qxe.R;

/**
 * Created by 谢栋 on 2016/9/30.
 */
public class SearchGoodsFragment extends Fragment {

    private View view;
    private Button searchButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_dialog,null);

        initView();


        return view;

    }

    private void initView() {

        searchButton = (Button) view.findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"没有相关商品呢，同学...",Toast.LENGTH_SHORT).show();

                FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft =fm.beginTransaction();

                //获取Framgent事务
                Fragment searchFragment = fm.findFragmentById(R.id.container);
                ft.remove(searchFragment);

                Log.i("xiedong", "搜索界面中的回退栈个数" + getFragmentManager().getBackStackEntryCount());
//                getFragmentManager().getFragments().clear();  //清除掉所有fragment
                getFragmentManager().popBackStack();// 这个方法可以让栈顶的fragment出栈
                Log.i("xiedong", "搜索界面中的回退栈个数" + getFragmentManager().getBackStackEntryCount());

//                ft.add(R.id.container,searchGoodsFragment,"searchGoodsFragment");
//                ft.addToBackStack("searchGoodsFragment");

                ft.commit();  //提交事务
            }
        });
    }
}
