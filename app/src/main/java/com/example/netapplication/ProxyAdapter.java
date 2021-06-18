package com.example.netapplication;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.new7c.bean.ProxyMemberBean;
import com.example.new7c.util.CfLog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Administrator on 2021/6/15
 * Describe ：
 */

public class ProxyAdapter extends BaseQuickAdapter<ProxyMemberBean, BaseViewHolder> {

    private Context mContext;

    private ImageView iv_proxy_checked ;
//    private int mSelectedPos = 0;   //实现单选，保存当前选中的position

    private   List<ProxyMemberBean> mList ;
    private String memberId;


    public String getMemberId() {
        return memberId;
    }





    public ProxyAdapter(int layoutResId, @Nullable List<ProxyMemberBean> data, Context context, String memberId) {
        super(layoutResId, data);
        this.mContext = context;
        this.mList = data;
        this.memberId = memberId;
//        for (int i = 0; i < mList.size(); i++) {
//            if (mList.get(i).isSelected()) {
//                mSelectedPos = i;
//            }
//        }

    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ProxyMemberBean proxyMemberBean) {

        int position = baseViewHolder.getAdapterPosition();  // 选择的位置。
        // 默认选中第1个
        if (position == 0 && TextUtils.isEmpty(memberId)) {
            memberId = proxyMemberBean.getMemberId();
        }

         ImageView   iv_choose_status =  baseViewHolder.getView(R.id.iv_choose_status);
         if (proxyMemberBean.getMemberId().equals(memberId)){
             iv_proxy_checked= baseViewHolder.getView(R.id.iv_choose_status);
             iv_choose_status.setVisibility(View.VISIBLE);
         }else {
             iv_choose_status.setVisibility(View.GONE);
         }
          baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  CfLog.d(proxyMemberBean.toString());
                  if (iv_proxy_checked != null){
                      iv_proxy_checked.setVisibility(View.GONE);
                  }
                  baseViewHolder.getView(R.id.iv_choose_status).setVisibility(View.VISIBLE);
                  iv_proxy_checked = baseViewHolder.getView(R.id.iv_choose_status);
                  memberId = proxyMemberBean.getMemberId();
              }
          });



        if (proxyMemberBean !=null){
            baseViewHolder.setText(R.id.tv_proxynickname,proxyMemberBean.getNickname());
            baseViewHolder.setText(R.id.tv_proxymemberId,String.valueOf(proxyMemberBean.getMemberId()));



            Glide.with(mContext).load(proxyMemberBean.getAvatar())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop())) // 圆图
                    .placeholder(R.drawable.avatar_round)
                    .into((ImageView) baseViewHolder.getView(R.id.iv_avatar_proxy));
        }



    }
}
