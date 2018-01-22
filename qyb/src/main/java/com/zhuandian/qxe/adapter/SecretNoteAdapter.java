package com.zhuandian.qxe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.entity.SercetNoteEntity;

import java.util.List;

/**
 * Created by 谢栋 on 2016/11/2.
 */
public class SecretNoteAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private List<SercetNoteEntity> mdatas;
    private LayoutInflater inflater;
    private Context context;

    private OnItemClickListener listener;

    //对外暴漏绑定点击事件的方法
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;  //完成映射
    }

    //适配器的Item点击回调接口
    public interface OnItemClickListener{
        /**
         * @param v   被点击的View
         * @param position  位置
         */
        public void onItemClick(View v, int position);
        public void onItemLongClick(View v, int position);
    }

    public SecretNoteAdapter( Context context ,List<SercetNoteEntity> mdatas) {
        this.mdatas = mdatas;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }



    /**
     * 创建一个viewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =inflater.inflate(R.layout.secret_note_fram_item,parent,false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }


    /**
     * 绑定一个ViewHolder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

               holder.time.setText(mdatas.get(position).gettime());
               holder.content.setText(mdatas.get(position).getContent());


        //如果listener不为空，为其绑定点击事件
        if(listener!=null){

            //绑定点击事件
            holder.noteLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //把当前的holder持有的view控件及下标位置，传递给点击方法
                    listener.onItemClick(holder.content,position);
                }
            });

            //绑定长按事件
            holder.noteLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //把当前的holder持有的view控件及下标位置，传递给长按方法
                    listener.onItemLongClick(holder.content,position);
                    return true;   //return true即可解决长按事件跟点击事件同时响应的问题
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mdatas.size();
    }

    /**
     * 添加一个Item
     * @param pos
     */
    public void addData(int pos){

        //把要插入的内容添加到pos位置
        mdatas.add(pos,new SercetNoteEntity());

        //注意不是notifyDataSetChange()
        notifyItemInserted(pos);
    }


    /**
     * 删除一个Item
     * @param pos
     */
    public void deleteData(int pos){
        mdatas.remove(pos);

        notifyItemRemoved(pos);//通知移除Item
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView time;
    TextView content;
    LinearLayout noteLinearLayout;  //处理Item点击事件引入的控件

    public MyViewHolder(View itemView) {
        super(itemView);

        content = (TextView) itemView.findViewById(R.id.content);
        time = (TextView) itemView.findViewById(R.id.time);
        noteLinearLayout = (LinearLayout) itemView.findViewById(R.id.note_layout);
    }
}
