package lyh.rookiemall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

import lyh.rookiemall.LoginActivity;
import lyh.rookiemall.RookmailApplication;
import lyh.rookiemall.bean.User;


public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater,container,savedInstanceState);
        x.view().inject(this,view);
        initToolBar();
        init();
        return view;
    }
    public void  initToolBar(){
    }
    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();
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
