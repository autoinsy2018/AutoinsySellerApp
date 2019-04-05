package com.autionsy.seller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.UploadImageAdapter;
import com.autionsy.seller.constant.Constant;
import com.autionsy.seller.entity.Goods;
import com.autionsy.seller.entity.Lease;
import com.autionsy.seller.utils.OkHttp3Utils;
import com.autionsy.seller.views.GridViewInScrollView;
import com.scrat.app.selectorlibrary.ImageSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishLeaseActivity extends BaseActivity{

    @BindView(R.id.upload_image_gv)
    GridViewInScrollView upload_image_gv;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.submit_tv)
    TextView submit_tv;

    @BindView(R.id.lease_title_et)
    EditText lease_title_et;
    @BindView(R.id.lease_stall_position_et)
    EditText lease_stall_position_et;
    @BindView(R.id.lease_price_et)
    EditText lease_price_et;
    @BindView(R.id.lease_describe_et)
    EditText lease_describe_et;
    @BindView(R.id.lease_mobile_phone_num_et)
    EditText lease_mobile_phone_num_et;
    @BindView(R.id.lease_time_et)
    EditText lease_time_et;
    @BindView(R.id.acreage_et)
    EditText acreage_et;
    @BindView(R.id.lease_info_source_et)
    EditText lease_info_source_et;

    private String leaseTitle;
    private String stallPosition;
    private String leasePrice;
    private String descrihe;
    private String mobilePhoneNum;
    private String leaseTime;
    private String acreage;
    private String infoSource;

    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;
    private File file;
    private List<String> path;//路径集合

    private Lease lease;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_publish_lease);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.publish_lease_title);
        submit_tv.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back_btn,
            R.id.image_selector_layout,
            R.id.submit_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.image_selector_layout:
                ImageSelector.show(this, REQUEST_CODE_SELECT_IMG, MAX_SELECT_COUNT);
                break;
            case R.id.submit_tv:
                postAsynHttpGoods();
                break;
        }
    }

    private void postAsynHttpGoods(){
        lease = new Lease();

        leaseTitle = lease_title_et.getText().toString().trim();
        stallPosition = lease_stall_position_et.getText().toString().trim();
        leasePrice = lease_price_et.getText().toString().trim();
        descrihe = lease_describe_et.getText().toString().trim();
        mobilePhoneNum = lease_mobile_phone_num_et.getText().toString().trim();
        leaseTime = lease_time_et.getText().toString().trim();
        acreage = acreage_et.getText().toString().trim();
        infoSource = lease_info_source_et.getText().toString().trim();


        String url = Constant.HTTP_URL + "login";

        Map<String,String> map = new HashMap<>();
        map.put("loginName", leaseTitle);
        map.put("passWord", stallPosition);
        map.put("passWord", leasePrice);
        map.put("passWord", descrihe);
        map.put("passWord", mobilePhoneNum);
        map.put("passWord", leaseTime);
        map.put("passWord", acreage);
        map.put("passWord", infoSource);

        OkHttp3Utils.doPost(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responeString = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(responeString);
                            String resultCode = jsonObject.optString("code");
                            String data = jsonObject.optString("data");
                            String message = jsonObject.optString("message");

                            if("200".equals(resultCode)){


                            }else if("403".equals(resultCode)){
                                Toast.makeText(getApplicationContext(),R.string.param_error,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),R.string.login_fail,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_IMG) {
            showImage(data); //设置图片 跟图片目录
            uploadImage(data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(Intent data) {

        if (data != null) {
            path = ImageSelector.getImagePaths(data);
            for (int i = 0; i < path.size(); i++) {
                /*
                 * 从本地文件中读读取图片
                 * */
                String fileName = "";
                file = new File(path.get(i));
                if (file.getName() == null) {
                } else {
                    fileName = getFileName(path.get(i));
                }
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("image/jpg"), file))
                        .build();
                Request build = new Request.Builder()
                        .url("http://172.16.52.10:8080/UploadDemo4/UploadFile") //TomCat服务器
                        .post(requestBody)
                        .build();
                new OkHttpClient().newCall(build).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        setResult(response.body().string(), true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PublishLeaseActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
        UploadImageAdapter adapter = new UploadImageAdapter(path, PublishLeaseActivity.this);
        upload_image_gv.setAdapter(adapter);
    }
    private void showImage(Intent data) {
        path = ImageSelector.getImagePaths(data); //集合获取path(这里的path是集合)
    }

    public String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }
    private void setResult(String string, final boolean success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    Toast.makeText(PublishLeaseActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishLeaseActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
