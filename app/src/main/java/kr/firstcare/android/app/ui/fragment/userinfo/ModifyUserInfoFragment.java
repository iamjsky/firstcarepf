package kr.firstcare.android.app.ui.fragment.userinfo;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

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
import kr.firstcare.android.app.adapter.DefaultSpinnerAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.data.SignUp;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.FaqListModel;
import kr.firstcare.android.app.ui.dialog.CheckUserIdDialog;
import kr.firstcare.android.app.ui.dialog.SearchAddressDialog;
import kr.firstcare.android.app.ui.dialog.SearchAgencyDialog;
import kr.firstcare.android.app.ui.dialog.UserPhoneAuthDialog;
import kr.firstcare.android.app.ui.fragment.BaseFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ClassName            ModifyUserInfoFragment
 * Created by JSky on   2020-07-07
 * <p>
 * Description          기본정보 수정
 */
public class ModifyUserInfoFragment extends BaseFragment {
    private int frag_num;
    public static ModifyUserInfoFragment instance;

    /*휴대폰 본인인증 팝업*/
    private UserPhoneAuthDialog userPhoneAuthDialog;
    @BindView(R.id.btn_phoneAuth)
    Button btn_phoneAuth;

    /*이메일 선택 스피너*/
    @BindView(R.id.sp_selectEmailType)
    Spinner sp_selectEmailType;
    @BindView(R.id.sp_selectGender)
    Spinner sp_selectGender;

    /*회원정보 입력*/
    @BindView(R.id.edtxt_directEmailType)
    EditText edtxt_directEmailType;
    @BindView(R.id.btn_searchAddress)
    Button btn_searchAddress;
    @BindView(R.id.btn_searchAgency)
    Button btn_searchAgency;
    @BindView(R.id.edtxt_inputEmail)
    EditText edtxt_inputEmail;
    @BindView(R.id.edtxt_inputAddress)
    EditText edtxt_inputAddress;

    /*이메일 수신 여부 체크*/
    @BindView(R.id.rdbtn_emailAgree)
    RadioButton rdbtn_emailAgree;
    @BindView(R.id.rdbtn_emailDisAgree)
    RadioButton rdbtn_emailDisAgree;

    /*아이디 중복확인 팝업*/
    private CheckUserIdDialog checkIdPopUpDialog;

    private boolean isPhoneAuthFin = false;
    private boolean isCheckIdFin = false;

    //SpinnerAdapter
    DefaultSpinnerAdapter emailTypeSpinnerAdapter, genderSpinnerAdapter;
    List<String> emailTypeArrayData = new ArrayList<>();
    List<String> genderArrayData = new ArrayList<>();

    /*주소검색, 제공기관 검색 팝업*/
    private SearchAddressDialog searchAddressDialog;
    private SearchAgencyDialog searchAgencyDialog;

    public String selectedAddress = "";
    private ArrayList<String> selectedIdx = new ArrayList<>();
    private String zipcode = "";

    public ModifyUserInfoFragment() {
    }

