package kr.firstcare.android.app.ui.fragment.userinfo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.adapter.BoardFaqAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.FaqListModel;
import kr.firstcare.android.app.ui.fragment.BaseFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ClassName            ChangePasswordFragment
 * Created by JSky on   2020-07-07
 * <p>
 * Description          비밀번호 변경
 */
public class ChangePasswordFragment extends BaseFragment {
    private int frag_num;
    public static ChangePasswordFragment instance;


    @BindView(R.id.edtxt_inputBeforePassword)
    EditText edtxt_inputBeforePassword;
    @BindView(R.id.edtxt_inputAfterPassword)
    EditText edtxt_inputAfterPassword;
    @BindView(R.id.edtxt_inputMatchAfterPassword)
    EditText edtxt_inputMatchAfterPassword;
    @BindView(R.id.tv_checkMatchPw)
    TextView tv_checkMatchPw;


    public ChangePasswordFragment() {
    }

    // newInstance constructor for creating fragment with arguments
    public static ChangePasswordFragment newInstance(int num) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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

        View view = inflater.inflate(R.layout.fragment_userinfo_second, container, false);
        ButterKnife.bind(this, view);
        instance = this;

        edtxt_inputAfterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*입력할 때 마다 비밀번호 일치 체크*/
                if (edtxt_inputAfterPassword.getText().toString().equals(edtxt_inputMatchAfterPassword.getText().toString())) {

                    tv_checkMatchPw.setText(getString(R.string.ChangePasswordFragment_text_passwordMatch));

                } else {

                    tv_checkMatchPw.setText(getString(R.string.ChangePasswordFragment_text_passwordNotMatch));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        edtxt_inputMatchAfterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*입력할 때 마다 비밀번호 일치 체크*/
                if (edtxt_inputAfterPassword.getText().toString().equals(edtxt_inputMatchAfterPassword.getText().toString())) {

                    tv_checkMatchPw.setText(getString(R.string.ChangePasswordFragment_text_passwordMatch));

                } else {

                    tv_checkMatchPw.setText(getString(R.string.ChangePasswordFragment_text_passwordNotMatch));
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

        if (getView() == null) {
            return;
        }


    }   //  onResume

    @Override
    public void onStop() {
        super.onStop();
    }


    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked() {

        if (checkValidation()) {

            changePassword();

        }

    }

    public void changePassword() {

        String userpw = edtxt_inputAfterPassword.getText().toString();
        int idx = UserInfo.getInstance().idx;
        String edit_ip = Utils.getIpAddress();
        String vteacher_id = UserInfo.getInstance().vteacherId;

        apiService.changePassword(userpw, idx, edit_ip, vteacher_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();
                        Constant.LOG(TAG, result);
                        if(result.equals("success")){
                            showSnackbar(getView(), getString(R.string.changePassword_msg_fin_01));
                            edtxt_inputBeforePassword.setText("");
                            edtxt_inputAfterPassword.setText("");
                            edtxt_inputMatchAfterPassword.setText("");

                            PreferenceManager.setString(getActivity(), "userpw", userpw);
                            UserInfo.getInstance().userpw = PreferenceManager.getString(getActivity(), "userpw");

                        } else{

                            showSnackbar(getView(), getString(R.string.changePassword_msg_err_01));

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                        showSnackbar(getView(), getString(R.string.changePassword_msg_err_01));

                    }

                } else {

                    showSnackbar(getView(), getString(R.string.changePassword_msg_err_01));

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showSnackbar(getView(), getString(R.string.changePassword_msg_err_02));

            }

        });

    }


    /*유효성 검사*/
    private boolean checkValidation() {
        boolean check = false;

        String inputBeforeUserPW = edtxt_inputBeforePassword.getText().toString() + "";
        String inputAfterUserPW = edtxt_inputAfterPassword.getText().toString() + "";
        String inputAfterUserMatchPW = edtxt_inputMatchAfterPassword.getText().toString() + "";
        String userpw = UserInfo.getInstance().userpw;


        /*기존 비밀번호 : 입력 체크*/
        if (inputBeforeUserPW.length() == 0) {

            showSnackbar(getView(), getString(R.string.ChangePasswordFragment_msg_inputPW_err));

            /*기존 비밀번호 : 6자리 이상*/
        } else if (inputBeforeUserPW.length() < 6) {

            Constant.LOG(TAG, "기존 비밀번호 6자리 이상");
            showSnackbar(getView(), getString(R.string.ChangePasswordFragment_msg_inputPWCount_err));

            /*기존 비밀번호 : 영문 소문자 숫자 특수문자 각각 1개이상*/
        } else if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{6,15}$", inputBeforeUserPW)) {

            Constant.LOG(TAG, "기존 영어숫자특수문자 조합 6~15 가능");
            showSnackbar(getView(), getString(R.string.ChangePasswordFragment_msg_inputPWPattern_err));

        } else if (!inputBeforeUserPW.equals(userpw)) {

            showSnackbar(getView(), getString(R.string.ChangePasswordFragment_msg_beforePWNotMatch_err));

            /*변경할 비밀번호 : 입력 체크*/
        } else if (inputAfterUserPW.length() == 0) {

            showSnackbar(getView(), getString(R.string.ChangePasswordFragment_msg_inputAfterPW_err));

            /*변경할 비밀번호 : 6자리 이상*/
        } else if (inputAfterUserPW.length() < 6) {

            Constant.LOG(TAG, "비밀번호 6자리 이상");
            showSnackbar(getView(), getString(R.string.ChangePasswordFragment_msg_inputAfterPWCount_err));

            /*변경할 비밀번호 : 영문 소문자 숫자 특수문자 각각 1개이상*/
        } else if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{6,15}$", inputAfterUserPW)) {

            Constant.LOG(TAG, "변경할 비밀번호 영어숫자특수문자 조합 6~15 가능");
            showSnackbar(getView(), getString(R.string.ChangePasswordFragment_msg_inputAfterPWPattern_err));

            /*비밀번호 확인 : 입력 체크*/
        } else if (inputAfterUserMatchPW.length() == 0) {

            showSnackbar(getView(), getString(R.string.ChangePasswordFragment_msg_inputMatchPW_err));
            /*비밀번호 확인 : 일치 체크*/
        } else if (!inputAfterUserPW.equals(inputAfterUserMatchPW)) {

            showSnackbar(getView(), getString(R.string.ChangePasswordFragment_msg_matchPW_err));

        } else {

            check = true;

        }

        return check;

    }


}
