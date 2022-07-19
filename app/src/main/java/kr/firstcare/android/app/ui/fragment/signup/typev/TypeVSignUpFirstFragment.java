package kr.firstcare.android.app.ui.fragment.signup.typev;

import android.os.Build;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.data.SignUp;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.ui.activity.LoginActivity;
import kr.firstcare.android.app.ui.activity.SignUpActivity;
import kr.firstcare.android.app.ui.dialog.CheckUserIdDialog;
import kr.firstcare.android.app.ui.dialog.UserPhoneAuthDialog;
import kr.firstcare.android.app.ui.fragment.BaseFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ClassName            TypeVSignUpFirstFragment
 * Created by JSky on   2020-06-12
 * <p>
 * Description          회원가입 프래그먼트 1
 */
public class TypeVSignUpFirstFragment extends BaseFragment {
    private int frag_num;
    public static TypeVSignUpFirstFragment instance;

    /*휴대폰 본인인증 팝업*/
    private UserPhoneAuthDialog userPhoneAuthDialog;
    @BindView(R.id.btn_phoneAuth)
    Button btn_phoneAuth;

    /*회원정보 입력*/
    @BindView(R.id.edtxt_inputId)
    EditText edtxt_inputId;
    @BindView(R.id.edtxt_inputPassword)
    EditText edtxt_inputPassword;
    @BindView(R.id.edtxt_inputMatchPassword)
    EditText edtxt_inputMatchPassword;
    @BindView(R.id.tv_checkMatchPw)
    TextView tv_checkMatchPw;
    @BindView(R.id.edtxt_inputName)
    EditText edtxt_inputName;

    /*아이디 중복확인 팝업*/
    private CheckUserIdDialog checkIdPopUpDialog;

    private boolean isPhoneAuthFin = false;
    private boolean isCheckIdFin = false;

    public TypeVSignUpFirstFragment() {
    }

    // newInstance constructor for creating fragment with arguments
    public static TypeVSignUpFirstFragment newInstance(int num) {
        TypeVSignUpFirstFragment fragment = new TypeVSignUpFirstFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frag_num = getArguments().getInt("num", 0);

    }   //  onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_typev_signup_first, container, false);
        ButterKnife.bind(this, view);
        instance = this;

        /*휴대폰 인증 초기화*/
        isPhoneAuthFin = false;

        edtxt_inputId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*아이디 글자가 바뀌면 중복확인 false*/
                isCheckIdFin = false;


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtxt_inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*입력할 때 마다 비밀번호 일치 체크*/
                if(edtxt_inputPassword.getText().toString().equals(edtxt_inputMatchPassword.getText().toString())){

                    tv_checkMatchPw.setText(getString(R.string.TypeVSignUpFirstFragment_text_passwordMatch));

                }else{

                    tv_checkMatchPw.setText(getString(R.string.TypeVSignUpFirstFragment_text_passwordNotMatch));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        edtxt_inputMatchPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*입력할 때 마다 비밀번호 일치 체크*/
                if(edtxt_inputPassword.getText().toString().equals(edtxt_inputMatchPassword.getText().toString())){

                    tv_checkMatchPw.setText(getString(R.string.TypeVSignUpFirstFragment_text_passwordMatch));

                }else{

                    tv_checkMatchPw.setText(getString(R.string.TypeVSignUpFirstFragment_text_passwordNotMatch));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        return view;

    }   //  onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);
    }   //  onViewCreated

    @Override
    public void onResume() {
        super.onResume();
        /*휴대폰 인증 버튼 활성화*/
        btn_phoneAuth.setEnabled(true);

    }   //  onResume

    @Override
    public void onStop() {
        super.onStop();
    }

    /*아이디 중복 확인 클릭*/
    @OnClick(R.id.btn_checkDuplicateId)
    public void btn_checkDuplicateIdClicked(){
        String userId = edtxt_inputId.getText().toString()+"";

        if (userId.equals("") || userId.length() == 0) {

            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputId_err));

