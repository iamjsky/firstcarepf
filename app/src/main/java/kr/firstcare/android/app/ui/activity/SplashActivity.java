package kr.firstcare.android.app.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.UserModel;
import kr.firstcare.android.app.ui.dialog.DefaultPopUpDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * ClassName            SplashActivity
 * Created by JSky on   2020-06-03
 * 
 * Description          스플래시
 */

public class SplashActivity extends BaseActivity {
    /*앱 체크*/
    private boolean isErr = false;
    private static int CHECKNUM = 0;
    Handler mHandler;
    int MY_PERMISSIONS_REQUEST_CONTACTS = 1;

    /*스플래시*/
    @BindView(R.id.iv_logoText)
    ImageView iv_logoText;
    Animation fadeOutAnimation, fadeInAnimation;

    /*메시지 팝업*/
    private DefaultPopUpDialog defaultPopUpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        /*스플래시 로고 페이드 인/아웃 애니메이션*/
        fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_fade_out);
        fadeOutAnimation.setAnimationListener(fadeOutAnimationListener);
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_fade_in);
        fadeInAnimation.setAnimationListener(fadeInAnimationListener);

        /*스플래시 로고 페이드 아웃 시작*/
        iv_logoText.startAnimation(fadeOutAnimation);

        /*앱 관련 체크 처리 핸들러*/
        mHandler = new Handler() {
            public void handleMessage(Message msg) {

                if (!isErr) {

                    /*앱 관련 체크 모두 true*/
                    if (CHECKNUM > Constant.PROGRESS_COUNT) {

                        /*체크 완료 후 자동 로그인*/
                        if (PreferenceManager.getInt(mContext, "selectLoginType") > -1 &&
                                PreferenceManager.getString(mContext, "vteacherId") != null &&
                                !PreferenceManager.getString(mContext, "vteacherId").equals("") &&
                                PreferenceManager.getString(mContext, "userpw") != null &&
                                !PreferenceManager.getString(mContext, "userpw").equals("") &&
                                PreferenceManager.getBoolean(mContext, "autoLoginCheck")) {

                            int selectLoginType = PreferenceManager.getInt(mContext, "selectLoginType");
                            String vteacherId = PreferenceManager.getString(mContext, "vteacherId");
                            String userpw = PreferenceManager.getString(mContext, "userpw");

                            /*나중에 자동 로그인 종류 분기(대상자보호자, 케어선생님, 제공인력)*/

                            // 제공인력
                            if (selectLoginType == 3) {

                                Constant.LOG(TAG, "vteacherId : " + vteacherId + "/ userpw : " + userpw);
                                userLogin(vteacherId, userpw);

                            }

                            /**
                             * 자동 로그인 실패
                             * 1. 자동 로그인 체크 하지 않은 경우
                             * 2. 내부에 저장된 사용자 정보 누락
                             * 3. 앱 최초 실행
                             */
                        } else {

                            Intent intent = new Intent(SplashActivity.this, SelectLoginTypeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

                        }

                        /*앱 관련 체크 하나라도 false면 처음부터 다시 체크*/
                    } else {

                        startLoading();

                    }

                }

            }

        };


        /*해시키 조회*/
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("kr.firstcare.android.app", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Constant.LOG("key_hash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }


    }   //  onCreate

    @Override
    protected void onResume() {
        super.onResume();
        CHECKNUM = 0;



    }   // onResume

    @Override
    public void onBackPressed() {

    }   // onBackPressed


    /*스플래시 로고 페이드 아웃 리스너*/
    public Animation.AnimationListener fadeOutAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {

            /*스플래시 로고 페이드인 시작*/
            Glide.with(SplashActivity.this).load(R.drawable.splash_logo_02)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_logoText);
            iv_logoText.startAnimation(fadeInAnimation);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    };

    /*스플래시 로고 페이드 인 리스너*/
    public Animation.AnimationListener fadeInAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {

            /*페이드인 끝나고 앱 체크 시작*/
            startLoading();

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    };

    /*퍼미션 및 기능, 네트워크 상태 체크*/
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean stateCheck(int checkNum){

        /**
         * checkNum
         * 0    권한 허용 체크
         * 1    GPS 기능 ON/OFF 체크
         * 2    네트워크 연결 상태 체크
         * 3    퍼스트케어 서버 상태 체크 (현재 사용 안함, true 고정)
         */

        boolean check = false;

        switch (checkNum){
            // 권한 체크
            case 0:
                Constant.ACCESS_FINE_LOCATION_STAT = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                Constant.ACCESS_BACKGROUND_LOCATION_STAT = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION);

                /*GPS 권한 관련 안드로이드 최신/구 버전 체크*/
                if (Constant.ACCESS_FINE_LOCATION_STAT == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (Constant.ACCESS_BACKGROUND_LOCATION_STAT == PackageManager.PERMISSION_GRANTED) { check = true; }
                        else { check = false; }

                    } else { check = true; }

                } else { check = false; }

                break;

                // GPS 기능 ON/OFF 체크
            case 1:
                if (Utils.getGPSState(this)) { check = true; }
                else { check = false; }

                break;

                // 네트워크 상태 체크
            case 2:
                if(Utils.getNetworkStatus(this) < 3){ check = true; }
                else{ check = false; }

                break;

                // 퍼스트케어 서버 상태 체크(사용안함)
            case 3:
                check = true;
                break;

            default:
                check = false;

        }
        Constant.LOG(TAG, checkNum + " : " + check);
        return check;

    }

    /*권한 Request*/
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 // 사용자가 권한 거부 하더라도 반드시 허용해야 하기 때문에 무조건 처음부터 다시 체크
                }

                isErr = false;
                Constant.LOG(TAG, "33");
                CHECKNUM = 0;
                startLoading();

                return;

            }

        }

    }

    /*체크 실행*/
    private void startLoading() {

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void run() {

                if (!isErr) {

                    if (stateCheck(CHECKNUM)) {

                        CHECKNUM++;
                        Message msg = mHandler.obtainMessage();
                        Bundle bd = new Bundle();
                        bd.putString("fire", "fire");
                        msg.setData(bd);

                        mHandler.sendMessage(msg);    //메세지를 핸들러로 넘김

                    } else {

                        isErr = true;

                        /*메시지 팝업*/
                        ViewGroup permissionView = (ViewGroup) getLayoutInflater().inflate(R.layout.view_permission_meta, null, false);

                        switch (CHECKNUM) {

                            case 0:

                                TextView permissionErrText = permissionView.findViewById(R.id.tv_test);
                                permissionErrText.setText(getString(R.string.Common_msg_permission_err));
                                defaultPopUpDialog = new DefaultPopUpDialog(SplashActivity.this, getString(R.string.Dialog_title_default), permissionView, permissionErrPopUpCloseButtonListener, false);
                                defaultPopUpDialog.show();

                                break;

                            case 1:

                                TextView gpsErrText = permissionView.findViewById(R.id.tv_test);
                                gpsErrText.setText(getString(R.string.Common_msg_gps_err));
                                defaultPopUpDialog = new DefaultPopUpDialog(SplashActivity.this, getString(R.string.Dialog_title_default), permissionView, gpsErrPopUpCloseButtonListener, false);
                                defaultPopUpDialog.show();

                                break;

                            case 2:

                                TextView networkErrText = permissionView.findViewById(R.id.tv_test);
                                networkErrText.setText(getString(R.string.Common_msg_network_err));
                                defaultPopUpDialog = new DefaultPopUpDialog(SplashActivity.this, getString(R.string.Dialog_title_default), permissionView, networkErrPopUpCloseButtonListener, false);
                                defaultPopUpDialog.show();

                                break;

                            case 3:

                                break;

                        }
                    }
                }
            }

            /*!!!! 0.5초마다 체크*/
        }, 500);

    }

    /*추후 기능 추가를 위해 팝업 닫기는 각각 정의*/

    // 퍼미션 에러 팝업 닫기
    private View.OnClickListener permissionErrPopUpCloseButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {
            defaultPopUpDialog.dismiss();
            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    MY_PERMISSIONS_REQUEST_CONTACTS);

        }
    };

    // gps 에러 팝업 닫기
    private View.OnClickListener gpsErrPopUpCloseButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {
            defaultPopUpDialog.dismiss();
            CHECKNUM = 0;
            isErr=false;
            startLoading();

        }
    };

    // 네트워크 에러 팝업 닫기
    private View.OnClickListener networkErrPopUpCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            defaultPopUpDialog.dismiss();
            CHECKNUM = 0;
            isErr=false;
            startLoading();

        }
    };

    /*login_insteacher - 회원 로그인*/
    public void userLogin(String vteacher_id, String userpw){

        apiService.userLogin(vteacher_id, userpw).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();
                        Constant.LOG(TAG, "result : " + result);

                        if (result.equals("0")) {

                            PreferenceManager.setString(mContext, "vteacherId", "");
                            PreferenceManager.setString(mContext, "userpw", "");
                            Constant.LOG(TAG, "회원정보가 없습니다.");
                            showSnackbar(getString(R.string.userLogin_msg_err_01));

                        } else if (result.equals("1")) {

                            PreferenceManager.setString(mContext, "vteacherId", vteacher_id);
                            PreferenceManager.setString(mContext, "userpw", userpw);
                            Constant.LOG(TAG, "로그인에 성공 했습니다.");

                            /*로그인 후 사용자 정보 가져오기*/
                            getUserInfo(vteacher_id);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        showSnackbar(getString(R.string.userLogin_msg_err_02));
                    }


                } else {
                    showSnackbar(getString(R.string.userLogin_msg_err_02));
                    Constant.LOG(TAG, "UserLogin response err");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showSnackbar(getString(R.string.userLogin_msg_err_03));
                Constant.LOG(TAG, "UserLogin err : " + t.toString());
            }
        });
    }

    /*insteacher_user_info - 회원정보 가져오기*/
    public void getUserInfo(String vteacher_id) {

        apiService.getUserInfo(vteacher_id).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    List<UserModel> userModelList = new ArrayList<>();
                    userModelList = response.body();

                    Constant.LOG(TAG, "getUserInfo result : " + userModelList);

                    if (userModelList.size() > 0) {
                        for (UserModel data : userModelList) {
                            Constant.LOG(TAG, " data.getVteacherName() result : " + data.getVteacherName());

                            PreferenceManager.setString(mContext, "vteacherName", data.getVteacherName());
                            PreferenceManager.setInt(mContext, "idx", data.getIdx());

                            UserInfo.getInstance().selectLoginType = PreferenceManager.getInt(mContext, "selectLoginType");
                            UserInfo.getInstance().vteacherId = PreferenceManager.getString(mContext, "vteacherId");
                            UserInfo.getInstance().userpw = PreferenceManager.getString(mContext, "userpw");
                            UserInfo.getInstance().vteacherName = PreferenceManager.getString(mContext, "vteacherName");
                            UserInfo.getInstance().idx = PreferenceManager.getInt(mContext, "idx");
                            UserInfo.getInstance().photo = data.getPhoto();
                            UserInfo.getInstance().hp = data.getHp();
                            UserInfo.getInstance().email = data.getEmail();

                        }

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

                    } else {

                        showSnackbar(getString(R.string.getUserInfo_msg_err_01));

                    }

                } else {

                    showSnackbar(getString(R.string.getUserInfo_msg_err_02));
                    Constant.LOG(TAG, "getUserInfo response err");
                }

            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                showSnackbar(getString(R.string.getUserInfo_msg_err_03));
                Constant.LOG(TAG, "getUserInfo err : " + t.toString());
            }

        });

    }

}
