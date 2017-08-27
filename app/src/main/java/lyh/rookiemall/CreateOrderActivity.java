package lyh.rookiemall;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import c.b.BP;
import c.b.PListener;
import lyh.rookiemall.adapter.FullyLinearLayoutManager;
import lyh.rookiemall.adapter.WareOrderAdapter;
import lyh.rookiemall.bean.Charge;
import lyh.rookiemall.bean.ShoppingCart;
import lyh.rookiemall.http.OkHttpHelper;
import lyh.rookiemall.http.SpotsCallBack;
import lyh.rookiemall.msg.BaseRespMsg;
import lyh.rookiemall.msg.CreateOrderRespMsg;
import lyh.rookiemall.utils.CartProvider;
import lyh.rookiemall.utils.JSONUtil;
import okhttp3.Response;


public class CreateOrderActivity extends BaseActivity  implements View.OnClickListener {

    private static final boolean CHANNEL_WECHAT = false;
    /**
     * 微信支付渠道
     */
    private static final boolean CHANNEL_ALIPAY = true;
    /**
     * 支付支付渠道
     */


    @ViewInject(R.id.txt_order)
    private TextView txtOrder;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;


    @ViewInject(R.id.rl_alipay)
    private RelativeLayout mLayoutAlipay;

    @ViewInject(R.id.rl_wechat)
    private RelativeLayout mLayoutWechat;

    @ViewInject(R.id.rb_alipay)
    private RadioButton mRbAlipay;

    @ViewInject(R.id.rb_webchat)
    private RadioButton mRbWechat;
    @ViewInject(R.id.btn_createOrder)
    private Button mBtnCreateOrder;

    @ViewInject(R.id.txt_total)
    private TextView mTxtTotal;


    private CartProvider cartProvider;

    private WareOrderAdapter mAdapter;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private boolean payChannel =CHANNEL_ALIPAY;
    private String orderNum;
    private float amount;


    private HashMap<Boolean,RadioButton> channels = new HashMap<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        x.view().inject(this);
        showData();
        init();
    }
    private void init(){
        channels.put(CHANNEL_ALIPAY,mRbWechat);
        mLayoutAlipay.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);
        amount = mAdapter.getTotalPrice();
        mTxtTotal.setText("应付款： ￥"+amount);
    }



    public void showData(){

        cartProvider = new CartProvider(this);
        mAdapter = new WareOrderAdapter(this,cartProvider.getAll());
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onClick(View v) {
        if (v.getTag().toString().equals("true")){
            selectPayChannle(true);
        }else {
            selectPayChannle(false);
        }

    }


    public void selectPayChannle(Boolean paychannel){


        for (Map.Entry<Boolean,RadioButton> entry:channels.entrySet()){

            payChannel = paychannel;
            RadioButton rb = entry.getValue();
            if(entry.getKey().equals(paychannel.toString())){

                boolean isCheck = rb.isChecked();
                rb.setChecked(!isCheck);

            }
            else
                rb.setChecked(false);
        }


    }


    @Event(R.id.btn_createOrder)
    public void createNewOrder(View view){

        postNewOrder();
    }


    private void postNewOrder(){
    }


    private void openPaymentActivity(Charge charge){
    /* 第4个参数为true时调用支付宝支付，为false时调用微信支付
                */
        BP.pay("商品名称", "商品描述", 0.02, true, new PListener() {
            @Override
            public void orderId(String s) {

            }

            @Override
            public void succeed() {

            }

            @Override
            public void fail(int i, String s) {

            }

            @Override
            public void unknow() {

            }
        });
//        startActivityForResult(intent, Contants.REQUEST_CODE_PAYMENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Contants.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                if (result.equals("success"))
                    changeOrderStatus(1);
                else if (result.equals("fail"))
                    changeOrderStatus(-1);
                 else if (result.equals("cancel"))
                    changeOrderStatus(-2);
                 else
                    changeOrderStatus(0);

            /* 处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed
             *
             * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
             */
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

            }
        }
    }



    private void changeOrderStatus(final int status){

        Map<String,Object> params = new HashMap<>(5);
        params.put("order_num",orderNum);
        params.put("status",status+"");

    }


    private void toPayResultActivity(int status){

        Intent intent = new Intent(this,PayResultActivity.class);
        intent.putExtra("status",status);

        startActivity(intent);
        this.finish();

    }


    class WareItem {
        private  Long ware_id;
        private  int amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }


}
