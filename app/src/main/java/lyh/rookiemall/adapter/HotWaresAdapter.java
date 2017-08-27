package lyh.rookiemall.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import lyh.rookiemall.R;
import lyh.rookiemall.bean.ShoppingCart;
import lyh.rookiemall.bean.Wares;
import lyh.rookiemall.utils.CartProvider;
import lyh.rookiemall.utils.ToastUtils;


/**
 * Created by 刘营海 on 2017/8/6.
 */
public class HotWaresAdapter extends RecyclerView.Adapter<HotWaresAdapter.ViewHolder>  {


    protected int layoutResId=R.layout.template_hot_wares;
    private List<Wares> mDatas;
    private Context mcontext;
    private LayoutInflater mInflater;

    CartProvider cartProvider;
    public HotWaresAdapter(List<Wares> wares,Context context){
        mDatas = wares;
        mcontext=context;
        cartProvider=new CartProvider(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(layoutResId,null);
        return new ViewHolder(view);

    }
    public Wares getItem(int position) {
        if (position >= mDatas.size()) return null;
        return mDatas.get(position);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Wares wares = getData(position);
        holder.draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.textTitle.setText(wares.getName());
        holder.textPrice.setText("￥"+wares.getPrice());
        if (holder.btnbuy!=null){
            holder.btnbuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartProvider.put(wares);
                    ToastUtils.show(mcontext,"已添加到购物车");
                }
            });
        }
    }
    public Wares getData(int position){
        return mDatas.get(position);
    }
    public List<Wares> getDatas(){
        return  mDatas;
    }
    public void clearData(){

        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }
    public void clear(){
        int itemCount = mDatas.size();
        mDatas.clear();
        this.notifyItemRangeRemoved(0,itemCount);
    }
    public void addData(List<Wares> datas){

        addData(0,datas);
    }
    public void  resetLayout(int layoutId){
        this.layoutResId  = layoutId;
        notifyItemRangeChanged(0,getDatas().size());


    }
    public void refreshData(List<Wares> list){
        if(list !=null && list.size()>0){
            clear();
            int size = list.size();
            for (int i=0;i<size;i++){
                mDatas.add(i,list.get(i));
                notifyItemInserted(i);
            }

        }
    }
    public void loadMoreData(List<Wares> list){
        if(list !=null && list.size()>0){

            int size = list.size();
            int begin = mDatas.size();
            for (int i=0;i<size;i++){
                mDatas.add(list.get(i));
                notifyItemInserted(i+begin);
            }

        }

    }
    public void addData(int position,List<Wares> datas){

        if(datas !=null && datas.size()>0) {

            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }
    }


    @Override
    public int getItemCount() {
        if(mDatas!=null && mDatas.size()>0)
            return mDatas.size();

        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView draweeView;
        TextView textTitle;
        TextView textPrice;
        Button btnbuy;
        public ViewHolder(View itemView) {
            super(itemView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
            textTitle= (TextView) itemView.findViewById(R.id.text_title);
            textPrice= (TextView) itemView.findViewById(R.id.text_price);
            btnbuy= (Button) itemView.findViewById(R.id.btn_add );
        }
    }
}
