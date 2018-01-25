package com.zhuandian.qxe.MainFrame.esGoods;

import android.support.v7.widget.LinearLayoutManager;

import com.android.databinding.library.baseAdapters.BR;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.BaseViewModel;
import com.zhuandian.qxe.base.binding.MvvmCommonAdapter;
import com.zhuandian.qxe.databinding.FragmentSecondHandGoodsBinding;
import com.zhuandian.qxe.entity.GoodsEntity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * desc：二手商品列表页ViewModel
 * author：xiedong
 * date：2018/1/17.
 */
public class SecondHandGoodsViewModel extends BaseViewModel<FragmentSecondHandGoodsBinding> {
    @Override
    protected void initView() {
        binding.swipeRefresh.setRefreshing(false);
        binding.rvGoodsList.setLayoutManager(new LinearLayoutManager(activity));
        initData();
    }

    private void initData() {
        //查询数据库中的具体商品信息
        final GoodsEntity goodsBenn = new GoodsEntity();
        BmobQuery<GoodsEntity> query = new BmobQuery<GoodsEntity>();
        query.order("-updatedAT");
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<GoodsEntity>() {
            @Override
            public void done(List<GoodsEntity> data, BmobException e) {
                if (e == null) {
                    if (data.size() == 0) {
                        //数据全部被加载，，已经没有可再加载的数据时，对footview的操作
                    }
                    binding.rvGoodsList.setAdapter(new MvvmCommonAdapter(data, BR.viewModel, activity, R.layout.item_second_goods));
                }
            }
        });

    }
}
