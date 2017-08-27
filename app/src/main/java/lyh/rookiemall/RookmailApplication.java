package lyh.rookiemall;

import android.content.Context;
import android.content.Intent;


import org.xutils.x;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobApplication;


import c.b.BP;
import lyh.rookiemall.bean.User;
import lyh.rookiemall.utils.UserLocalData;

/**
 * Created by 刘营海 on 2017/8/7.
 */

public class RookmailApplication extends MobApplication {
    private User user;

    private static  RookmailApplication mInstance;

    public static  RookmailApplication getInstance(){
        return  mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initUser();
        BP.init("e0ef8a5377710842d0dbe7ece4239cd1");
        x.Ext.init(this);
        x.Ext.setDebug(true);
        Fresco.initialize(this);
    }

    private void initUser(){

        this.user = UserLocalData.getUser(this);
    }


    public User getUser(){

        return user;
    }


    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);


    }


    public String getToken(){

        return  UserLocalData.getToken(this);
    }



    private  Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){
        context.startActivity(intent);
        this.intent =null;
    }

}
