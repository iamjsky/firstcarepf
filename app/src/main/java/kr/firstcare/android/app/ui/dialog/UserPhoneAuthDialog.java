package kr.firstcare.android.app.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.ui.activity.LoginActivity;
import kr.firstcare.android.app.ui.activity.SignUpActivity;
import kr.firstcare.android.app.ui.activity.UserInfoActivity;
import kr.firstcare.android.app.ui.fragment.login.FindPWFragment;
import kr.firstcare.android.app.ui.fragment.signup.typev.TypeVSignUpFirstFragment;
import kr.firstcare.android.app.ui.fragment.userinfo.ModifyUserInfoFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * ClassName            UserPhoneAuthDialog
 * Created by JSky on   2020-06-17
 * 
 * Description          휴대폰 본인인증 다이얼로그
 */

public class UserPhoneAuthDialog extends BaseDialog {
    public static final String TAG = "fc_debug";

    private View.OnClickListener mCloseButtonListener;

    private String mTitle = "";

    private Context mContext;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_close)
    ImageView iv_close;

    /*휴대폰 번호 입력*/
    @BindView(R.id.edtxt_inputPhoneNum1)
    EditText edtxt_inputPhoneNum1;
    @BindView(R.id.edtxt_inputPhoneNum2)
    EditText edtxt_inputPhoneNum2;
    @BindView(R.id.edtxt_inputPhoneNum3)
    EditText edtxt_inputPhoneNum3;

    /*인증번호 발송,입력*/
    @BindView(R.id.btn_sendAuthNum)
    Button btn_sendAuthNum;
    @BindView(R.id.edtxt_inputAuthNum)
    EditText edtxt_inputAuthNum;
    @BindView(R.id.btn_checkAuthNum)
    Button btn_checkAuthNum;
    @BindView(R.id.tv_authTime)
    TextView tv_authTime;

    /*완료*/
    @BindView(R.id.btn_finishAuth)
    Button btn_finishAuth;

    private boolean isFin = false;
    private String userPhoneNumber = "";

    private int authStat = 10;
    /**
    *   authStat
    *   10  토큰 발급 단계
    *   11  토큰 발급 완료 상태
    *   12  토큰 발급 실패 상태
    *
    *   21  인증번호 발송 완료 상태
    *   22  인증번호 발송 실패 상태
    *
    *   31  인증번호 확인 입력 카운트다운 시작 상태
    *   32  인증번호 확인 입력 시간 초과 상태
    *   33  인증번호 확인 카운트다운 취소
    *
    *   40  인증 완료
    *
    */

    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000;
    final int COUNT_DOWN_INTERVAL = 1000;

    private int parentType;
    /**  parentType
     *   0   :   비밀번호 찾기
     *   1   :   회원가입
     */

    public UserPhoneAuthDialog(Context context, String title, View.OnClickListener closeButtonListener, int parentType) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mTitle = title;
        mContext = context;
        this.parentType = parentType;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_phone_auth);
        ButterKnife.bind(this);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        iv_close.setOnClickListener(mCloseButtonListener);
        tv_title.setText(mTitle);

        /*초기화*/
        isFin = false;
        authStat = 10;
        tv_authTime.setVisibility(View.GONE);
        setButtonStatus(0,true);
        setButtonStatus(1,false);
        setButtonStatus(2,false);

        edtxt_inputPhoneNum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 3){
                    edtxt_inputPhoneNum2.requestFocus();
                }
            }
        });

        edtxt_inputPhoneNum2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4){
                    edtxt_inputPhoneNum3.requestFocus();
                }
            }
        });

        edtxt_inputPhoneNum3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4){
                    hideSoftKeyBoard();
                }
            }

        });

    }   //  onCreate

    /*인증번호 발송*/
    @OnClick(R.id.btn_sendAuthNum)
    public void btn_sendAuthNumClicked() {
        String phoneNum1 = edtxt_inputPhoneNum1.getText().toString() + "";
        String phoneNum2 = edtxt_inputPhoneNum2.getText().toString() + "";
        String phoneNum3 = edtxt_inputPhoneNum3.getText().toString() + "";
        String inputPhoneNum = phoneNum1 + "-" + phoneNum2 + "-" + phoneNum3;

        if (checkValidationPhoneNumber(inputPhoneNum)) {

            setButtonStatus(0, false);
            getPhoneAuthToken(inputPhoneNum);

        } else {

            setButtonStatus(0, true);
            Constant.LOG(TAG, "휴대폰번호를 입력하세요 : " + inputPhoneNum);

        }

    }

    /*부모뷰 메시지 스낵바*/
    private void showParentTypeSnackbar(String msg) {
        switch (parentType) {
            case 0:

                ((LoginActivity) mContext).showSnackbar(mainLayout, msg);
                break;

            case 1:
                ((SignUpActivity) mContext).showSnackbar(mainLayout, msg);
                break;

            case 2:
                ((UserInfoActivity) mContext).showSnackbar(mainLayout, msg);
                break;
        }

    }

    /*인증번호 확인*/
    @OnClick(R.id.btn_checkAuthNum)
    public void btn_checkAuthNumClicked() {
        String checkAuthNum = edtxt_inputAuthNum.getText().toString() + "";

        if (checkAuthNum != null && !checkAuthNum.equals("")) {
//            String checkAuthNumTrim = checkAuthNum.trim();
//            String phoneAuthNumberTrim = UserInfo.getInstance().phoneAuthNumber.trim();

            if (UserInfo.getInstance().phoneAuthNumber.equals(checkAuthNum)) {

                showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_numberMatch_finish));
                Constant.LOG(TAG, "인증완료 : " + UserInfo.getInstance().phoneAuthNumber);
                setButtonStatus(1, false);
                setButtonStatus(2, true);
                isFin = true;
                UserInfo.getInstance().hp = userPhoneNumber;
                authStat = 40;
                countDownTimer(false);

            } else {

                showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_numberMatch_err));
                Constant.LOG(TAG, "인증번호 불일치 : " + UserInfo.getInstance().phoneAuthNumber + " / " + checkAuthNum);
                setButtonStatus(2, false);
                isFin = false;
                userPhoneNumber = "";

            }
        } else {

            showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_inputNumber_err));
            Constant.LOG(TAG, "인증번호를 입력하세요 : " + checkAuthNum);
            setButtonStatus(2, false);
            isFin = false;
            userPhoneNumber = "";

        }

    }

    /*인증완료 클릭*/
    @OnClick(R.id.btn_finishAuth)
    public void btn_finishAuthClicked() {

        if (isFin) {

            switch (parentType) {

                case 0:
                    FindPWFragment.instance.setPhoneAuthFin(isFin);
                    break;

                case 1:
                    TypeVSignUpFirstFragment.instance.setPhoneAuthFin(isFin);
                    break;

                case 2:
                    ModifyUserInfoFragment.instance.setPhoneAuthFin(isFin);
                    break;

            }

            countDownTimer(false);
            setButtonStatus(0, true);
            setButtonStatus(1, false);
            setButtonStatus(2, false);
            dismiss();

        } else {

            countDownTimer(false);
            setButtonStatus(0, true);
            setButtonStatus(1, false);
            setButtonStatus(2, false);
            Constant.LOG(TAG, "isFin : " + isFin);

        }

    }

    /*gabia_token - 인증 토큰 발급*/
    public void getPhoneAuthToken(String phoneNum){

        apiService.getPhoneAuthToken().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    try {

                        String result = response.body().string();
                        Constant.LOG(TAG, " getPhoneAuthToken result : " + result);
                        if(result != null &&!result.equals("")){

                            UserInfo.getInstance().phoneAuthToken = result;
                            authStat = 11;
                            /*토큰 발급 후 바로 휴대폰에 인증번호 전송*/
                            getPhoneAuthNumber(phoneNum);

                        }else{

                            setButtonStatus(0,true);
                            Constant.LOG(TAG, "getPhoneAuthToken : 토큰발급실패");
                            authStat = 12;
                            showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_token_err) + authStat);

                        }
                    } catch (IOException e) {
                        setButtonStatus(0,true);
                        e.printStackTrace();
                        authStat = 12;
                        showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_token_err) + authStat);
                    }


                }else{

                    setButtonStatus(0,true);

                    Constant.LOG(TAG, "getPhoneAuthToken response err");
                    authStat = 12;
                    showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_token_err) + authStat);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setButtonStatus(0,true);

                Constant.LOG(TAG, "getPhoneAuthToken err : " + t.toString());
                authStat = 12;
                showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_token_err) + authStat);

            }

        });

    }

    /*phonenum_authorization - 휴대폰 인증번호 발급*/
    public void getPhoneAuthNumber(String phoneNum) {

        apiService.getPhoneAuthNumber(UserInfo.getInstance().phoneAuthToken, phoneNum).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();
                        Constant.LOG(TAG, " getPhoneAuthNumber result : " + result);

                        if (result != null && !result.equals("")) {

                            UserInfo.getInstance().phoneAuthNumber = result;

                            setButtonStatus(1, true);
                            userPhoneNumber = phoneNum;
                            authStat = 21;
                            tv_authTime.setVisibility(View.VISIBLE);

                            countDownTimer(true);
                            showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_sendFin));
                            edtxt_inputAuthNum.requestFocus();

                        } else {

                            Constant.LOG(TAG, "getPhoneAuthNumber 인증번호발급실패");

                            authStat = 22;
                            tv_authTime.setVisibility(View.GONE);
                            setButtonStatus(0, true);
                            showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_token_err) + authStat);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();

                        authStat = 22;
                        tv_authTime.setVisibility(View.GONE);
                        setButtonStatus(0, true);
                        showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_token_err) + authStat);

                    }

                } else {

                    Constant.LOG(TAG, "getPhoneAuthNumber response err");

                    authStat = 22;
                    tv_authTime.setVisibility(View.GONE);
                    setButtonStatus(0, true);
                    showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_token_err) + authStat);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.LOG(TAG, "getPhoneAuthNumber err : " + t.toString());
                authStat = 22;
                tv_authTime.setVisibility(View.GONE);
                setButtonStatus(0, true);
                showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_token_err) + authStat);

            }

        });

    }

    /*유효성 검사*/
    private boolean checkValidationPhoneNumber(String inputPhoneNum){
        boolean result = false;


        Constant.LOG("dingding", inputPhoneNum);
        if(inputPhoneNum.equals("")){
           showParentTypeSnackbar(mContext.getString(R.string.PhoneAuth_msg_inputPhoneNumber_err));
        }
        else if(!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", inputPhoneNum)){
            showParentTypeSnackbar(mContext.getString(R.string.PhoneAuth_msg_inputPhoneNumberPattern_err));
        }else{
            result = true;
        }

        return result;
    }

    /*인증번호 입력 카운트*/
    public void countDownTimer(boolean value) {

        if (value) {

            authStat = 31;

            countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long emailAuthCount = millisUntilFinished / 1000;

                    if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) {

                        tv_authTime.setText((emailAuthCount / 60) + ":" + (emailAuthCount - ((emailAuthCount / 60) * 60)));

                    } else {

                        tv_authTime.setText((emailAuthCount / 60) + ":0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));

                    }

                }

                @Override
                public void onFinish() {

                    authStat = 32;
                    tv_authTime.setVisibility(View.GONE);
                    setButtonStatus(0, true);
                    setButtonStatus(1, false);
                    setButtonStatus(2, false);
                    showParentTypeSnackbar(mContext.getResources().getString(R.string.PhoneAuth_msg_countdown_err));

                }

            }.start();

        } else {

            authStat = 33;
            countDownTimer.cancel();

        }

    }

    /*인증 단계별 UI 처리*/
    private void setButtonStatus(int type, boolean value){
        switch (type){

            case 0:
                if(value){

                    edtxt_inputPhoneNum1.setBackgroundResource(R.drawable.bg_input_rounded_01);
                    edtxt_inputPhoneNum2.setBackgroundResource(R.drawable.bg_input_rounded_01);
                    edtxt_inputPhoneNum3.setBackgroundResource(R.drawable.bg_input_rounded_01);
                    btn_sendAuthNum.setTextColor(mContext.getColor(R.color.fc_mainColor));
                    btn_sendAuthNum.setBackgroundResource(R.drawable.bg_rounded_06);

                }else{

                    edtxt_inputPhoneNum1.setBackgroundResource(R.drawable.bg_input_rounded_02);
                    edtxt_inputPhoneNum2.setBackgroundResource(R.drawable.bg_input_rounded_02);
                    edtxt_inputPhoneNum3.setBackgroundResource(R.drawable.bg_input_rounded_02);
                    btn_sendAuthNum.setTextColor(mContext.getColor(R.color.fc_lightGrayColor));
                    btn_sendAuthNum.setBackgroundResource(R.drawable.bg_rounded_gray_04);

                }

                edtxt_inputPhoneNum1.setEnabled(value);
                edtxt_inputPhoneNum2.setEnabled(value);
                edtxt_inputPhoneNum3.setEnabled(value);
                btn_sendAuthNum.setEnabled(value);

                break;

            case 1:
                if(value){

                    edtxt_inputAuthNum.setBackgroundResource(R.drawable.bg_input_rounded_01);
                    btn_checkAuthNum.setTextColor(mContext.getColor(R.color.fc_mainColor));
                    btn_checkAuthNum.setBackgroundResource(R.drawable.bg_rounded_06);

                }else{

                    edtxt_inputAuthNum.setBackgroundResource(R.drawable.bg_input_rounded_02);
                    btn_checkAuthNum.setTextColor(mContext.getColor(R.color.fc_lightGrayColor));
                    btn_checkAuthNum.setBackgroundResource(R.drawable.bg_rounded_gray_04);

                }

                edtxt_inputAuthNum.setEnabled(value);
                btn_checkAuthNum.setEnabled(value);

                break;

            case 2:
                if(value){

                    btn_finishAuth.setTextColor(mContext.getColor(R.color.white));
                    btn_finishAuth.setBackgroundResource(R.drawable.bg_rounded_01);

                }else{

                    btn_finishAuth.setTextColor(mContext.getColor(R.color.fc_deepGrayColor));
                    btn_finishAuth.setBackgroundResource(R.drawable.bg_rounded_04);

                }

                btn_finishAuth.setEnabled(value);

                break;

        }

    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


}
