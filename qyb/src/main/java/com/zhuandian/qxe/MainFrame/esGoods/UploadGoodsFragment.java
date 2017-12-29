package com.zhuandian.qxe.MainFrame.esGoods;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.bean.GoodsBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 谢栋 on 2016/8/14.
 */
public class UploadGoodsFragment extends Fragment {
    private View mView;
    private ImageView goodsImageView;
    private EditText titleEditText;
    private EditText contentEditText;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText priceEditText;
    private Button commitButton;
    private GoodsBean goodsBenn;
    private SweetAlertDialog pDialog;
    private TextView commit;

    private Bitmap mBitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.uploadgoods, null);
        ( (TextView)  getActivity().findViewById(R.id.navigation_text)).setText("闲物处理");

        //初始化Bmob的SDK
        Bmob.initialize(getActivity(), "df25a6c6a79479d11a60f2e89c68b467");
        //初始化控件
        initView();

        //加入dialog提示
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("小二正在玩命记录您的商品...");
        pDialog.setCancelable(false);


        return mView;
    }

    private void initView() {
        goodsImageView = (ImageView) mView.findViewById(R.id.iv_goods);
        titleEditText = (EditText) mView.findViewById(R.id.ed_title);
        contentEditText = (EditText) mView.findViewById(R.id.ed_desc);
        nameEditText = (EditText) mView.findViewById(R.id.ed_name);
        priceEditText= (EditText) mView.findViewById(R.id.ed_price);
        phoneEditText = (EditText) mView.findViewById(R.id.ed_phone);
        commit = (TextView) mView.findViewById(R.id.commit);

        goodsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //给按钮绑定监听事件
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();
                uploadGoods();
            }
        });
    }

    /**
     * 上传会员用户的商品详情到数据库
     */
    private void uploadGoods() {

        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        String price  = priceEditText.getText().toString();
        Bitmap bitmap = mBitmap;

        if("".equals(name) || "".equals(phone) ||"".equals(title) ||"".equals(content) ||"".equals(price) ||mBitmap==null){
            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("请完善商品信息")
                    .setContentText("商品信息所有条目都为必填项 ！！")
                    .show();
            //cancle点提交商品的dialog
            pDialog.cancel();
            pDialog.dismiss();
        }else {

            Log.i("xiedong：", "不该进来哇");
            goodsBenn = new GoodsBean();
            goodsBenn.setPrice(price);
            goodsBenn.setName(name);
            goodsBenn.setPhone(phone);
            goodsBenn.setGoodsTiltle(title);
            goodsBenn.setGoodsContent(content);
//        goodsBenn.setGoodsBitmap(bitmap);


            //上传图片文件并且返回图片的url
            final String picPath = "/sdcard/qyb/Goods.jpg";
            final BmobFile bmobFile = new BmobFile(new File(picPath));
            bmobFile.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.i("上传文件成功:", bmobFile.getFileUrl().toString());
                        //得到商品存储的url地址之后，代码商品属性加载加载完成，然后把各项数据存进数据库
                        goodsBenn.setGoodsUrl(bmobFile.getFileUrl() + "");
                        goodsBenn.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    pDialog.cancel();
                                    pDialog.dismiss();

                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("好的嘛 !")
                                            .setContentText("小二已经记下您的商品啦 !")
                                            .show();
//                                Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i("xie", e.toString());
                                }

                            }

                        });

                    } else {
                        Log.i("上传文件失败：", e.getMessage().toString());
                    }

                }


                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });

        }

    }

    //调用摄像机拍照并存储在指定位置
    private void takePhoto() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }

            String name = "Goods" + ".jpg";
            Toast.makeText(getActivity(), name, Toast.LENGTH_LONG).show();
            Bundle bundle = data.getExtras();
            mBitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;
            File file = new File("/sdcard/qyb/");
            file.mkdirs();// 创建文件夹
            String fileName = "/sdcard/qyb/" + name;

            try {
                b = new FileOutputStream(fileName);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                goodsImageView.setImageBitmap(mBitmap);// 将图片显示在ImageView里
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }

        }
    }


}
