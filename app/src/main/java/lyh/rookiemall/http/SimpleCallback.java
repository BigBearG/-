package lyh.rookiemall.http;

import android.content.Context;
import android.content.Intent;

import lyh.rookiemall.LoginActivity;
import lyh.rookiemall.R;
import lyh.rookiemall.RookmailApplication;
import lyh.rookiemall.utils.ToastUtils;
import okhttp3.Request;
import okhttp3.Response;


public abstract class SimpleCallback<T> extends BaseCallback<T> {

    protected Context mContext;

    public SimpleCallback(Context context){

        mContext = context;

    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

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
