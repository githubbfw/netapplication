package com.example.netapplication.net;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.new7c.R;
import com.example.new7c.bean.DetaiInfo;
import com.example.new7c.bean.DetailListBean;
import com.example.new7c.bean.OneDetailBean;
import com.example.new7c.net.BaseObserver2;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2021/6/13
 * Describe ：
 */

public class AccountDetailAdapter extends BaseQuickAdapter<DetailListBean, BaseViewHolder> {

    private Context mContext;

    private SwipeRecyclerView mSwipeRecyclerView;
    private  int  pageNum = 1 ;
    private  List<DetailListBean> mDetailListBeans;

   private  List<DetaiInfo> mDetaiInfos = new ArrayList<>();

   private  ItemAccountAdapter itemAccountAdapter;




    public AccountDetailAdapter(int layoutResId, @Nullable List<DetailListBean> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
        this.mDetailListBeans = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DetailListBean detailListBean) {
        // 渲染出去
//        ItemAccountAdapter   itemAccountAdapter = new ItemAccountAdapter(R.layout.item_item_accountdetail,mDetaiInfos,mContext);

         if (detailListBean != null){
             baseViewHolder.setText(R.id.tv_date,detailListBean.getDate()); //时间
             baseViewHolder.setText(R.id.tv_income_money,detailListBean.getCensus().getIncome());// 收入
             baseViewHolder.setText(R.id.tv_payoumoney,detailListBean.getCensus().getPayOut()); // 支出

             // todo: // 点击加载更多
             baseViewHolder.findView(R.id.tv_morerecoding).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) { // pageNum = 1 默认是1，
                     pageNum++;
                     getOneDetail(detailListBean.getDate(),"5",pageNum,itemAccountAdapter);

                 }
             });

             if (detailListBean.getList()!=null && detailListBean.getList().size()>0){
//                 mDetaiInfos.clear();
                 List<DetaiInfo> list = detailListBean.getList();// 子listview的数据
//                 mDetaiInfos.addAll(list);
                 mSwipeRecyclerView = baseViewHolder.findView(R.id.item_swiperrv);
                 LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                 linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                 itemAccountAdapter = new ItemAccountAdapter(R.layout.item_item_accountdetail,list,mContext);

                 mSwipeRecyclerView.setLayoutManager(linearLayoutManager);
                 mSwipeRecyclerView.setAdapter(itemAccountAdapter);
             }

         }
    }



    private  void  getOneDetail(String exactDate,String pageSize,int pageNum,ItemAccountAdapter itemAccountAdapter){
        RetrofitServiceManager.getInstance().create(ApiServer.class)
                .getOneDetail(exactDate, pageSize, pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<>(new ResponseCallBack<OneDetailBean>() {
                    @Override
                    public void onSuccess(OneDetailBean oneDetailBean) {
                        if (oneDetailBean!=null){
                            mDetaiInfos.addAll(oneDetailBean.getList());
                            Log.i("-----",oneDetailBean.getList().toString());
                            itemAccountAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onFault(int code, String errorMsg) {
                        ToastUtils.showShort(code+errorMsg);

                    }
                }));
    }





}
