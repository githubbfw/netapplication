package com.example.netapplication.net;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.new7c.Constants;
import com.example.new7c.R;
import com.example.new7c.base.BaseActivity;
import com.example.new7c.bean.ProxyInfo;
import com.example.new7c.net.BaseObserver2;
import com.example.new7c.util.CfLog;
import com.example.new7c.util.SPUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 下线转账
 */

public class OfflineTransferActivity extends BaseActivity {

    private AppCompatImageView back_transfer;
    private AppCompatEditText et_inputusername; //

    private AppCompatTextView tv_chooseoffline; // 选择下线
    private AppCompatTextView tv_mainaccountbalance;// 主账户余额

    private Button btn_surtransfer; // 确认转账
    private Context mContext;


    @Override
    protected int setLayout() {
        return R.layout.activity_offline_transfer;
    }

    @Override
    protected void initView() {
        mContext = com.example.new7c.activity.OfflineTransferActivity.this;

        back_transfer = findViewById(R.id.back_transfer);
        et_inputusername = findViewById(R.id.et_inputusername); //
        tv_chooseoffline = findViewById(R.id.tv_chooseoffline); // 选择下
        tv_mainaccountbalance = findViewById(R.id.tv_mainaccountbalance);//
        btn_surtransfer = findViewById(R.id.btn_surtransfer); // 确认转账

        back_transfer.setOnClickListener(this);
        tv_chooseoffline.setOnClickListener(this);
        btn_surtransfer.setOnClickListener(this);

        if (SPUtils.getInstance().getString(Constants.BALANCE)!=null){
            tv_mainaccountbalance.setText(SPUtils.getInstance().getString(Constants.BALANCE));
        }else {
            tv_mainaccountbalance.setText("0.00");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_transfer:
                finish();
                break;

            case R.id.tv_chooseoffline:// 选择下线
//                ActivityUtils.startActivity(new Intent(mContext,ChooseProxyActivity.class));
//                ActivityUtils.startActivityForResult((Activity) mContext, ChooseProxyActivity.class, 5);
                Intent data = new Intent(mContext, ChooseProxyActivity.class);
                data.putExtra("id", et_inputusername.getText().toString().trim());
                CfLog.i(et_inputusername.getText().toString().trim());
                startActivityForResult(data, 5);
                break;
            case R.id.btn_surtransfer:
                String useruid = et_inputusername.getText().toString().trim();
                Log.i("tag", useruid);
                if (!TextUtils.isEmpty(useruid)) {
                    getProxyInfo(useruid);
                }


                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == RESULT_OK) {
            String memberId = data.getStringExtra("position");// -1
            if (!TextUtils.isEmpty(memberId) && Integer.parseInt(memberId) >= 0) {
                Log.i("tag", memberId);
                et_inputusername.setText(memberId);
            } else { // 如果没有选择
                et_inputusername.setText("");
            }


        }
    }


    @Override
    protected void initData() {

    }


    private void getProxyInfo(String memberId) {
        RetrofitServiceManager.getInstance().create(ApiServer.class)
                .getProxyInfo(memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<>(new ResponseCallBack<ProxyInfo>() {
                    @Override
                    public void onSuccess(ProxyInfo proxyInfo) {
                        Log.i("tag", proxyInfo.toString());
                        if (proxyInfo != null) {
                            Intent intent = new Intent(mContext,OfflineTransferAgainActivity.class);
                            intent.putExtra("proxyInfo", proxyInfo);
                            Log.i("tag",proxyInfo.toString());
                            ActivityUtils.startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.showShort("收入款或者uid错误！");
                        }


                    }

                    @Override
                    public void onFault(int code, String errorMsg) {
                        ToastUtils.showShort(code + errorMsg);
                    }
                }));


    }


}