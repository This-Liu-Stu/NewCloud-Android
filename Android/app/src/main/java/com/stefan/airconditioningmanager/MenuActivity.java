package com.stefan.airconditioningmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.com.newland.nle_sdk.responseEntity.DeviceState;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends Activity {
    private Handler handler;

    private EditText EquipmentID;           //设备id
    private Button ConfirmDevice;           //确认id
    private TextView Isonline;              //在线状态
    private Button GetStatus;               //获取状态.
    private TextView CurrentWindtran;           //当前风向
    private TextView CurrentWindSpeed;           //当前风速
    private TextView CurrentAirQuality;           //当前空气质量
    private TextView CurrentHumidity;           //当前湿度
    private TextView CurrentTemperature;           //当前温度
    private TextView CurrentLight;           //当前光照
    private double temWindtran;                     //缓存传感器数据
    private double temWindSpeed;
    private double temAirQuality;
    private double temHumidity;
    private double temTemperature;
    private double temLight;

    private Button OpenRedWarning, CloseRedWarning;    //开关红色报警
    private Button OpenYellowWarning, CloseYellowWarning;        //开关黄色报警
    private Button OpenAirControl, CloseAirControl;    //开关空气质量控制器
    private Button OpenWindWarning, CloseWindWarning;        //开关大风预警

    private TextView[] sensorValues;        //传感器数值
    private Button[] alarmButtons;          //警报灯按钮
    private String[] sensorNames = {"Windtran", "WindSpeed", "AirQuality", "Humidity", "Temperature", "Light"};

    private NetWorkBusiness netWorkBusiness;
    private String accessToken;
    private String deviceID = "915638";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getWindtran();
                getWindSpeed();
                getAirQuality();
                getHumidity();
                getTemperature();
                getLight();

                handler.postDelayed(this, 5000); // 每隔5秒获取一次传感器数据
            }
        }, 0);

        GetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取状态.
                GetDeviceIsOnLine();
            }
        });


        OpenRedWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control(deviceID, "RedWarning", 1);  //开红色警报
            }
        });
        CloseRedWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control(deviceID, "RedWarning", 0);  //关红色警报.
            }
        });
        OpenYellowWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control(deviceID, "YellowWarning", 1);   //开黄色报警
            }
        });
        CloseYellowWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control(deviceID, "YellowWarning", 0);  //关黄色报警
            }
        });
        OpenAirControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control(deviceID, "AirControl", 1);   //开空气质量
            }
        });
        CloseAirControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control(deviceID, "AirControl", 0);  //关空气质量
            }
        });
        OpenWindWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control(deviceID, "WindWarning", 1);   //开大风预警
            }
        });
        CloseWindWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control(deviceID, "WindWarning", 0);  //关大风预警
            }
        });

        // TODO: 2020-07-19 确定设备id
        ConfirmDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deviceID = EquipmentID.getText().toString();
            }
        });

    }


    /**
     * 获取风向的方法
     */
    private void getWindtran() {
        netWorkBusiness.getSensor(deviceID, "Windtran", new NCallBack<BaseResponseEntity<SensorInfo>>() {
            @Override
            public void onResponse(final Call<BaseResponseEntity<SensorInfo>> call, final Response<BaseResponseEntity<SensorInfo>> response) {
                BaseResponseEntity baseResponseEntity = response.body();
                if (baseResponseEntity != null) {
                    //获取到了内容,使用json解析.
                    //JSON 是一种文本形式的数据交/。：
                    // 换格式，它比XML更轻量、比二进制容易阅读和编写，调式也更加方便;解析和生成的方式很多，Java中最常用的类库有：JSON-Java、Gson、Jackson、FastJson等
                    final Gson gson = new Gson();
                    JSONObject jsonObject = null;
                    String msg = gson.toJson(baseResponseEntity);
                    try {
                        jsonObject = new JSONObject(msg);   //解析数据.

                        JSONObject resultObj = (JSONObject) jsonObject.get("ResultObj");
                        String aaa = resultObj.getString("Value");
                        temWindtran = Double.valueOf(aaa).intValue();
                        CurrentWindtran.setText(String.valueOf(temWindtran));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onResponse(BaseResponseEntity<SensorInfo> response) {

            }

            public void onFailure(final Call<BaseResponseEntity<SensorInfo>> call, final Throwable t) {
            }
        });
    }

    /**
     * 获取风速的方法
     */
    private void getWindSpeed() {
        netWorkBusiness.getSensor(deviceID, "WindSpeed", new NCallBack<BaseResponseEntity<SensorInfo>>() {
            @Override
            public void onResponse(final Call<BaseResponseEntity<SensorInfo>> call, final Response<BaseResponseEntity<SensorInfo>> response) {
                BaseResponseEntity baseResponseEntity = response.body();
                if (baseResponseEntity != null) {
                    //获取到了内容,使用json解析.
                    //JSON 是一种文本形式的数据交/。：
                    // 换格式，它比XML更轻量、比二进制容易阅读和编写，调式也更加方便;解析和生成的方式很多，Java中最常用的类库有：JSON-Java、Gson、Jackson、FastJson等
                    final Gson gson = new Gson();
                    JSONObject jsonObject = null;
                    String msg = gson.toJson(baseResponseEntity);
                    try {
                        jsonObject = new JSONObject(msg);   //解析数据.

                        JSONObject resultObj = (JSONObject) jsonObject.get("ResultObj");
                        String aaa = resultObj.getString("Value");
                        temWindSpeed = Double.valueOf(aaa).intValue();
                        CurrentWindSpeed.setText(String.valueOf(temWindSpeed));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onResponse(BaseResponseEntity<SensorInfo> response) {

            }

            public void onFailure(final Call<BaseResponseEntity<SensorInfo>> call, final Throwable t) {
            }
        });
    }

    /**
     * 获取空气质量的方法
     */
    private void getAirQuality() {
        netWorkBusiness.getSensor(deviceID, "AirQuality", new NCallBack<BaseResponseEntity<SensorInfo>>() {
            @Override
            public void onResponse(final Call<BaseResponseEntity<SensorInfo>> call, final Response<BaseResponseEntity<SensorInfo>> response) {
                BaseResponseEntity baseResponseEntity = response.body();
                if (baseResponseEntity != null) {
                    //获取到了内容,使用json解析.
                    //JSON 是一种文本形式的数据交/。：
                    // 换格式，它比XML更轻量、比二进制容易阅读和编写，调式也更加方便;解析和生成的方式很多，Java中最常用的类库有：JSON-Java、Gson、Jackson、FastJson等
                    final Gson gson = new Gson();
                    JSONObject jsonObject = null;
                    String msg = gson.toJson(baseResponseEntity);
                    try {
                        jsonObject = new JSONObject(msg);   //解析数据.

                        JSONObject resultObj = (JSONObject) jsonObject.get("ResultObj");
                        String aaa = resultObj.getString("Value");
                        temAirQuality = Double.valueOf(aaa).intValue();
                        CurrentAirQuality.setText(String.valueOf(temAirQuality));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onResponse(BaseResponseEntity<SensorInfo> response) {

            }

            public void onFailure(final Call<BaseResponseEntity<SensorInfo>> call, final Throwable t) {
            }
        });
    }

    /**
     * 获取湿度的方法
     */
    private void getHumidity() {
        netWorkBusiness.getSensor(deviceID, "Humidity", new NCallBack<BaseResponseEntity<SensorInfo>>() {
            @Override
            public void onResponse(final Call<BaseResponseEntity<SensorInfo>> call, final Response<BaseResponseEntity<SensorInfo>> response) {
                BaseResponseEntity baseResponseEntity = response.body();
                if (baseResponseEntity != null) {
                    //获取到了内容,使用json解析.
                    //JSON 是一种文本形式的数据交/。：
                    // 换格式，它比XML更轻量、比二进制容易阅读和编写，调式也更加方便;解析和生成的方式很多，Java中最常用的类库有：JSON-Java、Gson、Jackson、FastJson等
                    final Gson gson = new Gson();
                    JSONObject jsonObject = null;
                    String msg = gson.toJson(baseResponseEntity);
                    try {
                        jsonObject = new JSONObject(msg);   //解析数据.

                        JSONObject resultObj = (JSONObject) jsonObject.get("ResultObj");
                        String aaa = resultObj.getString("Value");
                        temHumidity = Double.valueOf(aaa).intValue();
                        CurrentHumidity.setText(String.valueOf(temHumidity));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onResponse(BaseResponseEntity<SensorInfo> response) {

            }

            public void onFailure(final Call<BaseResponseEntity<SensorInfo>> call, final Throwable t) {
            }
        });
    }

    /**
     * 获取温度的方法
     */
    private void getTemperature() {
        netWorkBusiness.getSensor(deviceID, "Temperature", new NCallBack<BaseResponseEntity<SensorInfo>>() {
            @Override
            public void onResponse(final Call<BaseResponseEntity<SensorInfo>> call, final Response<BaseResponseEntity<SensorInfo>> response) {
                BaseResponseEntity baseResponseEntity = response.body();
                if (baseResponseEntity != null) {
                    //获取到了内容,使用json解析.
                    //JSON 是一种文本形式的数据交/。：
                    // 换格式，它比XML更轻量、比二进制容易阅读和编写，调式也更加方便;解析和生成的方式很多，Java中最常用的类库有：JSON-Java、Gson、Jackson、FastJson等
                    final Gson gson = new Gson();
                    JSONObject jsonObject = null;
                    String msg = gson.toJson(baseResponseEntity);
                    try {
                        jsonObject = new JSONObject(msg);   //解析数据.

                        JSONObject resultObj = (JSONObject) jsonObject.get("ResultObj");
                        String aaa = resultObj.getString("Value");
                        temTemperature = Double.valueOf(aaa).intValue();
                        CurrentTemperature.setText(String.valueOf(temTemperature));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onResponse(BaseResponseEntity<SensorInfo> response) {

            }

            public void onFailure(final Call<BaseResponseEntity<SensorInfo>> call, final Throwable t) {
            }
        });
    }

    /**
     * 获取温度的方法
     */
    private void getLight() {
        netWorkBusiness.getSensor(deviceID, "Light", new NCallBack<BaseResponseEntity<SensorInfo>>() {
            @Override
            public void onResponse(final Call<BaseResponseEntity<SensorInfo>> call, final Response<BaseResponseEntity<SensorInfo>> response) {
                BaseResponseEntity baseResponseEntity = response.body();
                if (baseResponseEntity != null) {
                    //获取到了内容,使用json解析.
                    //JSON 是一种文本形式的数据交/。：
                    // 换格式，它比XML更轻量、比二进制容易阅读和编写，调式也更加方便;解析和生成的方式很多，Java中最常用的类库有：JSON-Java、Gson、Jackson、FastJson等
                    final Gson gson = new Gson();
                    JSONObject jsonObject = null;
                    String msg = gson.toJson(baseResponseEntity);
                    try {
                        jsonObject = new JSONObject(msg);   //解析数据.

                        JSONObject resultObj = (JSONObject) jsonObject.get("ResultObj");
                        String aaa = resultObj.getString("Value");
                        temLight = Double.valueOf(aaa).intValue();
                        CurrentLight.setText(String.valueOf(temLight));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onResponse(BaseResponseEntity<SensorInfo> response) {

            }

            public void onFailure(final Call<BaseResponseEntity<SensorInfo>> call, final Throwable t) {
            }
        });
    }

    /**
     * 获取设备在线信息
     */
    private void GetDeviceIsOnLine() {
        netWorkBusiness.getBatchOnLine(deviceID, new NCallBack<BaseResponseEntity<List<DeviceState>>>() {
            @Override
            protected void onResponse(BaseResponseEntity<List<DeviceState>> response) {

            }

            @Override
            public void onResponse(final Call<BaseResponseEntity<List<DeviceState>>> call, final Response<BaseResponseEntity<List<DeviceState>>> response) {
                BaseResponseEntity baseResponseEntity = response.body();
                if (baseResponseEntity != null) {
                    //获取到了内容,使用json解析.
                    //JSON 是一种文本形式的数据交换格式，它比XML更轻量、比二进制容易阅读和编写，调式也更加方便;解析和生成的方式很多，Java中最常用的类库有：JSON-Java、Gson、Jackson、FastJson等
                    boolean value = false;
                    final Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = null;
                        String msg = gson.toJson(baseResponseEntity);
                        jsonObject = new JSONObject(msg);   //解析数据.
                        JSONArray resultObj = (JSONArray) jsonObject.get("ResultObj");
                        value = resultObj.getJSONObject(0).getBoolean("IsOnline");
                        if (value) {
                            Isonline.setText("在线");
                        } else {
                            Isonline.setText("离线");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 进行一些初始化操作
     */
    private void init() {
        EquipmentID = findViewById(R.id.equipmentID);   //设备id
        Isonline = findViewById(R.id.isonline);   //状态
        GetStatus = findViewById(R.id.getStatus);   //获取状态..

        CurrentWindtran = findViewById(R.id.currentWindtran);    //当前风向
        CurrentWindSpeed = findViewById(R.id.currentWindSpeed);    //当前风速
        CurrentAirQuality = findViewById(R.id.currentAirQuality);    //当前空气质量
        CurrentHumidity = findViewById(R.id.currentHumidity);    //当前湿度
        CurrentTemperature = findViewById(R.id.currentTemperature);    //当前温度
        CurrentLight = findViewById(R.id.currentLight);    //当前光照

        OpenRedWarning = findViewById(R.id.openRedWarning);  //开灯
        CloseRedWarning = findViewById(R.id.closeRedWarning);    //关灯
        OpenYellowWarning = findViewById(R.id.openYellowWarning);  //开灯
        CloseYellowWarning = findViewById(R.id.closeYellowWarning);    //关灯
        OpenAirControl = findViewById(R.id.openAirControl);  //开灯
        CloseAirControl = findViewById(R.id.closeAirControl);    //关灯
        OpenWindWarning = findViewById(R.id.openWindWarning);  //开灯
        CloseWindWarning = findViewById(R.id.closeWindWarning);    //关灯

        ConfirmDevice = findViewById(R.id.confirmDevice);
        Bundle bundle = getIntent().getExtras();
        accessToken = bundle.getString("accessToken");   //获得传输秘钥
        netWorkBusiness = new NetWorkBusiness(accessToken, "http://api.nlecloud.com:80/");   //进行登录连接
    }

    /**
     * 控制设备方法封装
     * Stefan注：此处可能需要根据新SDK进行微调，如果需要，请大家自行完成.
     *
     * @param id     设备ID
     * @param apiTag 标识符
     * @param value  控制值
     */
    public void control(String id, String apiTag, Object value) {
        netWorkBusiness.control(id, apiTag, value, new Callback<BaseResponseEntity>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponseEntity> call, @NonNull Response<BaseResponseEntity> response) {
                BaseResponseEntity<User> baseResponseEntity = response.body();
                if (baseResponseEntity == null) {
                    Toast.makeText(MenuActivity.this, "请求内容为空", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "请求出错 " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}