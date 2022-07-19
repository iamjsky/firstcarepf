package kr.firstcare.android.app.ui.fragment.signup.typev;

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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.adapter.DefaultSpinnerAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.SignUp;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.AgencyListModel;
import kr.firstcare.android.app.model.SignUpModel;
import kr.firstcare.android.app.ui.dialog.SearchAgencyDialog;
import kr.firstcare.android.app.ui.dialog.SearchAddressDialog;
import kr.firstcare.android.app.ui.fragment.BaseFragment;
import kr.firstcare.android.app.ui.fragment.signup.SignUpFinFragmentDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ClassName            TypeVSignUpSecondFragment
 * Created by JSky on   2020-06-12
 * <p>
 * Description          회원가입 프래그먼트 2
 */
public class TypeVSignUpSecondFragment extends BaseFragment {
    public static final String TAG = "fc_debug";
    private int frag_num;
    public static TypeVSignUpSecondFragment instance;

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

    public TypeVSignUpSecondFragment() {
    }

    // newInstance constructor for creating fragment with arguments
    public static TypeVSignUpSecondFragment newInstance(int num) {
        TypeVSignUpSecondFragment fragment = new TypeVSignUpSecondFragment();
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

        View view = inflater.inflate(R.layout.fragment_typev_signup_second, container, false);
        ButterKnife.bind(this, view);
        instance = this;


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
    public void onStop() {
        super.onStop();
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
        searchAddressDialog = new SearchAddressDialog(getActivity(), getString(R.string.Dialog_title_searchAddress), Constant.SEARCH_ADDRESS_URL, searchAddressCloseButtonListener, 0);
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
        searchAgencyDialog = new SearchAgencyDialog(getActivity(), getString(R.string.Dialog_title_searchAgency), searchAgencyCloseButtonListener, 0);
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

    /*다음 클릭*/
    @OnClick(R.id.btn_next)
    public void btn_NextClicked() {
        Constant.LOG(TAG, selectedIdx.toString());

        if(checkValidation()){

            userSignUp();

        }

    }

    /*회원가입 데이터 입력*/
    private void userSignUp() {

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
        String sex_flag = sp_selectGender.getSelectedItem().toString();

        if (sex_flag.equals("남자")) {

            sex_flag = "m";

        } else {

            sex_flag = "f";

        }

        Constant.LOG(TAG, "sex_flag" + sex_flag);

        SignUp.getInstance().email = email;
        SignUp.getInstance().emailFlag = email_flag;
        SignUp.getInstance().addr = addr;
        SignUp.getInstance().addrDetail = addr_detail;
        SignUp.getInstance().zipcode = zipcode;
        SignUp.getInstance().sexFlag = sex_flag;

        String vteacherId = SignUp.getInstance().vteacherId;
        String userPw = SignUp.getInstance().userPw;
        String vteacherName = SignUp.getInstance().vteacherName;
        String hp = SignUp.getInstance().hp;
        String nowDate = Utils.getNowDateTime();
        String ip = Utils.getIpAddress();

        apiService.userSignUp(vteacherId, userPw, vteacherName, hp, email, email_flag,
                "y", sex_flag, zipcode, addr, addr_detail, "", "",
                "", "", "0000-00-00 00:00:00", "", nowDate, vteacherId,
                ip, selectedIdx).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        assert response.body() != null;
                        String result = response.body().string();
                        Constant.LOG(TAG, "result : " + result);

                        if (result.equals("success")) {

                            SignUpFinFragmentDialog signUpFinFragmentDialog = new SignUpFinFragmentDialog();
                            signUpFinFragmentDialog.show(getActivity().getSupportFragmentManager(), "SignUpFinFragmentDialog");

                        } else {

                            showSnackbar(getView(), getString(R.string.userSignUp_msg_err_01));

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        showSnackbar(getView(), getString(R.string.userSignUp_msg_err_02));
                    }


                } else {
                    Constant.LOG(TAG, "userSignUp response err : ");
                    showSnackbar(getView(), getString(R.string.userSignUp_msg_err_02));

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.LOG(TAG, "userSignUp err : " + t);
                showSnackbar(getView(), getString(R.string.userSignUp_msg_err_03));
            }

        });


    }

    /*유효성 검사*/
    private boolean checkValidation(){
        boolean check = false;

        String inputEmail = edtxt_inputEmail.getText().toString()+"";
        String inputAddress = edtxt_inputAddress.getText().toString()+"";
        String directEmailType = edtxt_directEmailType.getText().toString() + "";

        int selectedEmailPos = sp_selectEmailType.getSelectedItemPosition();
        int lastPosition = emailTypeArrayData.size() - 1;

        String searchAddress = btn_searchAddress.getText().toString()+"";
        String searchAgency = btn_searchAgency.getText().toString()+"";

        int selectedGenderPos = sp_selectGender.getSelectedItemPosition();

        if(inputEmail.equals("") || inputEmail.length() == 0){

            showSnackbar(getView(),getString(R.string.TypeVSignUpSecondFragment_msg_inputEmailId_err));

        }else if (!Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+$", inputEmail)) {

            Constant.LOG(TAG, "이메일 아이디 영문대소문자숫자-_.만가능");
            showSnackbar(getView(), getString(R.string.TypeVSignUpSecondFragment_msg_inputEmailIdEN_err));

        }else if(selectedEmailPos == 0){

            showSnackbar(getView(),getString(R.string.TypeVSignUpSecondFragment_msg_selectEmailDomain_err));

        }else if((directEmailType.equals("") || directEmailType.length() == 0) && selectedEmailPos == lastPosition){

            showSnackbar(getView(),getString(R.string.TypeVSignUpSecondFragment_msg_inputEmailDomain_err));

        }else if(selectedEmailPos == lastPosition && !Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}+$", directEmailType)){

            showSnackbar(getView(),getString(R.string.TypeVSignUpSecondFragment_msg_inputEmailDomainPattern_err));

        }else if(!rdbtn_emailAgree.isChecked() && !rdbtn_emailDisAgree.isChecked()){

            showSnackbar(getView(),getString(R.string.TypeVSignUpSecondFragment_msg_checkboxEmailAgree_err));

        }else if (searchAddress.equals(getString(R.string.TypeVSignUpSecondFragment_text_searchAddress)) || searchAddress.length() == 0){

            showSnackbar(getView(),getString(R.string.TypeVSignUpSecondFragment_msg_selectSearchAddress_err));

        } else if (inputAddress.equals("") || inputAddress.length() == 0){

            showSnackbar(getView(),getString(R.string.TypeVSignUpSecondFragment_msg_inputAddress_err));

        } else if(searchAgency.equals(getString(R.string.TypeVSignUpSecondFragment_text_searchAgency)) || searchAgency.length() == 0){

            showSnackbar(getView(),getString(R.string.TypeVSignUpSecondFragment_msg_selectSearchAgency_err));

        }else if(selectedGenderPos == 0){

            showSnackbar(getView(),getString(R.string.TypeVSignUpSecondFragment_msg_selectGender_err));

        } else{

            check = true;

        }

        return check;

    }


}
