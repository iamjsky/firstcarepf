package kr.firstcare.android.app.ui.fragment.login;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.DefaultSpinnerAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.ui.activity.LoginActivity;
import kr.firstcare.android.app.ui.dialog.DefaultPopUpDialog;
import kr.firstcare.android.app.ui.dialog.FindPasswordDialog;
import kr.firstcare.android.app.ui.dialog.UserPhoneAuthDialog;
import kr.firstcare.android.app.ui.fragment.BaseDialogFragment;

/**
 * ClassName            FindPWFragment
 * Created by JSky on   2020-06-12
 * <p>
 * Description          비밀번호 찾기
 */

public class FindPWFragment extends BaseDialogFragment {
    public static FindPWFragment instance;
    private Fragment fragment;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.sp_selectType)
    Spinner sp_selectType;

    @BindView(R.id.edtxt_inputId)
    EditText edtxt_inputId;
    @BindView(R.id.edtxt_inputName)
    EditText edtxt_inputName;

    @BindView(R.id.btn_phoneAuth)
    Button btn_phoneAuth;

    /*휴대폰 본인 인증 팝업*/
    private UserPhoneAuthDialog userPhoneAuthDialog;

    /*권한 선택 스피너 (아이템 디자인이 없어서 기본 디자인 적용)*/
    DefaultSpinnerAdapter typeSpinnerAdapter;
    List<String> typeArrayData = new ArrayList<>();

    /*메시지 팝업*/
    private FindPasswordDialog findPasswordDialog;

    private boolean isPhoneAuthFin = false;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }

    }   //  onStart

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("CheckSignUpFragment");

    }   //  onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_find_pw, container, false);
        ButterKnife.bind(this, view);
        instance = this;
        isPhoneAuthFin = false;

        /*권한 선택스피너 아이템 데이터*/
        String[] typeArray = getResources().getStringArray(R.array.selectType);
        for(String data : typeArray){
            typeArrayData.add(data);

        }

        /*권한 선택 스피너*/
        //SpinnerAdapter
        typeSpinnerAdapter = new DefaultSpinnerAdapter(getActivity(), typeArrayData);
        //SpinnerAdapter 적용
        sp_selectType.setAdapter(typeSpinnerAdapter);
        sp_selectType.setSelection(0);
        sp_selectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  selectedType = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });

        return view;

    }   //  onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);

    }   //  onViewCreated


    @Override
    public void onStop() {
        super.onStop();
    }

    /*뒤로가기 클릭*/
    @OnClick(R.id.iv_back)
    public void iv_backClicked(){
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /*휴대폰 본인 인증 클릭*/
    @OnClick(R.id.btn_phoneAuth)
    public void btn_phoneAuthClicked(){
        if(isPhoneAuthFin) {
            Constant.LOG("dingding", "이미인증하였음");
        }else{
            userPhoneAuthDialog = new UserPhoneAuthDialog(getActivity(), getString(R.string.PhoneAuth_popup_title_01), userPhoneAuthCloseButtonListener, 0);
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

    /*확인 클릭*/
    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked(){
        if(checkValidation()){
            /*임시데이터*/
            String inputId = edtxt_inputId.getText().toString()+"";
            String inputName = edtxt_inputName.getText().toString() + "";
            String hp = UserInfo.getInstance().hp;

            StringBuilder sb = new StringBuilder();
            sb.append(sp_selectType.getSelectedItem());
            sb.append("///" + inputId);
            sb.append("///" + inputName);
            sb.append("///" + hp);
            /*          */

            findPasswordDialog = new FindPasswordDialog(getActivity(), getString(R.string.FindPWFragment_popup_title_01), sb.toString(), findPWCloseButtonListener);
            findPasswordDialog.show();

        }

    }

    // 비밀번호 찾기 팝업 닫기
    private View.OnClickListener findPWCloseButtonListener = new View.OnClickListener(){
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {
            findPasswordDialog.dismiss();
            setPhoneAuthFin(false);
        }

    };

    /*휴대폰 인증 완료 처리*/
    public void setPhoneAuthFin(boolean isFin){
        Constant.LOG(TAG, "CHECK");
        isPhoneAuthFin = isFin;

        if(isFin) {

            btn_phoneAuth.setText(getString(R.string.PhoneAuth_text_authFin));
            btn_phoneAuth.setEnabled(false);

        }else{

            btn_phoneAuth.setText(getString(R.string.PhoneAuth_text_authDefault));
            btn_phoneAuth.setEnabled(true);

        }

    }

    /*유효성 검사*/
    private boolean checkValidation(){
        boolean result = false;

        String inputId = edtxt_inputId.getText().toString()+"";
        String inputName = edtxt_inputName.getText().toString()+"";

        /*권한 선택 스피너 확인*/
        if(sp_selectType.getSelectedItemPosition() == 0) {

            ((LoginActivity)getActivity()).showSnackbar(getView(), getString(R.string.FindPWFragment_msg_selectUserType));

            /*아이디 : 입력 확인*/
        } else if (inputId.equals("") || inputId.length() == 0) {

            ((LoginActivity)getActivity()).showSnackbar(getView(), getString(R.string.FindPWFragment_msg_inputId_err));

            /*아이디 : 네 글자 이상*/
        } else if (inputId.length() < 6) {

            ((LoginActivity)getActivity()).showSnackbar(getView(), getString(R.string.FindPWFragment_msg_inputIdCount_err));
            Constant.LOG(TAG, "아이디 6자리 이상");

            /*아이디 : 영문 숫자 혼합*/
        } else if (!Pattern.matches("^[a-zA-Z0-9]+$", inputId)) {

            Constant.LOG(TAG, "아이디 영문 숫자만가능");
            ((LoginActivity)getActivity()).showSnackbar(getView(), getString(R.string.FindPWFragment_msg_inputIdPattern_err));

            /*이름 : 한글만 입력 가능*/
        } else if(inputName.equals("") || !Pattern.matches("^[가-힣]*$", inputName)){

            ((LoginActivity) getActivity()).showSnackbar(getView(), getString(R.string.FindPWFragment_msg_inputName_err));
            Constant.LOG(TAG, "이름 한글만 가능");

            /*휴대폰 인증 : 인증여부 체크 */
        }else if (!isPhoneAuthFin) {

            ((LoginActivity) getActivity()).showSnackbar(getView(), getString(R.string.FindPWFragment_msg_phoneAuth_err));

        }else{

            result = true;

        }

        return result;

    }

}
