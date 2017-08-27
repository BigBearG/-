package lyh.rookiemall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import lyh.rookiemall.Contants;
import lyh.rookiemall.R;
import lyh.rookiemall.WareDetailActivity;
import lyh.rookiemall.adapter.BaseAdapter;
import lyh.rookiemall.adapter.HWAdatper;
import lyh.rookiemall.bean.Page;

import lyh.rookiemall.bean.Wares;
import lyh.rookiemall.http.OkHttpHelper;
import lyh.rookiemall.http.SpotsCallBack;
import okhttp3.Response;

/**
 * Created by 刘营海 on 2017/8/2.
 */
public class HotFragment extends Fragment{
    private OkHttpHelper httpHelper=OkHttpHelper.getInstance();
    private int currPage=1;
    private int totalPage=3;
    private int pageSize=10;
    private List<Wares> datas;
    private HWAdatper mAdatper;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLaout;



    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;

    private int state=STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_hot,container,false);
        x.view().inject(this,view);
        getData();
        initRefreshLayout();
        return view ;

    }
    public void initRefreshLayout(){
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (currPage<=totalPage){
                    refreshLoadMoreData();
                }else {
                    Toast.makeText(getActivity(),"没有下一页",Toast.LENGTH_SHORT).show();
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }
    public void refreshData(){
        currPage=1;
        state=STATE_REFREH;
        getData();
    }
    public void refreshLoadMoreData(){
        currPage=currPage+1;
        state=STATE_MORE;
        getData();
    }
    private void getData(){
        String url = Contants.API.WARES_HOT+"?curPage="+currPage+"&pageSize="+pageSize;
        httpHelper.get(url, new SpotsCallBack<Page<Wares>>(getContext()) {
            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                datas = waresPage.getList();
                currPage = waresPage.getCurrentPage();
                showData();
            }
            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }
    private  void showData(){
        switch (state){
            case  STATE_NORMAL:
                mAdatper = new HWAdatper(getContext(),datas);
                mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Wares wares = mAdatper.getItem(position);

                        Intent intent = new Intent(getActivity(), WareDetailActivity.class);

                        intent.putExtra(Contants.WARE,wares);
                        startActivity(intent);


                    }
                });

                mRecyclerView.setAdapter(mAdatper);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                break;

            case STATE_REFREH:
                mAdatper.clear();
                mAdatper.addData(datas);
                mRecyclerView.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;
            case STATE_MORE:
                mAdatper.addData(mAdatper.getDatas().size(),datas);
                mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }



    }


}
