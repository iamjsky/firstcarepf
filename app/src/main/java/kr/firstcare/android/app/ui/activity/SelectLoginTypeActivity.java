package kr.firstcare.android.app.ui.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.ui.dialog.SearchAgencyDialog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;


/**
 * ClassName            SelectLoginTypeActivity
 * Created by JSky on   2020-06-03
 *
 * Description          로그인 하는 타입(권한) 선택
 */

public class SelectLoginTypeActivity extends BaseActivity {

    @BindView(R.id.btn_TypeG)
    TextView btn_TypeG;
    @BindView(R.id.btn_TypeN)
    TextView btn_TypeN;
    @BindView(R.id.btn_TypeV)
    TextView btn_TypeV;

    /**
     * selectType
     * -1 : err
     * 0 : none
     * 1 : Type G (대상자보호자)
     * 2 : Type N (케어 선생님)
     * 3 : Type V (제공인력)
     */

    private int selectLoginType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_login_type);
        ButterKnife.bind(this);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

    }   //  onCreate

    @Override
    protected void onResume() {
        super.onResume();
        setSelectType(-1);

    }   //  onResume





    // 6월 선출시 이후 수정
    /*대상자 보호자 클릭*/
    @OnClick(R.id.btn_TypeG)
    public void btn_TypeGClicked(){
//        btn_TypeN.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
//        btn_TypeV.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
//        if(selectType == 1) {
//            btn_TypeG.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
//            selectType = 0;
//        }else{
//            btn_TypeG.setBackground(getDrawable(R.drawable.bg_select_type_on_01));
//            selectType = 1;
//        }
    }

    // 6월 선출시 이후 수정
    /*케어선생님 클릭*/
    @OnClick(R.id.btn_TypeN)
    public void btn_TypeNClicked(){
//        btn_TypeG.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
//        btn_TypeV.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
//        if(selectType == 2) {
//            btn_TypeN.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
//            selectType = 0;
//        }else{
//            btn_TypeN.setBackground(getDrawable(R.drawable.bg_select_type_on_01));
//            selectType = 2;
//        }

    }

    /*제공인력 클릭*/
    @OnClick(R.id.btn_TypeV)
    public void btn_TypeVClicked(){
//        btn_TypeG.setBackground(getDrawable(R.drawable.bg_type_select_disable_01));
//        btn_TypeG.setTextColor(getColor(R.color.Gray_Cloud));
//        btn_TypeN.setBackground(getDrawable(R.drawable.bg_type_select_disable_01));
//        btn_TypeN.setTextColor(getColor(R.color.Gray_Cloud));

        setSelectType(3);

    }

    /*다음 클릭*/
    @OnClick(R.id.btn_Next)
    public void btn_NextClicked() {
        if (selectLoginType == -1) {

            showSnackbar(getString(R.string.SelectLoginTypeActivity_msg_selectType_err));

        }else{

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

        }

    }

    SearchAgencyDialog  searchAgencyDialog;
    /*회원가입 클릭*/
    @OnClick(R.id.tv_signUp)
    public void tv_signUpClicked() {
//        Constant.LOG(TAG, "TOUCH");
        Intent intent = new Intent(this, SelectSignUpTypeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }

    // 소속기관 검색 팝업 닫기
    private View.OnClickListener searchAgencyCloseButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {

            searchAgencyDialog.dismiss();


        }
    };

    /*권한 선택 동작*/
    private void setSelectType(int code) {
        switch (code) {

            case -1:
                btn_TypeG.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
                btn_TypeG.setTextColor(getColor(R.color.fc_typeSelectOffTextColor));
                btn_TypeN.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
                btn_TypeN.setTextColor(getColor(R.color.fc_typeSelectOffTextColor));
                btn_TypeV.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
                btn_TypeV.setTextColor(getColor(R.color.fc_typeSelectOffTextColor));
                selectLoginType = -1;

                break;

            case 1:
                if (selectLoginType == code) {

                    btn_TypeG.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
                    btn_TypeG.setTextColor(getColor(R.color.fc_typeSelectOffTextColor));
                    selectLoginType = -1;

                } else {

                    btn_TypeG.setBackground(getDrawable(R.drawable.bg_select_type_on_01));
                    btn_TypeG.setTextColor(getColor(R.color.fc_boldColor));
                    selectLoginType = code;

                }

                break;

            case 2:
                if (selectLoginType == code) {

                    btn_TypeN.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
                    btn_TypeN.setTextColor(getColor(R.color.fc_typeSelectOffTextColor));
                    selectLoginType = -1;

                } else {

                    btn_TypeN.setBackground(getDrawable(R.drawable.bg_select_type_on_01));
                    btn_TypeN.setTextColor(getColor(R.color.fc_boldColor));
                    selectLoginType = code;

                }

                break;

            case 3:
                if (selectLoginType == code) {

                    btn_TypeV.setBackground(getDrawable(R.drawable.bg_select_type_off_01));
                    btn_TypeV.setTextColor(getColor(R.color.fc_typeSelectOffTextColor));
                    selectLoginType = -1;

                } else {

                    btn_TypeV.setBackground(getDrawable(R.drawable.bg_select_type_on_01));
                    btn_TypeV.setTextColor(getColor(R.color.fc_boldColor));
                    selectLoginType = code;

                }

                break;

        }

        /*로그인 권한 저장*/
        PreferenceManager.setInt(mContext, "selectLoginType", selectLoginType);
        UserInfo.getInstance().selectLoginType = selectLoginType;

    }

}
