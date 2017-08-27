package lyh.rookiemall.http;

import android.content.Context;
import android.content.Intent;


import dmax.dialog.SpotsDialog;
import lyh.rookiemall.LoginActivity;
import lyh.rookiemall.R;
import lyh.rookiemall.RookmailApplication;
import lyh.rookiemall.utils.ToastUtils;
import okhttp3.Request;
import okhttp3.Response;


public abstract class SpotsCallBack<T> extends BaseCallback<T> {


    private  Context mContext;

    private  SpotsDialog mDialog;

    public SpotsCallBack(Context context){

        mContext = context;

        initSpotsDialog();
    }



    private  void initSpotsDialog(){

        mDialog = new SpotsDialog(mContext,"拼命加载中...");

    }

    public  void showDialog(){
        mDialog.show();
    }

    public  void dismissDialog(){
        mDialog.dismiss();
    }


    public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }


    @Override
    public void onFailure(Request request, Exception e) {
        dismissDialog();
    }

    @Override
    public void onBeforeRequest(Request request) {

        showDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, mContext.getString(R.string.token_error));
        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        RookmailApplication.getInstance().clearUser();
    }
}
