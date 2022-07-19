package kr.firstcare.android.app.ui.fragment.login;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import kr.firstcare.android.app.ui.activity.LoginActivity;
import kr.firstcare.android.app.ui.dialog.DefaultPopUpDialog;
import kr.firstcare.android.app.ui.dialog.FindUserIdDialog;
import kr.firstcare.android.app.ui.fragment.BaseDialogFragment;

/**
 * ClassName            FindIDFragment
 * Created by JSky on   2020-06-12
 * <p>
 * Description          아이디 찾기
 */

public class FindIDFragment extends BaseDialogFragment {
    private Fragment fragment;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.sp_selectType)
    Spinner sp_selectType;

    @BindView(R.id.edtxt_inputName)
    EditText edtxt_inputName;
    @BindView(R.id.edtxt_inputPhoneNumber)
    EditText edtxt_inputPhoneNumber;

    /*권한 선택 스피너 (아이템 디자인이 없어서 기본 디자인 적용)*/
    DefaultSpinnerAdapter typeSpinnerAdapter;
    List<String> typeArrayData = new ArrayList<>();

    /*메시지 팝업*/
    private FindUserIdDialog findUserIdDialog;

    /*다이얼로그 전체 화면 설정*/
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

        View view = inflater.inflate(R.layout.fragment_find_id, container, false);
        ButterKnife.bind(this, view);

        /*권한 선택 스피너 아이템 데이터*/
        String[] typeArray = getResources().getStringArray(R.array.selectType);
        for(String data : typeArray){
            typeArrayData.add(data);

        }

        /*권한 선택 스피너*/
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

    // 아이디 찾기 팝업 닫기
    private View.OnClickListener findIDCloseButtonListener = new View.OnClickListener(){
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {
            findUserIdDialog.dismiss();

        }

    };

    /*유효성 검사*/
    private boolean checkValidation() {
        boolean result = false;

        String inputName = edtxt_inputName.getText().toString() + "";
        String inputPhoneNum = edtxt_inputPhoneNumber.getText().toString() + "";
        inputPhoneNum = PhoneNumberUtils.formatNumber(inputPhoneNum);

        Constant.LOG("dingding", inputPhoneNum);
        /*권한 선택 스피너 확인*/
        if (sp_selectType.getSelectedItemPosition() == 0) {

            ((LoginActivity) getActivity()).showSnackbar(getView(), getString(R.string.FindIDFragment_msg_selectUserType));

            /*이름 : 한글만 입력 가능*/
        } else if (inputName.equals("") || !Pattern.matches("^[가-힣]*$", inputName)) {

            ((LoginActivity) getActivity()).showSnackbar(getView(), getString(R.string.FindIDFragment_msg_inputName_err));

            /*휴대폰 : 010~1 - 3~4자리 - 4자리*/
        } else if (inputPhoneNum.equals("") ||
                !Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", inputPhoneNum)) {

            ((LoginActivity) getActivity()).showSnackbar(getView(), getString(R.string.FindIDFragment_msg_inputPhoneNumber_err));

        } else {

            result = true;

        }

        return result;
    }

    /*확인 클릭*/
    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked() {
        if (checkValidation()) {
            /*임시 데이터*/
            String inputName = edtxt_inputName.getText().toString() + "";
            String inputPhoneNum = edtxt_inputPhoneNumber.getText().toString() + "";

            StringBuilder sb = new StringBuilder();
            sb.append(sp_selectType.getSelectedItem());
            sb.append("///" + inputName);
            sb.append("///" + inputPhoneNum);
            /*          */

            findUserIdDialog = new FindUserIdDialog(getActivity(), getString(R.string.FindIDFragment_popup_title_01), sb.toString(), findIDCloseButtonListener);
            findUserIdDialog.show();

        }

    }




}