    // newInstance constructor for creating fragment with arguments
    public static ModifyUserInfoFragment newInstance(int num) {
        ModifyUserInfoFragment fragment = new ModifyUserInfoFragment();
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

        View view = inflater.inflate(R.layout.fragment_userinfo_first, container, false);
        ButterKnife.bind(this, view);
        instance = this;

        /*휴대폰 인증 초기화*/
        isPhoneAuthFin = false;



        /*이메일 도메인 스피너 데이터*/
        String[] emailTypeArray = getResources().getStringArray(R.array.selectEmailType);
        for (String data : emailTypeArray) {
            emailTypeArrayData.add(data);

        }

        /*성별 스피너 데이터*/
        String[] genderArray = getResources().getStringArray(R.array.selectGender);
        for (String data : genderArray) {
            genderArrayData.add(data);

        }


        //SpinnerAdapter
        emailTypeSpinnerAdapter = new DefaultSpinnerAdapter(getActivity(), emailTypeArrayData);
        genderSpinnerAdapter = new DefaultSpinnerAdapter(getActivity(), genderArrayData);
        //SpinnerAdapter 적용
        sp_selectEmailType.setAdapter(emailTypeSpinnerAdapter);
        sp_selectEmailType.setSelection(0);
        sp_selectEmailType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtxt_directEmailType.setText("");
                if (position == emailTypeArrayData.size() - 1) {

                    TextView selectedTextView = (TextView) view.findViewById(R.id.spinnerText);
                    selectedTextView.setVisibility(View.INVISIBLE);
                    edtxt_directEmailType.setVisibility(View.VISIBLE);

                } else {

                    TextView selectedTextView = (TextView) view.findViewById(R.id.spinnerText);
                    selectedTextView.setVisibility(View.VISIBLE);
                    edtxt_directEmailType.setVisibility(View.GONE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });

        sp_selectGender.setAdapter(genderSpinnerAdapter);
        sp_selectGender.setSelection(0);
        sp_selectGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
    public void onResume() {
        super.onResume();

        /*휴대폰 인증 버튼 활성화*/
        btn_phoneAuth.setEnabled(true);

    }   //  onResume


    @Override
    public void onStop() {
        super.onStop();
    }

    /*휴대폰 본인인증 클릭*/
    @OnClick(R.id.btn_phoneAuth)
    public void btn_phoneAuthClicked(){
        if(isPhoneAuthFin) {
            Constant.LOG("dingding", "이미인증하였음");

        }else{
            userPhoneAuthDialog = new UserPhoneAuthDialog(getActivity(), getString(R.string.PhoneAuth_popup_title_01), userPhoneAuthCloseButtonListener, 2);
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

    /*주소 입력 처리*/
    public void setAddress(String zipcode, String address) {
        if (address != null && zipcode != null) {
            btn_searchAddress.setText(address);
            this.zipcode = zipcode;
        }

    }

    /*기관 입력 처리*/
    public void setAgency(String agency, ArrayList selectedIdx) {
        if (agency != null) {
            btn_searchAgency.setText(agency);
            this.selectedIdx = selectedIdx;
        }

    }

    /*도로명 주소 검색 클릭*/
    @OnClick(R.id.btn_searchAddress)
    public void btn_searchAddressClicked() {
        searchAddressDialog = new SearchAddressDialog(getActivity(), getString(R.string.Dialog_title_searchAddress), Constant.SEARCH_ADDRESS_URL,
                searchAddressCloseButtonListener, 1);
        searchAddressDialog.show();

    }

    // 도로명 주소 검색 팝업 닫기
    private View.OnClickListener searchAddressCloseButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {

            searchAddressDialog.dismiss();


        }
    };

    /*소속기관 검색 클릭*/
    @OnClick(R.id.btn_searchAgency)
    public void btn_searchAgencyClicked(){
        searchAgencyDialog = new SearchAgencyDialog(getActivity(), getString(R.string.Dialog_title_searchAgency),
                searchAgencyCloseButtonListener, 1);
        searchAgencyDialog.show();
    }

    // 소속기관 검색 팝업 닫기
    private View.OnClickListener searchAgencyCloseButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {

            searchAgencyDialog.dismiss();


        }
    };

    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked(){

        if(checkValidation()){

            modifyUserInfo();

        }

    }


    /*유효성 검사*/
    private boolean checkValidation() {
        boolean check = false;

        String inputEmail = edtxt_inputEmail.getText().toString()+"";
        String inputAddress = edtxt_inputAddress.getText().toString()+"";
        String directEmailType = edtxt_directEmailType.getText().toString() + "";

        int selectedEmailPos = sp_selectEmailType.getSelectedItemPosition();
        int lastPosition = emailTypeArrayData.size() - 1;

        String searchAddress = btn_searchAddress.getText().toString()+"";
        String searchAgency = btn_searchAgency.getText().toString()+"";

        int selectedGenderPos = sp_selectGender.getSelectedItemPosition();


        /*휴대폰 본인인증*/
        if (!isPhoneAuthFin) {

            showSnackbar(getView(), getString(R.string.ModifyUserInfoFragment_msg_phoneAuth_err));

        } else if(inputEmail.equals("") || inputEmail.length() == 0){

            showSnackbar(getView(), getString(R.string.ModifyUserInfoFragment_msg_inputEmailId_err));

        }else if (!Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+$", inputEmail)) {

            Constant.LOG(TAG, "이메일 아이디 영문대소문자숫자-_.만가능");
            showSnackbar(getView(), getString(R.string.ModifyUserInfoFragment_msg_inputEmailIdEN_err));

        } else if(selectedEmailPos == 0) {

            showSnackbar(getView(), getString(R.string.ModifyUserInfoFragment_msg_selectEmailDomain_err));

        } else if((directEmailType.equals("") || directEmailType.length() == 0) && selectedEmailPos == lastPosition){

            showSnackbar(getView(),getString(R.string.ModifyUserInfoFragment_msg_inputEmailDomain_err));

        }else if(selectedEmailPos == lastPosition && !Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}+$", directEmailType)){

            showSnackbar(getView(),getString(R.string.ModifyUserInfoFragment_msg_inputEmailDomainPattern_err));

        }else if(!rdbtn_emailAgree.isChecked() && !rdbtn_emailDisAgree.isChecked()){

            showSnackbar(getView(),getString(R.string.ModifyUserInfoFragment_msg_checkboxEmailAgree_err));

        }else if (searchAddress.equals(getString(R.string.ModifyUserInfoFragment_text_searchAddress)) || searchAddress.length() == 0){

            showSnackbar(getView(),getString(R.string.ModifyUserInfoFragment_msg_selectSearchAddress_err));

        } else if (inputAddress.equals("") || inputAddress.length() == 0){

            showSnackbar(getView(),getString(R.string.ModifyUserInfoFragment_msg_inputAddress_err));

        } else if(searchAgency.equals(getString(R.string.ModifyUserInfoFragment_text_searchAgency)) || searchAgency.length() == 0){

            showSnackbar(getView(),getString(R.string.ModifyUserInfoFragment_msg_selectSearchAgency_err));

        }else if(selectedGenderPos == 0){

            showSnackbar(getView(),getString(R.string.ModifyUserInfoFragment_msg_selectGender_err));

        } else {

            check = true;

        }

        return check;

    }


