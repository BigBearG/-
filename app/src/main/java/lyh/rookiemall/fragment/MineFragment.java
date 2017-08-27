package lyh.rookiemall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.hdodenhof.circleimageview.CircleImageView;
import lyh.rookiemall.AddressListActivity;
import lyh.rookiemall.Contants;
import lyh.rookiemall.LoginActivity;
import lyh.rookiemall.R;
import lyh.rookiemall.RookmailApplication;
import lyh.rookiemall.bean.User;

/**
 * Created by 刘营海 on 2017/8/2.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;
    @ViewInject(R.id.txt_my_orders)
    private TextView mTextShell;
    @ViewInject(R.id.btn_logout)
    private Button mbtnLogout;
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_mine,container,false);
        x.view().inject(this,view);
        User user =  RookmailApplication.getInstance().getUser();
        showUser(user);
        mImageHead.setOnClickListener(this);
        mbtnLogout.setOnClickListener(this);
        mTxtUserName.setOnClickListener(this);
        mTextShell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), AddressListActivity.class);
                startActivity(intent);
            }
        });
        return  view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        User user =  RookmailApplication.getInstance().getUser();
        showUser(user);

    }
    private void showUser(User user){
        if(user!=null){
            if(!TextUtils.isEmpty(user.getLogo_url()))
                showHeadImage(user.getLogo_url());
            mTxtUserName.setText(user.getUsername());
            mbtnLogout.setVisibility(View.VISIBLE);
        }
        else {
            mTxtUserName.setText(R.string.to_login);
            mbtnLogout.setVisibility(View.GONE);
        }
    }

    private void showHeadImage(String url){

        Picasso.with(getActivity()).load(url).into(mImageHead);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_head:
            case R.id.txt_username:
                if(RookmailApplication.getInstance().getUser() == null) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivityForResult(intent, Contants.REQUEST_CODE);
                }
                break;
            case R.id.btn_logout:
                RookmailApplication.getInstance().clearUser();
                showUser(null);
                break;
        }

    }
    public void startActivity(Intent intent, boolean isNeedLogin){


        if(isNeedLogin){

            User user = RookmailApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            }
            else{
                RookmailApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);

            }

        }
        else{
            super.startActivity(intent);
        }

    }
}
