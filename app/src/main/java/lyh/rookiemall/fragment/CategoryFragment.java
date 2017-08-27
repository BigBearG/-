package lyh.rookiemall.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import lyh.rookiemall.Contants;
import lyh.rookiemall.R;
import lyh.rookiemall.adapter.BaseAdapter;
import lyh.rookiemall.adapter.CategoryAdapter;
import lyh.rookiemall.adapter.DividerGridItemDecoration;
import lyh.rookiemall.adapter.WaresAdapter;
import lyh.rookiemall.bean.Banner;
import lyh.rookiemall.bean.Category;
import lyh.rookiemall.bean.Page;
import lyh.rookiemall.bean.Wares;
import lyh.rookiemall.http.BaseCallback;
import lyh.rookiemall.http.OkHttpHelper;
import lyh.rookiemall.http.SpotsCallBack;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 刘营海 on 2017/8/2.
 */
public class CategoryFragment extends Fragment {
    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerviewWares;
    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLaout;
    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;
    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdatper;
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();
    private int currPage=1;
    private int totalPage=1;
    private int pageSize=10;
    private long category_id=0;
    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;
    private int state=STATE_NORMAL;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_category,container,false);
        x.view().inject(this,view);
        requestCategoryData();
        requestBannerData();
        initRefreshLayout();
        return  view;
    }
    private  void initRefreshLayout(){
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(currPage <=totalPage)
                    loadMoreData();
                else{
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }
    private  void refreshData(){
        currPage =1;
        state=STATE_REFREH;
        requestWares(category_id);

    }
    private void loadMoreData(){
        currPage =currPage+1;
        state = STATE_MORE;
        requestWares(category_id);
    }
    private  void requestCategoryData(){
        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);

                if(categories !=null && categories.size()>0)
                    category_id = categories.get(0).getId();
                    requestWares(category_id);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    private  void showCategoryData(List<Category> categories){
        mCategoryAdapter = new CategoryAdapter(getContext(),categories);
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category = mCategoryAdapter.getItem(position);
                category_id = category.getId();
                currPage=1;
                state=STATE_NORMAL;
                requestWares(category_id);


            }
        });
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }
    private void requestBannerData( ) {
        String url = Contants.API.BANNER+"?type=1";
        mHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()){
            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                showSliderViews(banners);
            }
            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    private void showSliderViews(List<Banner> banners){
        if(banners !=null){
            for (Banner banner : banners){
                DefaultSliderView sliderView = new DefaultSliderView(this.getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(sliderView);

            }
        }
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        mSliderLayout.setDuration(3000);
    }



    private void requestWares(long categoryId){
        String url = Contants.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+currPage+"&pageSize="+pageSize;
        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
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
            public void onSuccess(Response response, Page<Wares> waresPage) {
                currPage = waresPage.getCurrentPage();
                totalPage =waresPage.getTotalPage();
                showWaresData(waresPage.getList());
            }
            @Override
            public void onError(Response response, int code, Exception e) {
            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }
    private  void showWaresData(List<Wares> wares){
        switch (state){
            case  STATE_NORMAL:
                if(mWaresAdatper ==null) {
                    mWaresAdatper = new WaresAdapter(getContext(), wares);
                    mRecyclerviewWares.setAdapter(mWaresAdatper);
                    mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }
                else{
                    mWaresAdatper.clear();
                    mWaresAdatper.addData(wares);
                }
                break;
            case STATE_REFREH:
                mWaresAdatper.clear();
                mWaresAdatper.addData(wares);
                mRecyclerviewWares.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;
            case STATE_MORE:
                mWaresAdatper.addData(mWaresAdatper.getDatas().size(),wares);
                mRecyclerviewWares.scrollToPosition(mWaresAdatper.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }
    }
}



