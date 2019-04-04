package com.autionsy.seller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autionsy.seller.R;
import com.autionsy.seller.constant.Constant;
import com.autionsy.seller.entity.Lease;
import com.autionsy.seller.entity.Recruit;
import com.autionsy.seller.utils.OkHttp3Utils;
import com.scrat.app.selectorlibrary.ImageSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PublishRecruitActivity extends BaseActivity{

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.submit_tv)
    TextView submit_tv;

    @BindView(R.id.recruit_title_et)
    EditText recruit_title_et;
    @BindView(R.id.company_name_et)
    EditText company_name_et;
    @BindView(R.id.start_salary_et)
    EditText start_salary_et;
    @BindView(R.id.end_salary_et)
    EditText end_salary_et;
    @BindView(R.id.seller_address_et)
    EditText seller_address_et;
    @BindView(R.id.recruit_person_number_et)
    EditText recruit_person_number_et;
    @BindView(R.id.contact_phone_number_et)
    EditText contact_phone_number_et;
    @BindView(R.id.work_experience_et)
    EditText work_experience_et;
    @BindView(R.id.education_et)
    EditText education_et;
    @BindView(R.id.company_person_number_et)
    EditText company_person_number_et;

    private String recruitTile;
    private String companyName;
    private String startSalary;
    private String endSalary;
    private String sellerAddress;
    private String recruitPersonNum;
    private String contactPhoneNum;
    private String workExperience;
    private String education;
    private String companyPersonNum;

    private Recruit recruit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_publish_recruit);

        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        title_tv.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.publish_recruit);
        submit_tv.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back_btn,
            R.id.submit_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.submit_tv:
                postAsynHttpGoods();
                break;
        }
    }

    private void postAsynHttpGoods(){
        recruit = new Recruit();

        recruitTile = recruit_title_et.getText().toString().trim();
        companyName = company_name_et.getText().toString().trim();
        startSalary = start_salary_et.getText().toString().trim();
        endSalary = end_salary_et.getText().toString().trim();
        sellerAddress = seller_address_et.getText().toString().trim();
        recruitPersonNum = recruit_person_number_et.getText().toString().trim();
        contactPhoneNum = contact_phone_number_et.getText().toString().trim();
        workExperience = work_experience_et.getText().toString().trim();
        education = education_et.getText().toString().trim();
        companyPersonNum = company_person_number_et.getText().toString().trim();

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
}
