package com.zhuandian.qxe.utils.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuandian.qxe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/2.
 */

public class HorizontalItem extends RelativeLayout {

    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    private int rightImageViewSrc;
    private String rightTextStr;
    private String leftTextStr;
    private Context mContext;
    private int rightType;
    private static final int rightTypeText = 0;
    private static final int rightTypeImg = 1;
    private OnRightImgClickListener imgClickListener;
    private OnRightTextClickListener textClickListener;


    public HorizontalItem(Context context) {
        super(context, null);
    }

    public HorizontalItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalItem);
        leftTextStr = typedArray.getString(R.styleable.HorizontalItem_tv_left);
        rightTextStr = typedArray.getString(R.styleable.HorizontalItem_tv_right);
        rightImageViewSrc = typedArray.getResourceId(R.styleable.HorizontalItem_iv_right, R.drawable.add);
        rightType = typedArray.getInt(R.styleable.HorizontalItem_right_type, 0);
        typedArray.recycle();
        initView();
    }

    private void initView() {
        ButterKnife.bind(this, View.inflate(mContext, R.layout.horizontal_item, this));
        if (rightType == rightTypeText) {
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(rightTextStr);
        } else if (rightType == rightTypeImg) {
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageResource(rightImageViewSrc);
        }
        tvLeft.setText(leftTextStr);

        ivRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgClickListener != null) {
                    imgClickListener.onImgClick();
                }

            }
        });

        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textClickListener != null) {
                    textClickListener.onTextClick();
                }
            }
        });
    }


    public void setImgClickListener(OnRightImgClickListener imgClickListener) {
        this.imgClickListener = imgClickListener;
    }

    public void setTextClickListener(OnRightTextClickListener textClickListener) {
        this.textClickListener = textClickListener;
    }

    public interface OnRightImgClickListener {
        void onImgClick();
    }

    public interface OnRightTextClickListener {
        void onTextClick();
    }


}
