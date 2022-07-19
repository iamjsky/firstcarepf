package kr.firstcare.android.app.ui.activity;

import androidx.appcompat.app.AppCompatActivity;


import kr.firstcare.android.app.R;
import kr.firstcare.android.app.RetrofitGenerator;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.api.APIService;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.data.UserInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;


/**
 * ClassName            BaseActivity
 * Created by JSky on   2020-06-03
 *
 * Description          기본 정의 액티비티
 */

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "fc_debug";
    public RetrofitGenerator retrofitGenerator;
    public APIService apiService;
    protected Context mContext;
    public final long FINISH_INTERVAL_TIME = 2000;
    public long backPressedTime = 0;
    public View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        retrofitGenerator = new RetrofitGenerator();
        apiService = retrofitGenerator.getApiService();

        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }   //  onCreate

    @Override
    protected void onResume() {
        super.onResume();
        Constant.LOG(TAG, "selectLoginType : " + UserInfo.getInstance().selectLoginType);
        Constant.LOG(TAG, "selectLoginType : " + PreferenceManager.getInt(this, "selectLoginType"));

    }   //  onResume

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    /*회원 로그아웃 처리*/
    public void userLogout(){

        /*내부에 저장된 유저 정보 초기화*/
        UserInfo.getInstance().selectLoginType = -1;
        PreferenceManager.setInt(this, "selectLoginType", -1);
        PreferenceManager.setString(mContext, "vteacherId","");
        PreferenceManager.setString(mContext, "vteacherName","");
        PreferenceManager.setString(mContext, "userpw", "");
        PreferenceManager.setInt(mContext, "idx",-1);
        PreferenceManager.setBoolean(this, "autoLoginCheck", false);
        PreferenceManager.setInt(mContext, "selectedAgencyPosition", 0);
        PreferenceManager.setInt(mContext, "selectedProgramPosition", 0);

        /*충돌 대비 초기화*/
        UserInfo.getInstance().selectLoginType = PreferenceManager.getInt(mContext,"selectLoginType");
        UserInfo.getInstance().vteacherId = PreferenceManager.getString(mContext,"vteacherId");
        UserInfo.getInstance().userpw = PreferenceManager.getString(mContext,"userpw");
        UserInfo.getInstance().vteacherName = PreferenceManager.getString(mContext,"vteacherName");
        UserInfo.getInstance().idx = PreferenceManager.getInt(mContext,"idx");
        UserInfo.getInstance().selectedService_idx = -1;
        UserInfo.getInstance().selectedSubagency_idx = -1;
        UserInfo.getInstance().selectedService_name = "";
        UserInfo.getInstance().selectedSubagency_name = "";
        UserInfo.getInstance().selectedSubagency_addr = "";


        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }

    /*액티비티 메시지 스낵바*/
    public void showSnackbar(String msg){

        Snackbar snackbar = Snackbar.make(mainLayout, msg, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getResources().getColor(R.color.fc_boldColor));

        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0,0,0,0);

        snackbarView.setLayoutParams(params);

        TextView mTextView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        mTextView.setGravity(Gravity.TOP);
        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mTextView.setTypeface(mTextView.getTypeface(), Typeface.BOLD);
        mTextView.setTextColor(getColor(R.color.white));
        mTextView.setTextSize(Utils.dpToPx(this, getResources().getDimension(R.dimen._1sdp)));
        mTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.getView().bringToFront();

        snackbar.show();

    }

    /*프래그먼트, 다이얼로그 메시지 스낵바*/
    public void showSnackbar(View parent, String msg){

        Snackbar snackbar = Snackbar.make(parent, msg, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getResources().getColor(R.color.fc_boldColor));

        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0,0,0,0);

        snackbarView.setLayoutParams(params);

        TextView mTextView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        mTextView.setGravity(Gravity.TOP);
        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mTextView.setTypeface(mTextView.getTypeface(), Typeface.BOLD);
        mTextView.setTextColor(getColor(R.color.white));
        mTextView.setTextSize(Utils.dpToPx(this, getResources().getDimension(R.dimen._1sdp)));
        mTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.getView().bringToFront();

        snackbar.show();

    }

}
