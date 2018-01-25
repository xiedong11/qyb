package com.zhuandian.qxe.MainFrame.esGoods;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.base.QYBFragment;
import com.zhuandian.qxe.databinding.FragmentSecondHandGoodsBinding;


/**
 * desc：二手商品列表页
 * author：xiedong
 * date：2018/1/17.
 */
public class SecondHandGoodsFragment extends QYBFragment<SecondHandGoodsViewModel,FragmentSecondHandGoodsBinding> {
    @Override
    protected void setupView() {
       viewModel.initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_second_hand_goods;
    }

    @Override
    protected void setModle() {

    }


}
