package com.autionsy.seller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.adapter.UploadImageAdapter;
import com.autionsy.seller.constant.Constant;
import com.autionsy.seller.entity.Lease;
import com.autionsy.seller.entity.Service;
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

public class PublishServiceActivity extends BaseActivity {

    @BindView(R.id.upload_image_gv)
    GridViewInScrollView upload_image_gv;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.submit_tv)
    TextView submit_tv;

    @BindView(R.id.service_name_et)
    EditText service_name_et;
    @BindView(R.id.service_store_et)
    EditText service_store_et;
    @BindView(R.id.contact_et)
    EditText contact_et;
    @BindView(R.id.contact_phone_num_et)
    EditText contact_phone_num_et;
    @BindView(R.id.service_address_et)
    EditText service_address_et;
    @BindView(R.id.service_describe_et)
    EditText service_describe_et;
    @BindView(R.id.service_area_tv)
    TextView service_area_tv;
    @BindView(R.id.service_type_tv)
    TextView service_type_tv;
    @BindView(R.id.upload_image_textview)
    TextView upload_image_textview;

    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;
    private File file;
    private List<String> path;//路径集合

    private Service service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_publish_service);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.publish_service);
        submit_tv.setVisibility(View.VISIBLE);
        upload_image_textview.setText(R.string.upload_seller_shop_photo);
    }

    @OnClick({R.id.back_btn,
            R.id.image_selector_layout,
            R.id.submit_tv,
            R.id.service_area_selector_layout})
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
            case R.id.service_area_selector_layout:

                break;
            case R.id.service_type_selector_layout:

                break;
        }
    }

    private void postAsynHttpGoods(){
        service = new Service();
//        goodsName = goods_name_et.getText().toString().trim();
//        goodsQuantity = goods_quantity_et.getText().toString().trim();
//        goodsProductPlace = goods_product_place_et.getText().toString().trim();

        String url = Constant.HTTP_URL + "login";

        Map<String,String> map = new HashMap<>();
//        map.put("loginName", goodsName);
//        map.put("passWord", goodsQuantity);
//        map.put("passWord", goodsProductPlace);

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
                                Toast.makeText(PublishServiceActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
        UploadImageAdapter adapter = new UploadImageAdapter(path, PublishServiceActivity.this);
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
                    Toast.makeText(PublishServiceActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishServiceActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
