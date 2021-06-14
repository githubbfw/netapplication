package com.example.netapplication.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.new7c.R;
import com.example.new7c.base.BaseActivity;
import com.example.new7c.bean.AccountDetailBean;
import com.example.new7c.bean.DetailListBean;
import com.example.new7c.custom.SpacesItemDecoration;
import com.example.new7c.net.BaseObserver2;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AccountDetailActivity extends BaseActivity {
    private Context mContext;


    private AppCompatTextView recording_accountdetail; // 点击记录筛选
    private AppCompatImageView back_accountdetail; // 返回


    private SmartRefreshLayout account_smartfreshlayout;
    private SwipeRecyclerView account_swipereview;

    private AccountDetailAdapter mAccountDetailAdapter;
    private List<DetailListBean> mDetailListBeans = new ArrayList<>();




    private AppCompatTextView tv_income_money; // 收入金额
    private AppCompatTextView tv_pay_money;  // 支付金额


    private String surestarttime;
    private String suresendtime;


    private  int  pageNum = 1 ;


    @Override
    protected int setLayout() {
        return R.layout.activity_account_detail;
    }

    @Override
    protected void initView() {
        mContext = com.example.new7c.activity.AccountDetailActivity.this;
        back_accountdetail = findViewById(R.id.back_accountdetail);
        back_accountdetail.setOnClickListener(this);

        recording_accountdetail = findViewById(R.id.recording_accountdetail);
        recording_accountdetail.setOnClickListener(this);

        account_smartfreshlayout = findViewById(R.id.account_smartfreshlayout);
        account_swipereview = findViewById(R.id.account_swipereview);

        mAccountDetailAdapter = new AccountDetailAdapter(R.layout.item_accountdetail,mDetailListBeans,mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        account_swipereview.setLayoutManager(linearLayoutManager);

        int space = SizeUtils.dp2px(10);
        account_swipereview.addItemDecoration(new SpacesItemDecoration(space));

        account_swipereview.setAdapter(mAccountDetailAdapter);


        tv_income_money = findViewById(R.id.tv_income_money);// 收入金额
        tv_pay_money = findViewById(R.id.tv_pay_money);  // 支付金额


        mAccountDetailAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ToastUtils.showShort("-------------","--"+position+""+view.getId());
                Log.i("tag","--"+position+""+view.getId());
//                pageNum++;
//                 getOneDetail(detailListBean.getDate(),"5",pageNum);


            }
        });




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recording_accountdetail:
//                ActivityUtils.startActivity(new Intent(mContext, RecordChooseActivity.class));
                // 再次点击， 需要清空数据
                mDetailListBeans.clear();

                ActivityUtils.startActivityForResult((Activity) mContext, RecordChooseActivity.class, 4);
                break;
            case R.id.back_accountdetail: // 退出
                finish();
                break;



        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == RESULT_OK) {
            Log.i("tag111000", "--------------");
            ToastUtils.showShort("回来了");
            // 单选时间查询
//            intent.putExtra("surestarttime", surestarttime);
//            intent.putExtra("suresendtime", suresendtime);


            String dateType = data.getStringExtra("dateType");
            String starttime = data.getStringExtra("surestarttime");
            String endtime = data.getStringExtra("suresendtime");


            if (!TextUtils.isEmpty(dateType)){
                Log.i("tag4444", dateType);
            }


            if (!TextUtils.isEmpty(starttime) && !TextUtils.isEmpty(endtime)) {
                surestarttime = starttime;
                suresendtime = endtime;
                Log.i("tag1111", surestarttime);
                Log.i("tag1111", suresendtime);
            }
            // 数据回来了，-----进行网络请求，
            if (!TextUtils.isEmpty(dateType)){ // dateType 不为空，才进行数据请求
                getAccountDetail(dateType, surestarttime, suresendtime);
            }else {
              ToastUtils.showShort("选择类型为空!");
            }

        }


    }

    @Override
    protected void initData() {
        account_smartfreshlayout.setEnableRefresh(true);
        account_smartfreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.finishRefresh();
                }
                // 再次刷新
//               getAccountDetail();

            }
        });


    }


    private void getAccountDetail(String dateType, String surestarttime, String suresendtime) {
        RetrofitServiceManager.getInstance().create(ApiServer.class)
                .getSearchDetail(dateType, surestarttime, suresendtime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<AccountDetailBean>(new ResponseCallBack<AccountDetailBean>() {
                    @Override
                    public void onSuccess(AccountDetailBean accountDetailBean) {
                        if (accountDetailBean != null) {
                            // 这个具体的详情页面还没弄好，这个就暂时先到这里
                            Log.i("tag", accountDetailBean.toString() + "----------------------");
                            String income = accountDetailBean.getIncome(); // 收入；
                            String payOut = accountDetailBean.getPayOut();// 支出

                            if (!TextUtils.isEmpty(income)&&!TextUtils.isEmpty(payOut)){
                                tv_income_money.setText(income);
                                tv_pay_money.setText(payOut);
                            }

                            List<DetailListBean> list = accountDetailBean.getList(); // recyview的数据
                            if (list!=null && list.size()>0){
                                mDetailListBeans.addAll(list);
                                mAccountDetailAdapter.notifyDataSetChanged();
                            }


                        }

                    }

                    @Override
                    public void onFault(int code, String errorMsg) {
                        ToastUtils.showShort(code + errorMsg);
                    }
                }));
    }

//    private  void  getOneDetail(String exactDate,String pageSize,int pageNum){
//        RetrofitServiceManager.getInstance().create(ApiServer.class)
//                .getOneDetail(exactDate, pageSize, pageNum)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver2<>(new ResponseCallBack<OneDetailBean>() {
//                    @Override
//                    public void onSuccess(OneDetailBean oneDetailBean) {
//                        if (oneDetailBean!=null){
//                            list.addAll(oneDetailBean.getList());
//                            Log.i("-----",oneDetailBean.getList().toString());
//                            itemAccountAdapter.notifyDataSetChanged();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFault(int code, String errorMsg) {
//                        ToastUtils.showShort(code+errorMsg);
//
//                    }
//                }));
//    }





}