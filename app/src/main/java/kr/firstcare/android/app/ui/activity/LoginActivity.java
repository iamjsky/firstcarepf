package kr.firstcare.android.app.ui.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.UserModel;
import kr.firstcare.android.app.ui.fragment.login.FindIDFragment;
import kr.firstcare.android.app.ui.fragment.login.FindPWFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ClassName            LoginActivity
 * Created by JSky on   2020-06-03
 * <p>
 * Description          회원 로그인
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.layout_login)
    LinearLayout layout_login;

    @BindView(R.id.edtxt_inputId)
    EditText edtxt_inputId;
    @BindView(R.id.edtxt_inputPassword)
    EditText edtxt_inputPassword;

    @BindView(R.id.chkbox_checkAttendance)
    CheckBox chkbox_checkAttendance;

    private int selectLoginType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        chkbox_checkAttendance.setButtonDrawable(R.drawable.radiobtn_custom_01);

    }   //  onCreate

    @Override
    protected void onResume() {
        super.onResume();
        selectLoginType = UserInfo.getInstance().selectLoginType;

    }   //  onResume

    @Override
    public void onBackPressed() {
        iv_backClicked();

    }

    /*뒤로가기 클릭*/
    @OnClick(R.id.iv_back)
    public void iv_backClicked(){
        Intent intent = new Intent(this, SelectLoginTypeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }

    /*자동 로그인 체크*/
    @OnClick(R.id.layout_checkAutoLogin)
    public void layout_checkAutoLoginClicked(){
        if(chkbox_checkAttendance.isChecked()) {
            chkbox_checkAttendance.setChecked(false); PreferenceManager.setBoolean(this, "autoLoginCheck", false);
        }
        else {
            chkbox_checkAttendance.setChecked(true); PreferenceManager.setBoolean(this, "autoLoginCheck", true);
        }

    }

    /*아이디 찾기, 비밀번호 찾기 프래그먼트 컨트롤*/
    public void setFragment(int num) {

        switch (num) {
            case 0:
                FindIDFragment findIDFragment = new FindIDFragment();
                findIDFragment.show(this.getSupportFragmentManager(), "FindIDFragment");

                break;

            case 1:
                FindPWFragment findPWFragment = new FindPWFragment();
                findPWFragment.show(this.getSupportFragmentManager(), "FindPWFragment");

                break;
        }

    }

    /*유효성 검사*/
    private boolean checkValidation(){
        boolean value = false;
        String inputId = edtxt_inputId.getText().toString()+"";
        String inputPw = edtxt_inputPassword.getText().toString()+"";
        if(inputId.length() == 0){
            showSnackbar(getString(R.string.LoginActivity_msg_inputId_err));
        }
        else if(inputId.length() < 4){
            showSnackbar(getString(R.string.LoginActivity_msg_inputIdCount_err));
            Constant.LOG(TAG, "아이디 4자리 이상");
        }
        else if(!Pattern.matches("^[a-zA-Z0-9]+$", inputId)){
            Constant.LOG(TAG, "아이디 영문 숫자만가능");
            showSnackbar(getString(R.string.LoginActivity_msg_inputIdPattern_err));
        }
        else if(inputPw.length() == 0){
            showSnackbar(getString(R.string.LoginActivity_msg_inputPW_err));
        }
        else if(inputPw.length() < 6){
            Constant.LOG(TAG, "비밀번호 6자리 이상");
            showSnackbar(getString(R.string.LoginActivity_msg_inputPWCount_err));
        }
        else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{6,15}$", inputPw)){
            Constant.LOG(TAG, "비밀번호 영어숫자특수문자 조합 6~15 가능");
            showSnackbar(getString(R.string.LoginActivity_msg_inputPWPattern_err));
        }else{
            value = true;
        }

        return value;

    }

    /*로그인 클릭*/
    @OnClick(R.id.btn_login)
    public void btn_loginClicked(){
        if(checkValidation()){
            String inputId = edtxt_inputId.getText().toString()+"";
            String inputPw = edtxt_inputPassword.getText().toString()+"";

            if(selectLoginType == 3){
                userLogin(inputId, inputPw);
            }

        }

    }

    /*login_insteacher - 회원 로그인*/
    public void userLogin(String vteacher_id, String userpw){

        apiService.userLogin(vteacher_id, userpw).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    try {

                        String result = response.body().string();
                        Constant.LOG(TAG, "result : " + result);

                        if(result.equals("0")){

                            PreferenceManager.setString(mContext, "vteacherId", "");
                            PreferenceManager.setString(mContext, "userpw", "");
                            showSnackbar(getString(R.string.userLogin_msg_err_01));
                            Constant.LOG(TAG, "아이디와 비밀번호를 확인해 주세요.");

                        }else if(result.equals("1")){

                            PreferenceManager.setString(mContext, "vteacherId", vteacher_id);
                            PreferenceManager.setString(mContext, "userpw", userpw);
                            Constant.LOG(TAG, "로그인에 성공 했습니다.");

                            getUserInfo(vteacher_id);

                        }

                    } catch (IOException e) {
                        showSnackbar(getString(R.string.userLogin_msg_err_02));
                        e.printStackTrace();

                    }


                }else{
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
    public void getUserInfo(String vteacher_id){

        apiService.getUserInfo(vteacher_id).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if(response.isSuccessful()){
                    List<UserModel> userModelList = new ArrayList<>();
                    userModelList = response.body();

                    Constant.LOG(TAG, "getUserInfo result : " + userModelList);

                    if(userModelList.size() > 0){
                        for(UserModel data : userModelList){
                            Constant.LOG(TAG, " data.getVteacherName() result : " +  data.getVteacherName());

                            UserInfo.getInstance().vteacherId = PreferenceManager.getString(mContext,"vteacherId");
                            UserInfo.getInstance().userpw = PreferenceManager.getString(mContext,"userpw");
                            UserInfo.getInstance().vteacherName = data.getVteacherName();
                            UserInfo.getInstance().idx = data.getIdx();
                            UserInfo.getInstance().photo = data.getPhoto();
                            UserInfo.getInstance().hp = data.getHp();
                            UserInfo.getInstance().email = data.getEmail();



                        }

                      //  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

                    }else{

                        showSnackbar(getString(R.string.getUserInfo_msg_err_01));
                        Constant.LOG(TAG, "getUserInfo data err");

                    }

                }else{

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

    /*아이디 찾기 클릭*/
    @OnClick(R.id.tv_findId)
    public void tv_findIdClicked() {
        setFragment(0);

    }

    /*비밀번호 찾기 클릭*/
    @OnClick(R.id.tv_findPassword)
    public void tv_findPasswordClicked() {
        setFragment(1);

    }

    /*회원가입 클릭*/
    @OnClick(R.id.tv_signUp)
    public void tv_signUpClicked() {
        Intent intent = new Intent(this, SelectSignUpTypeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }

}