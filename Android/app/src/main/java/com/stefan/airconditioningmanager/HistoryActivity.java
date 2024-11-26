package com.stefan.airconditioningmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.newland.nle_sdk.responseEntity.SensorDataPageDTO;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Author: Stefan Charles
 * @Date: 2020-07-19
 * @Website: www.stefancharles.cn
 * @E-mail: stefancharles@qq.com
 * @Copyright: Copyright (c) 2020 Security Plus.All rights reserved.
 **/

public class HistoryActivity extends AppCompatActivity {


}