            /*아이디 : 네 글자 이상*/
        } else if (userId.length() < 6) {

            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputIdCount_err));
            Constant.LOG(TAG, "아이디 6자리 이상");

            /*아이디 : 영문 숫자 혼합*/
        } else if (!Pattern.matches("^[a-zA-Z0-9]+$", userId)) {

            Constant.LOG(TAG, "아이디 영문 숫자만가능");
            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputIdPattern_err));

        }else{

            duplicateCheckUserId(userId);

        }

    }

    // 아이디 중복 확인 팝업 닫기
    private View.OnClickListener checkIdPopUpCloseButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            checkIdPopUpDialog.dismiss();

        }

    };

    /*휴대폰 본인인증 클릭*/
    @OnClick(R.id.btn_phoneAuth)
    public void btn_phoneAuthClicked(){
        if(isPhoneAuthFin) {
            Constant.LOG("dingding", "이미인증하였음");

        }else{
            userPhoneAuthDialog = new UserPhoneAuthDialog(getActivity(), getString(R.string.PhoneAuth_popup_title_01), userPhoneAuthCloseButtonListener, 1);
            userPhoneAuthDialog.show();

        }

    }

    // 휴대폰 인증 팝업 닫기
    private View.OnClickListener userPhoneAuthCloseButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {
            userPhoneAuthDialog.dismiss();
            setPhoneAuthFin(false);
        }

    };

    /*휴대폰 인증 완료 처리*/
    public void setPhoneAuthFin(boolean isFin){
        isPhoneAuthFin = isFin;

        if(isFin) {

            btn_phoneAuth.setText(getString(R.string.PhoneAuth_text_authFin));
            btn_phoneAuth.setEnabled(false);

        }else{

            btn_phoneAuth.setText(getString(R.string.PhoneAuth_text_authDefault));
            btn_phoneAuth.setEnabled(true);

        }

    }

    /*다음 클릭*/
    @OnClick(R.id.btn_next)
    public void btn_NextClicked(){

        if (checkValidation()) {

            setUserSignUpData();

        }

    }

    /*회원가입 데이터 입력*/
    private void setUserSignUpData(){

        String vteacher_name = edtxt_inputName.getText().toString() + "";
        String vteacher_id = edtxt_inputId.getText().toString() + "";
        String userpw = edtxt_inputPassword.getText().toString() + "";
        String hp =  UserInfo.getInstance().hp + "";

        SignUp.getInstance().vteacherName = vteacher_name;
        SignUp.getInstance().vteacherId = vteacher_id;
        SignUp.getInstance().userPw = userpw;
        SignUp.getInstance().hp = hp;

        ((SignUpActivity)getActivity()).setFragment(1);

    }

    /*유효성 검사*/
    private boolean checkValidation() {
        boolean check = false;

        String inputUserId = edtxt_inputId.getText().toString() + "";
        String inputUserPW = edtxt_inputPassword.getText().toString() + "";
        String inputUserMatchPW = edtxt_inputMatchPassword.getText().toString() + "";
        String inputName = edtxt_inputName.getText().toString()+"";

        /*이름 : 한글만 입력 가능*/
        if (inputName.equals("") || !Pattern.matches("^[가-힣]*$", inputName)) {

            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputName_err));
            Constant.LOG(TAG, "이름 한글만 가능");

            /*아이디 : 입력 확인*/
        } else if (inputUserId.equals("") || inputUserId.length() == 0) {

            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputId_err));

            /*아이디 : 네 글자 이상*/
        } else if (inputUserId.length() < 6) {

            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputIdCount_err));
            Constant.LOG(TAG, "아이디 6자리 이상");

            /*아이디 : 영문 숫자 혼합*/
        } else if (!Pattern.matches("^[a-zA-Z0-9]+$", inputUserId)) {

            Constant.LOG(TAG, "아이디 영문 숫자만가능");
            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputIdPattern_err));

            /*아이디 : 중복확인체크*/
        } else if (!isCheckIdFin) {

            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_duplicateId_err));

            /*비밀번호 : 입력 체크*/
        } else if (inputUserPW.length() == 0) {

            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputPW_err));

            /*비밀번호 : 6자리 이상*/
        } else if (inputUserPW.length() < 6) {

            Constant.LOG(TAG, "비밀번호 6자리 이상");
            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputPWCount_err));

            /*비밀번호 : 영문 소문자 숫자 특수문자 각각 1개이상*/
        } else if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{6,15}$", inputUserPW)) {

            Constant.LOG(TAG, "영어숫자특수문자 조합 6~15 가능");
            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputPWPattern_err));

            /*비밀번호 확인 : 입력 체크*/
        } else if (inputUserMatchPW.length() == 0) {

            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_inputMatchPW_err));
            /*비밀번호 확인 : 일치 체크*/
        } else if (!inputUserPW.equals(inputUserMatchPW)) {

            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_matchPW_err));

          /*휴대폰 본인인증*/
        } else if (!isPhoneAuthFin) {

            showSnackbar(getView(), getString(R.string.TypeVSignUpFirstFragment_msg_phoneAuth_err));

        } else {

            check = true;

        }

        return check;

    }

    /*confirm_id - 아이디 중복 확인*/
    public void duplicateCheckUserId(String user_id) {

        apiService.duplicateCheckUserId(user_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();
                        String resultText = "";
                        Constant.LOG(TAG, "result : " + result);

                        if (result.equals("0")) {

                            resultText = getString(R.string.TypeVSignUpFirstFragment_text_availableId);
                            isCheckIdFin = true;
                            Constant.LOG(TAG, "사용가능한 아이디");

                        } else if (result.equals("1")) {

                            resultText = getString(R.string.TypeVSignUpFirstFragment_text_duplicateId);
                            isCheckIdFin = false;
                            Constant.LOG(TAG, "중복되는 아이디");

                        }

                        checkIdPopUpDialog = new CheckUserIdDialog(getActivity(), getString(R.string.TypeVSignUpFirstFragment_popup_title_01), resultText, checkIdPopUpCloseButtonListener, false);
                        checkIdPopUpDialog.show();

                    } catch (IOException e) {
                        showSnackbar(getView(), getString(R.string.Common_msg_network_err));
                        e.printStackTrace();

                    }


                } else {

                    showSnackbar(getView(), getString(R.string.Common_msg_network_err));
                    Constant.LOG(TAG, "duplicateCheckUserId response err");

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showSnackbar(getView(), getString(R.string.Common_msg_network_err));
                Constant.LOG(TAG, "duplicateCheckUserId err : " + t.toString());

            }

        });

    }

}
