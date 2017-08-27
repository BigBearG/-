package lyh.rookiemall.bean;

import java.io.Serializable;

/**
 * Created by 刘营海 on 2017/8/10.
 */
public class ShoppingCart extends Wares implements Serializable {


    private int count;
    private boolean isChecked=true;



    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }





}
