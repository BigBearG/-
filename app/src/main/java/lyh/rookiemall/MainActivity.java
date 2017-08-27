package lyh.rookiemall;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import lyh.rookiemall.bean.Tab;
import lyh.rookiemall.fragment.CartFragment;
import lyh.rookiemall.fragment.CategoryFragment;
import lyh.rookiemall.fragment.HomeFragment;
import lyh.rookiemall.fragment.HotFragment;
import lyh.rookiemall.fragment.MineFragment;
import lyh.rookiemall.weiget.FragmentTabHost;
/**
 * Created by 刘营海 on 2017/8/1.
 */
public class MainActivity extends AppCompatActivity {
    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private List<Tab> mTabs=new ArrayList<>(5);
    private CartFragment mcartFragment;
    private EventHandler eventHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 如果希望在读取通信录的时候提示用户，可以添加下面的代码，并且必须在其他代码调用之前，否则不起作用；如果没这个需求，可以不加这行代码
//        SMSSDK.setAskPermisionOnReadContact(boolShowInDialog)

        // 创建EventHandler对象
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (data instanceof Throwable) {
                    Throwable throwable = (Throwable)data;
                    String msg = throwable.getMessage();
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        // 处理你自己的逻辑
                    }
                }
            }
        };

        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
        initTab();
    }
    private void initTab() {
        Tab tab_home=new Tab(R.string.home,R.drawable.selector_icon_home,HomeFragment.class);
        Tab tab_hot=new Tab(R.string.hot,R.drawable.selector_icon_hot,HotFragment.class);
        Tab tab_category=new Tab(R.string.catagory,R.drawable.selector_icon_category, CategoryFragment.class);
        Tab tab_cart=new Tab(R.string.cart,R.drawable.selector_icon_cart,CartFragment.class);
        Tab tab_mine=new Tab(R.string.mine,R.drawable.selector_icon_mine,MineFragment.class);
        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);
        mInflater=LayoutInflater.from(this);
        mTabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        for (Tab tab:mTabs){
            TabHost.TabSpec tabSpec=mTabHost.newTabSpec(getString(tab.getTitle()));
            View  view=buildindicator(tab);
            tabSpec.setIndicator(view);
            mTabHost.addTab(tabSpec,tab.getFragment(),null);
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                    refshData();
            }
        });
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
    }
    private void refshData(){
        if (mcartFragment==null){
            Fragment fragment=getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (fragment!=null){
                mcartFragment= (CartFragment) fragment;
                mcartFragment.refData();
            }
        }else {
            mcartFragment.refData();
        }
    }
    private View buildindicator(Tab tab){
        View view=mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img= (ImageView) view.findViewById(R.id.icon_tab);
        TextView text= (TextView)view.findViewById(R.id.txt_indicator);
        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
