package com.zhuandian.qxe.MainFrame.esGoods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.databinding.library.baseAdapters.BR;
import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.binding.MvvmCommonAdapter;
import com.zhuandian.qxe.databinding.FragmentSecondHandGoodsBinding;
import com.zhuandian.qxe.entity.GoodsEntity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * desc：二手商品列表页
 * author：xiedong
 * date：2018/1/17.
 */
public class SecondHandGoodsFragment extends Fragment {
    @BindView(R.id.rv_goods_list)
    RecyclerView rvGoodsList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private FragmentSecondHandGoodsBinding mBinder;
    private List<GoodsEntity> mDatas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_second_hand_goods, container, false);
        ButterKnife.bind(this, mBinder.getRoot());
        initData();
        mBinder.swipeRefresh.setRefreshing(false);
        mBinder.rvGoodsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mBinder.getRoot();
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
            public void done(List<GoodsEntity> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 0) {
                        //数据全部被加载，，已经没有可再加载的数据时，对footview的操作
                    }
                    mDatas = object;
                    mBinder.rvGoodsList.setAdapter(new MvvmCommonAdapter(mDatas, BR.viewModel, getActivity(), R.layout.item_second_goods));

                }
            }
        });

    }


}
