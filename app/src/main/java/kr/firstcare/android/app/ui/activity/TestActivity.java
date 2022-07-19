package kr.firstcare.android.app.ui.activity;

import androidx.annotation.RequiresApi;

import android.os.Build;
import android.os.Bundle;

import kr.firstcare.android.app.data.Constant;

import android.view.View;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.ui.dialog.SearchAddressDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends BaseActivity {
    private SearchAddressDialog searchAddressDialog;
    private ArrayList<String> selectedIdx = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        /*정규식테스트*/
        String emaila = "a.b-cd";
        String emailb = "saf~12jow.co.krf.af";
        if (!Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+$", emaila)) {
            Constant.LOG(TAG, "안맞는다");
        }else{
            Constant.LOG(TAG, "맞다");
        }

//        ArrayList<String> agencyIdxList = new ArrayList<>();
//        selectedIdx.add("1");
//        selectedIdx.add("2");
//        selectedIdx.add("7");
//        for(int i=0; i < selectedIdx.size(); i++){
//            agencyIdxList.add(selectedIdx.get(i).toString());
//
//        }
//        apiService.userSignUp("fctest04", "test1234!@", "테스트사", "010-0000-0000", "email@test.com", "y",
//                "y", "m", "00000", "주소", "상세주소", "", "",
//                "", "", "0000-00-00 00:00:00", "", "2020-07-08 14:00:00", "fctest04",
//                "", agencyIdxList).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if(response.isSuccessful()){
//
//
//                    try {
//                        assert response.body() != null;
//                        Constant.LOG(TAG, "userSignUp : " + response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//
//
//
//
//                }else{
//                    Constant.LOG(TAG, "userSignUp response err : ");
//
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Constant.LOG(TAG, "userSignUp err : " + t);
//
//            }
//
//        });

    }


}