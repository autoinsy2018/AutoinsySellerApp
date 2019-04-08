package com.autionsy.seller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.autionsy.seller.R;
import com.autionsy.seller.activity.CommodityManagementActivity;
import com.autionsy.seller.activity.OrderListActivity;
import com.autionsy.seller.activity.SettingActivity;
import com.autionsy.seller.entity.Seller;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineFragment extends BaseFragment {
    private View view;

    @BindView(R.id.mine_header_iv)
    ImageView mine_header_iv;
    @BindView(R.id.mine_username_tv)
    TextView mine_username_tv;

    private Intent intent;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.frag_mine, null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ButterKnife.bind(this, view);

        postAsynHttpMine();
        return view;
    }

    private void postAsynHttpMine(){
        Seller seller = new Seller();

        String url = Constant.HTTP_URL + "login";

        Map<String,String> map = new HashMap<>();

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

                                RequestOptions options = new RequestOptions()
                                        .placeholder(R.mipmap.default_header)
                                        .error(R.mipmap.default_header);
                                Glide.with(getActivity())
                                        .load(seller.getHeadUrl())
                                        .apply(RequestOptions.circleCropTransform())
                                        .apply(options)
                                        .into(mine_header_iv);

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

    @OnClick({R.id.mine_setting_iv,
            R.id.wait_send_stock,
            R.id.wait_receive_stock,
            R.id.wait_comment_stock,
            R.id.exchange_stock,
            R.id.check_all_order,
            R.id.mine_product_management_layout,
            R.id.mine_ornament_management_text_layout,
            R.id.mine_service_management_text_layout,
            R.id.mine_lease_management_text_layout,
            R.id.mine_recuit_management_text_layout,
            R.id.mine_rescue_management_text_layout,})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.check_all_order:
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("order_state","0");
                startActivity(intent);
                break;
            case R.id.mine_setting_iv:
                intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.wait_send_stock:
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("order_state","1");
                startActivity(intent);
                break;
            case R.id.wait_receive_stock:
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("order_state","2");
                startActivity(intent);
                break;
            case R.id.wait_comment_stock:
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("order_state","3");
                startActivity(intent);
                break;
            case R.id.exchange_stock:
                intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("order_state","4");
                startActivity(intent);
                break;
            case R.id.mine_product_management_layout: //1代表汽配商品管理
                intent = new Intent(getActivity(), CommodityManagementActivity.class);
                intent.putExtra("commodity_management_state","1");
                startActivity(intent);
                break;
            case R.id.mine_ornament_management_text_layout: //2代表内饰商品管理
                intent = new Intent(getActivity(), CommodityManagementActivity.class);
                intent.putExtra("commodity_management_state","2");
                startActivity(intent);
                break;
            case R.id.mine_service_management_text_layout: //3代表服务管理
                intent = new Intent(getActivity(), CommodityManagementActivity.class);
                intent.putExtra("commodity_management_state","3");
                startActivity(intent);
                break;
            case R.id.mine_lease_management_text_layout: //4代表租赁管理
                intent = new Intent(getActivity(), CommodityManagementActivity.class);
                intent.putExtra("commodity_management_state","4");
                startActivity(intent);
                break;
            case  R.id.mine_recuit_management_text_layout: //5代表招聘管理
                intent = new Intent(getActivity(), CommodityManagementActivity.class);
                intent.putExtra("commodity_management_state","5");
                startActivity(intent);
                break;
            case R.id.mine_rescue_management_text_layout: //6代表道路救援管理
                intent = new Intent(getActivity(), CommodityManagementActivity.class);
                intent.putExtra("commodity_management_state","6");
                startActivity(intent);
                break;
        }
    }
}
