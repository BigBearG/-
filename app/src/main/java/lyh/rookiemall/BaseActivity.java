package lyh.rookiemall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import lyh.rookiemall.bean.User;


/**
 * Created by 刘营海 on 2017/8/17.
 */
public class BaseActivity extends AppCompatActivity {



    public void startActivity(Intent intent,boolean isNeedLogin){


        if(isNeedLogin){

            User user =RookmailApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            }
            else{

                RookmailApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this
                        , LoginActivity.class);
                super.startActivity(intent);

            }

        }
        else{
            super.startActivity(intent);
        }

    }
}
