package lyh.rookiemall.bean;

import java.io.Serializable;

/**
 * Created by 刘营海 on 2017/8/5.
 */
public class BaseBean implements Serializable {


    protected   long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
