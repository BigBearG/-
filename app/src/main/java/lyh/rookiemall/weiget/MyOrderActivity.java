package lyh.rookiemall.weiget;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lyh.rookiemall.BaseActivity;
import lyh.rookiemall.Contants;
import lyh.rookiemall.OrderDetailActivity;
import lyh.rookiemall.R;
import lyh.rookiemall.RookmailApplication;
import lyh.rookiemall.adapter.BaseAdapter;
import lyh.rookiemall.adapter.CardViewtemDecortion;
import lyh.rookiemall.adapter.MyOrderAdapter;
import lyh.rookiemall.bean.Order;
import lyh.rookiemall.http.OkHttpHelper;
import lyh.rookiemall.http.SpotsCallBack;
import okhttp3.Response;


public class MyOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {



    public static final int STATUS_ALL=1000;
    public static final int STATUS_SUCCESS=1; //支付成功的订单
    public static final int STATUS_PAY_FAIL=-2; //支付失败的订单
    public static final int STATUS_PAY_WAIT=0; //：待支付的订单
    private int status = STATUS_ALL;


    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar;


    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;


    private MyOrderAdapter mAdapter;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        x.view().inject(this);

        initToolBar();
        initTab();



        getOrders();
    }



    private void initToolBar(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTab(){


        TabLayout.Tab tab= mTablayout.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        mTablayout.addTab(tab);


        tab= mTablayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        mTablayout.addTab(tab);


        mTablayout.setOnTabSelectedListener(this);


    }




    private void getOrders(){


        Long userId = RookmailApplication.getInstance().getUser().getId();

        Map<String, String> params = new HashMap<>();

        params.put("user_id",userId.toString());
        params.put("status", String.valueOf(status));


        okHttpHelper.get(Contants.API.ORDER_LIST, params, new SpotsCallBack<List<Order>>(this) {
            @Override
            public void onSuccess(Response response, List<Order> orders) {
                showOrders(orders);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }



    private void showOrders(List<Order> orders){

        if(mAdapter ==null) {
            mAdapter = new MyOrderAdapter(this,orders);
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview.addItemDecoration(new CardViewtemDecortion());

            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    toDetailActivity(position);
                }
            });
        }
        else{
            mAdapter.refreshData(orders);
            mRecyclerview.setAdapter(mAdapter);
        }
    }


    private void toDetailActivity(int position){

        Intent intent = new Intent(this,OrderDetailActivity.class);

        Order order = mAdapter.getItem(position);
        intent.putExtra("order",order);
        startActivity(intent,true);
    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        status = (int) tab.getTag();
        getOrders();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