    public void modifyUserInfo() {

        int idx = UserInfo.getInstance().idx;
        String hp =  UserInfo.getInstance().hp + "";
        String email = edtxt_inputEmail.getText().toString() + "";

        int selectedEmailPos = sp_selectEmailType.getSelectedItemPosition();
        int lastPosition = emailTypeArrayData.size() - 1;

        String directEmailType = edtxt_directEmailType.getText().toString() + "";

        if(selectedEmailPos == lastPosition
                && !directEmailType.equals("")){

            email = email + "@" + directEmailType;

        } else if (selectedEmailPos != 0 && selectedEmailPos != lastPosition) {

            email = email + "@" + sp_selectEmailType.getSelectedItem().toString();

        }

        String email_flag;

        if (rdbtn_emailAgree.isChecked()) {

            email_flag = "y";

        } else if (rdbtn_emailDisAgree.isChecked()) {

            email_flag = "n";

        } else {

            email_flag = "n";

        }

        String addr = btn_searchAddress.getText().toString() + "";
        String addr_detail = edtxt_inputAddress.getText().toString() + "";


        String edit_date = Utils.getNowDateTime();
        String edit_id = SignUp.getInstance().vteacherId;
        String edit_ip = Utils.getIpAddress();


        apiService.modifyUserInfo(idx, hp, email, email_flag, zipcode, addr, addr_detail, edit_date,
                edit_id, edit_ip, selectedIdx).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();
                        Constant.LOG(TAG, result);

                        showSnackbar(getView(), getString(R.string.modifyUserInfo_msg_fin_01));

                        setPhoneAuthFin(false);
                        edtxt_inputEmail.setText("");
                        rdbtn_emailAgree.setChecked(false);
                        rdbtn_emailDisAgree.setChecked(false);
                        edtxt_inputAddress.setText("");
                        edtxt_directEmailType.setText("");
                        sp_selectEmailType.setSelection(0);
                        btn_searchAddress.setText(getString(R.string.ModifyUserInfoFragment_text_searchAddress));
                        btn_searchAgency.setText(getString(R.string.ModifyUserInfoFragment_text_searchAgency));
                        sp_selectGender.setSelection(0);


                    } catch (IOException e) {
                        e.printStackTrace();
                        showSnackbar(getView(), getString(R.string.modifyUserInfo_msg_err_01));

                    }

                } else {

                    showSnackbar(getView(), getString(R.string.modifyUserInfo_msg_err_01));

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showSnackbar(getView(), getString(R.string.modifyUserInfo_msg_err_02));

            }

        });

    }


}
