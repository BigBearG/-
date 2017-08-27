package lyh.rookiemall;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.bigkoo.pickerview.OptionsPickerView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import lyh.rookiemall.city.XmlParserHandler;
import lyh.rookiemall.city.model.CityModel;
import lyh.rookiemall.city.model.DistrictModel;
import lyh.rookiemall.city.model.ProvinceModel;
import lyh.rookiemall.http.OkHttpHelper;
import lyh.rookiemall.http.SpotsCallBack;
import lyh.rookiemall.msg.BaseRespMsg;
import lyh.rookiemall.weiget.ClearEditText;
import lyh.rookiemall.weiget.CnToolbar;
import okhttp3.Response;


public class AddressAddActivity extends BaseActivity {


    private OptionsPickerView mCityPikerView; //https://github.com/saiwu-bigkoo/Android-PickerView


    @ViewInject(R.id.txt_address)
    private TextView mTxtAddress;

    @ViewInject(R.id.edittxt_consignee)
    private ClearEditText mEditConsignee;

    @ViewInject(R.id.edittxt_phone)
    private ClearEditText mEditPhone;

    @ViewInject(R.id.edittxt_add)
    private ClearEditText mEditAddr;

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolBar;


    private List<ProvinceModel> mProvinces;
    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();


    private OkHttpHelper mHttpHelper= OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        x.view().inject(this);
        initToolbar();
        init();
    }

    private void initToolbar(){

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                createAddress();
            }
        });

    }
    private void init() {

        initProvinceDatas();
        mCityPikerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String addresss = mProvinces.get(options1).getName() +"  "
                        + mCities.get(options1).get(options2)+"  "
                        + mDistricts.get(options1).get(options2).get(options3);
                mTxtAddress.setText(addresss);
            }
        }).setTitleText("城市选择").setCyclic(false, false, false).build();
        mCityPikerView.setPicker((ArrayList) mProvinces,mCities,mDistricts);
    }



    protected void initProvinceDatas()
    {

        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            mProvinces = handler.getDataList();

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }

        if(mProvinces !=null){

            for (ProvinceModel p :mProvinces){

               List<CityModel> cities =  p.getCityList();
               ArrayList<String> cityStrs = new ArrayList<>(cities.size()); //城市List


               for (CityModel c :cities){

                   cityStrs.add(c.getName()); // 把城市名称放入 cityStrs


                   ArrayList<ArrayList<String>> dts = new ArrayList<>(); // 地区 List

                   List<DistrictModel> districts = c.getDistrictList();
                   ArrayList<String> districtStrs = new ArrayList<>(districts.size());

                   for (DistrictModel d : districts){
                       districtStrs.add(d.getName()); // 把城市名称放入 districtStrs
                   }
                   dts.add(districtStrs);


                  mDistricts.add(dts);
               }

               mCities.add(cityStrs); // 组装城市数据

            }
        }



    }



    @Event(R.id.ll_city_picker)
    public void showCityPickerView(View view){
        mCityPikerView.show();
    }


    public void createAddress(){


        String consignee = mEditConsignee.getText().toString();
        String phone = mEditPhone.getText().toString();
        String address = mTxtAddress.getText().toString() + mEditAddr.getText().toString();


        Map<String,String> params = new HashMap<>(1);
        params.put("user_id",RookmailApplication.getInstance().getUser().getId().toString());
        params.put("consignee",consignee);
        params.put("phone",phone);
        params.put("addr",address);
        params.put("zip_code","000000");

        mHttpHelper.post(Contants.API.ADDRESS_CREATE, params, new SpotsCallBack<BaseRespMsg>(this) {


            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS){
                    setResult(RESULT_OK);
                    finish();

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }





}
