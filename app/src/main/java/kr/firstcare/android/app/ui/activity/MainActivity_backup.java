package kr.firstcare.android.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.firstcare.android.app.GpsTracker;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.adapter.CommuteInfoAdapter;
import kr.firstcare.android.app.adapter.ServiceSpinnerAdapter;
import kr.firstcare.android.app.adapter.UserAgencySpinnerAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.CommuteInfoListModel;
import kr.firstcare.android.app.model.ProgramListModel;
import kr.firstcare.android.app.model.UserAgencyListModel;
import kr.firstcare.android.app.model.UserAttendanceListModel;
import kr.firstcare.android.app.model.UserModel;
import kr.firstcare.android.app.ui.dialog.CommuteConfirmDialog;
import kr.firstcare.android.app.ui.dialog.CommuteConfirmDialog_backup;
import kr.firstcare.android.app.ui.dialog.DatePickerDialog;
import kr.firstcare.android.app.ui.dialog.SelectPlaceDialog;
import kr.firstcare.android.app.ui.dialog.SelectProfileImageDialog;
import kr.firstcare.android.app.ui.dialog.UserAttendanceDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * ClassName            MainActivity
 * Created by JSky on   2020-06-19
 *
 * Description          메인화면, 출근 방식이 다른 메인 (백업)
 */

public class MainActivity_backup extends BaseActivity {

    private static Context context;
    public static Context getContext() {
        return context;
    }

    /*상단 메뉴*/
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_progressBar)
    LinearLayout layout_progressBar;

    /*출퇴근 기록하기*/
    @BindView(R.id.sp_selectService)
    Spinner sp_selectService;
    @BindView(R.id.sp_selectAgency)
    Spinner sp_selectAgency;
    @BindView(R.id.btn_getToWork)
    Button btn_getToWork;
    @BindView(R.id.btn_finishWork)
    Button btn_finishWork;


    /*네비게이션메뉴*/
    @BindView(R.id.tv_userName)
    TextView tv_userName;
    @BindView(R.id.tv_userType)
    TextView tv_userType;
    @BindView(R.id.iv_profile)
    CircleImageView iv_profile;
    @BindView(R.id.layout_navHeader_typeV)
    LinearLayout layout_navHeader_typeV;
    @BindView(R.id.layout_navMenu_typeV)
    LinearLayout layout_navMenu_typeV;

    /*출퇴근 상세 내용*/
    @BindView(R.id.rv_commuteListView)
    RecyclerView rv_commuteListView;
    @BindView(R.id.rv_refreshLayout)
    TwinklingRefreshLayout rv_refreshLayout;

    //SpinnerAdapter
    ServiceSpinnerAdapter serviceSpinnerAdapter;
    List<ProgramListModel> serviceArrayData = new ArrayList<>();

    UserAgencySpinnerAdapter userAgencySpinnerAdapter;
    List<UserAgencyListModel> userAgencyArrayData = new ArrayList<>();

    private CommuteInfoAdapter commuteInfoAdapter;

    CommuteConfirmDialog_backup commuteConfirmDialog;
    DatePickerDialog datePickerDialog;
    SelectProfileImageDialog selectProfileImageDialog;
    UserAttendanceDialog userAttendanceDialog;
    SelectPlaceDialog selectPlaceDialog;

    /*GPS*/
    private GpsTracker gpsTracker;
    private double nowLatitude = 0;
    private double nowLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        gpsTracker = new GpsTracker(mContext);

        rv_commuteListView.setLayoutManager(new LinearLayoutManager(this));
        commuteInfoAdapter = new CommuteInfoAdapter(this);

        /*이전 기록 조회*/
        commuteInfoAdapter.setOnDatePickerButtonClickListener(datePickerButtonClickListener);
        /*출근 위치 확인*/
        commuteInfoAdapter.setOnGetToWorkButtonClickListener(getToWorkButtonClickListener);
        /*퇴근 위치 확인*/
        commuteInfoAdapter.setOnFinishWorkButtonClickListener(finishWorkButtonClickListener);
        /*이용자 출석 관리*/
        commuteInfoAdapter.setOnUserRollListButtonClickListener(userRollListButtonClickListener);

        rv_commuteListView.setAdapter(commuteInfoAdapter);

        ProgressLayout headerView = new ProgressLayout(this);
        rv_refreshLayout.setHeaderView(headerView);
        rv_refreshLayout.setEnableOverScroll(false);
        rv_refreshLayout.setEnableKeepIView(false);
        rv_refreshLayout.setEnableLoadmore(false);

        /*위로 드래그 해서 리스트새로고침*/
        rv_refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();

                    }
                },1000);

            }
        });


    }   //  onCreate

    @Override
    protected void onResume() {
        super.onResume();

        refresh();

    }   //  onResume

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);

        } else {

            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            /*뒤로 버튼으로 앱 종료*/
            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {

                super.onBackPressed();

            } else {

                backPressedTime = tempTime;
                showSnackbar(getString(R.string.MainActivity_msg_appFinish));

            }

        }

    }

    /*네비 메뉴 햄버거 버튼*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*각종 데이터 파싱 새로고침*/
    public void refresh(){
        UserInfo.getInstance().userIp = Utils.getIpAddress();
        getCommuteInfo(UserInfo.getInstance().idx);
        getUserAgencyList(UserInfo.getInstance().idx);
        getUserInfo(UserInfo.getInstance().vteacherId);

    }

    /*로그인 타입에 따른 네비 메뉴 세팅*/
    private void setNavMenu(){

        String userName = UserInfo.getInstance().vteacherName+"";
        tv_userName.setText(userName);

        switch (UserInfo.getInstance().selectLoginType)
        {
            case 0:

                break;

            /*대상자 보호자일 경우*/
            case 1:

                tv_userType.setText(getString(R.string.MainActivity_text_typeG));
                layout_navHeader_typeV.setVisibility(View.GONE);
                layout_navMenu_typeV.setVisibility(View.GONE);

                break;

            /*케어선생님일 경우*/
            case 2:

                tv_userType.setText(getString(R.string.MainActivity_text_typeN));
                layout_navHeader_typeV.setVisibility(View.GONE);
                layout_navMenu_typeV.setVisibility(View.GONE);

                break;

            /*제공인력일 경우*/
            case 3:

                tv_userType.setText(getString(R.string.MainActivity_text_typeV));
                layout_navHeader_typeV.setVisibility(View.VISIBLE);
                layout_navMenu_typeV.setVisibility(View.VISIBLE);

                break;
        }

        /*네비메뉴의 프로필 사진*/
        String profileImgUrl = Constant.TEACHER_PROFILE_IMG_URL + UserInfo.getInstance().photo+"";
        if(!profileImgUrl.equals(Constant.TEACHER_PROFILE_IMG_URL)) {

            Glide.with(mContext).load(profileImgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_profile);

        }else{

            iv_profile.setImageResource(R.color.fc_mainColor);

        }

    }

    /*기본 정보 클릭*/
    @OnClick(R.id.layout_userInfo_typeV)
    public void layout_userInfo_typeVClicked() {
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    /*로그아웃 클릭*/
    @OnClick(R.id.layout_logout_typeV)
    public void layout_logoutClicked() {
        userLogout();
    }

    /*서비스탈퇴 클릭*/
    @OnClick(R.id.layout_userLeave_typeV)
    public void layout_userLeaveClicked() {

    }

    /*네비메뉴 공지사항 클릭*/
    @OnClick(R.id.tv_notice_typeV)
    public void tv_notice_typeVClicked() {
        Intent intent = new Intent(this, BoardActivity.class);
//        Intent intent = new Intent(this, BoardActivity_backup.class); //  공지사항 백업
        intent.putExtra("menuType", 1);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }

    /*네비메뉴 faq 클릭*/
    @OnClick(R.id.tv_faq_typeV)
    public void tv_faq_typeVClicked() {
        Intent intent = new Intent(this, BoardActivity.class);
//        Intent intent = new Intent(this, BoardActivity_backup.class); //  FAQ 다른 방식 백업
        intent.putExtra("menuType", 2);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }

    /*네비메뉴 문의하기 클릭*/
    @OnClick(R.id.tv_support_typeV)
    public void tv_support_typeVClicked() {
        //  미구현

    }

    /*네비메뉴 햄버거 버튼*/
    @OnClick(R.id.btn_nav)
    public void btn_navClicked() {
        Constant.LOG(TAG, sp_selectAgency.getSelectedItem().toString() + " / " + sp_selectService.getSelectedItem().toString());
        mDrawerLayout.openDrawer(Gravity.RIGHT);

    }

    /*프로필사진 업로드*/
    @OnClick(R.id.btn_uploadProfileImg)
    public void btn_uploadProfileImgClicked() {
        selectProfileImageDialog = new SelectProfileImageDialog(this, selectProfileImageDialogCloseButtonListener, 0);
        selectProfileImageDialog.show();

    }

    // 프로필 사진 업로드 선택 팝업 닫기
    private View.OnClickListener selectProfileImageDialogCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectProfileImageDialog.dismiss();
            refresh();

        }

    };


    /*로딩 프로그래스바 처리*/
    public void showProgress(boolean value){
        if(value){

            layout_progressBar.setVisibility(View.VISIBLE);

        }else{

            layout_progressBar.setVisibility(View.GONE);

        }

    }


    /*출근하기 클릭*/
    // 약속장소 선택하는 출근하기 (**new**)
    @OnClick(R.id.btn_getToWork)
    public void btn_getToWorkClicked() {

        /*현재 출퇴근 상태 값*/
        int userCommuteState = UserInfo.getInstance().userCommuteState;

        /*사용자 출퇴근 정보 못 가지고 왔을 때*/
        if(userCommuteState == -1){

            showSnackbar(getString(R.string.Common_msg_network_err));

        }
        /*출근하기 누른 상태*/
        else if(userCommuteState == 1){

            showSnackbar(getString(R.string.MainActivity_msg_setAtt_err));

        }

        else if(!Utils.getPermissionStatus(this)){

            showSnackbar(getString(R.string.Common_msg_permission_err));

        }
        else if(!Utils.getGPSState(this)) {

            showSnackbar(getString(R.string.Common_msg_gps_err));

        } else if(UserInfo.getInstance().selectedSubagency_idx == -1 || UserInfo.getInstance().selectedService_idx == -1){

            showSnackbar(getString(R.string.MainActivity_msg_spinner_err2));

        } else{

            nowLatitude = gpsTracker.getLatitude();
            nowLongitude = gpsTracker.getLongitude();
            selectPlaceDialog = new SelectPlaceDialog(this, selectPlaceCloseButtonListener, nowLatitude, nowLongitude);

            selectPlaceDialog.show();

        }

    }

    /*퇴근하기 클릭 (**new**)*/
    @OnClick(R.id.btn_finishWork)
    public void btn_finishWorkClicked() {

        /*카카오 지도 중복 로딩 방지(충돌 일어남)*/
        Utils.waitTwoSec(btn_finishWork);

        /*이용자 출석 관리 체크*/
        getChildAttInfoBeforeFinishWork();



    }

    /*출근 위치 확인 팝업*/
    public void iv_getToWorkInfoClicked(String actDate, String actTime, double attLatitude, double attLongitude, String locationName, String agencyName) {
        if(!Utils.getPermissionStatus(this)){

            showSnackbar(getString(R.string.Common_msg_permission_err));

        }
        else if(!Utils.getGPSState(this)) {

            showSnackbar(getString(R.string.Common_msg_gps_err));

        } else{

            commuteConfirmDialog = new CommuteConfirmDialog_backup(this, getString(R.string.Dialog_title_getToWorkInfo), actDate, actTime,
                    attLatitude,attLongitude, commuteCloseButtonListener, 3, agencyName, locationName);

            commuteConfirmDialog.show();

        }

    }

    /*퇴근 위치 확인 팝업*/
    public void iv_finishWorkInfoClicked(String actDate, String leaveTime, double leaveLatitude, double leaveLongitude, String locationName, String agencyName) {
        if(!Utils.getPermissionStatus(this)){

            showSnackbar(getString(R.string.Common_msg_permission_err));

        }
        else if(!Utils.getGPSState(this)) {

            showSnackbar(getString(R.string.Common_msg_gps_err));

        } else{



            commuteConfirmDialog = new CommuteConfirmDialog_backup(this, getString(R.string.Dialog_title_finishWorkInfo), actDate, leaveTime,
                    leaveLatitude,leaveLongitude, commuteCloseButtonListener, 4, agencyName, locationName);

            commuteConfirmDialog.show();
        }


    }

    // 출근하기, 퇴근하기, 출퇴근 위치확인 팝업 닫기
    private View.OnClickListener commuteCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            commuteConfirmDialog.dismiss();
            refresh();

        }

    };

    /*이전 기록 조회 리스너*/
    public CommuteInfoAdapter.OnDatePickerButtonClickListener datePickerButtonClickListener = new CommuteInfoAdapter.OnDatePickerButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            tv_commuteListClicked();
            Constant.LOG(TAG, commuteInfoAdapter.getItem(position).toString());
        }
    };

    /*출근 위치 확인 리스너*/
    public CommuteInfoAdapter.OnGetToWorkButtonClickListener getToWorkButtonClickListener = new CommuteInfoAdapter.OnGetToWorkButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            CommuteInfoListModel data = commuteInfoAdapter.getItem(position);

            String actDate = data.getActDate();
            String actTime = data.getAttTime();
            String attAddr = data.getAttAddr();
            double attLatitude =  Utils.getCurrentMapPoint(mContext, attAddr, 0);
            double attLongitude = Utils.getCurrentMapPoint(mContext, attAddr, 1);
            String locationName = data.getSub_addr() + data.getSub_addr_detail() + "";
            String agencyName = data.getSubAgencyName();

            Constant.LOG(TAG, commuteInfoAdapter.getItem(position).toString());
            iv_getToWorkInfoClicked(actDate,actTime,attLatitude,attLongitude,locationName,agencyName);

        }

    };

    /*퇴근 위치 확인 리스너*/
    public CommuteInfoAdapter.OnFinishWorkButtonClickListener finishWorkButtonClickListener = new CommuteInfoAdapter.OnFinishWorkButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            CommuteInfoListModel data = commuteInfoAdapter.getItem(position);

            String actDate = data.getActDate();
            String leaveTime = data.getLeaveTime();
            String leaveAddr = data.getLeaveAddr();
            double leaveLatitude =  Utils.getCurrentMapPoint(mContext, leaveAddr, 0);
            double leaveLongitude = Utils.getCurrentMapPoint(mContext, leaveAddr, 1);
            String locationName = data.getSub_addr() + data.getSub_addr_detail() + "";
            String agencyName = data.getSubAgencyName();

            Constant.LOG(TAG, "leaveLatitude : " + leaveLatitude + " / leaveLongitude : " + leaveLongitude + " / locationName : " + locationName);

            Constant.LOG(TAG, commuteInfoAdapter.getItem(position).toString());
            iv_finishWorkInfoClicked(actDate,leaveTime,leaveLatitude,leaveLongitude,locationName,agencyName);

        }

    };

    /*이용자 출석 관리 리스너*/
    public CommuteInfoAdapter.OnUserRollListButtonClickListener userRollListButtonClickListener = new CommuteInfoAdapter.OnUserRollListButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Constant.LOG(TAG, commuteInfoAdapter.getItem(position).toString());
            CommuteInfoListModel selectData = new CommuteInfoListModel();
            selectData = commuteInfoAdapter.getItem(position);
            if(selectData.getAttIdx() != -1){
                UserInfo.getInstance().att_idx = selectData.getAttIdx();
                btn_userRollListClicked();
            }

        }

    };

    // 약속장소 선택 팝업 닫기
    private View.OnClickListener selectPlaceCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectPlaceDialog.dismiss();
            refresh();

        }

    };



    /*이전기록조회(날짜선택) 팝업*/
    public void tv_commuteListClicked(){
        datePickerDialog = new DatePickerDialog(this, getString(R.string.Dialog_title_searchCommuteList), datePickerCloseButtonListener);
        datePickerDialog.show();

    }

    // 이전기록조회 팝업 닫기
    private View.OnClickListener datePickerCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerDialog.dismiss();
            refresh();

        }

    };

    /*이용자 출석 관리 팝업*/
    public void btn_userRollListClicked(){
        userAttendanceDialog = new UserAttendanceDialog(this, getString(R.string.Dialog_title_userAttendance), userAttCloseButtonListener, 0);
        userAttendanceDialog.show();

    }

    // 이용자출석관리 팝업 닫기
    private View.OnClickListener  userAttCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userAttendanceDialog.dismiss();
            refresh();

        }

    };


    /*program_list - 프로그램 정보 가져오기*/
    public void getProgramList(int sub_agency_idx){
        int vteacher_idx = UserInfo.getInstance().idx;
        apiService.getProgramList(vteacher_idx, sub_agency_idx).enqueue(new Callback<List<ProgramListModel>>() {
            @Override
            public void onResponse(Call<List<ProgramListModel>> call, Response<List<ProgramListModel>> response) {
                if (response.isSuccessful()) {
                    List<ProgramListModel> dataList = new ArrayList<>();
                    dataList = response.body();

                    Constant.LOG(TAG, "getProgramList result : " + dataList);

                    if (dataList.size() > 0) {

                        setServiceSpinner(dataList);

                    }else{

                        showSnackbar(getString(R.string.getProgramList_msg_err_01));
                        Constant.LOG(TAG, "getProgramList data err");
                        UserInfo.getInstance().selectedService_idx = -1;
                        UserInfo.getInstance().selectedService_name = "";
                        if(serviceSpinnerAdapter != null && serviceSpinnerAdapter.getCount() > 0){
                            serviceSpinnerAdapter.clearItem();
                        }


                    }


                } else {
                    showSnackbar(getString(R.string.getProgramList_msg_err_02));
                    Constant.LOG(TAG, "getProgramList response err");
                    UserInfo.getInstance().selectedService_idx = -1;
                    UserInfo.getInstance().selectedService_name = "";
                    if(serviceSpinnerAdapter != null && serviceSpinnerAdapter.getCount() > 0){
                        serviceSpinnerAdapter.clearItem();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<ProgramListModel>> call, Throwable t) {
                showSnackbar(getString(R.string.getProgramList_msg_err_03));
                Constant.LOG(TAG, "getProgramList err : " + t.toString());
                UserInfo.getInstance().selectedService_idx = -1;
                UserInfo.getInstance().selectedService_name = "";
                if(serviceSpinnerAdapter != null && serviceSpinnerAdapter.getCount() > 0){
                    serviceSpinnerAdapter.clearItem();
                }
            }

        });

    }

    /*서비스 스피너 세팅*/
    public void setServiceSpinner(List<ProgramListModel> serviceArray) {

        if(serviceSpinnerAdapter != null && serviceSpinnerAdapter.getCount() > 0 ){
            serviceSpinnerAdapter.clearItem();

        }

        for (ProgramListModel data : serviceArray) {
            serviceArrayData.add(data);

        }

        //SpinnerAdapter
        serviceSpinnerAdapter = new ServiceSpinnerAdapter(this, R.layout.spinner_service_normal, serviceArrayData);
        serviceSpinnerAdapter.setDropDownViewResource(R.layout.spinner_service_dropdown);

        //SpinnerAdapter 적용
        sp_selectService.setAdapter(serviceSpinnerAdapter);

        /*앱 종료 후 다시 켰을 때 선택 처리*/
        int selectedPosition =  PreferenceManager.getInt(mContext, "selectedProgramPosition");

        if(selectedPosition > -1 && selectedPosition < serviceArrayData.size() && UserInfo.getInstance().selectedService_idx != -1){

            sp_selectService.setSelection(selectedPosition);

        }

        /*출퇴근상태에 따라 스피너 선택 불가 처리 (제거)*/
//        if(UserInfo.getInstance().userCommuteState == 1 && !UserInfo.getInstance().TEST_MODE){
//
//            sp_selectService.setEnabled(false);
//
//        }else{
//
//            sp_selectService.setEnabled(true);
//
//        }

        sp_selectService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();

                PreferenceManager.setInt(mContext, "selectedProgramPosition", position);
                Constant.LOG(TAG, "selectedType : " + selectedType + "/ position : " + position);

                ProgramListModel programListModel = serviceSpinnerAdapter.getItem(position);
                assert programListModel != null;
                UserInfo.getInstance().selectedService_idx = programListModel.getProgramIdx();
                UserInfo.getInstance().selectedService_name = programListModel.getTitle();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }

    /*my_asso - 사용자와 매칭된 제공기관 불러오기*/
    public void getUserAgencyList(int idx) {

        apiService.getUserAgencyList(idx).enqueue(new Callback<List<UserAgencyListModel>>() {
            @Override
            public void onResponse(Call<List<UserAgencyListModel>> call, Response<List<UserAgencyListModel>> response) {
                if (response.isSuccessful()) {
                    List<UserAgencyListModel> dataList = new ArrayList<>();
                    dataList = response.body();

                    Constant.LOG(TAG, "getUserAgencyList result : " + dataList);

                    if (dataList.size() > 0) {

                        setUserAgencySpinner(dataList);

//                        searchUserAgencyAdapter.addItem(dataList);
//                        searchUserAgencyAdapter.notifyDataSetChanged();
                    }else{

                        showSnackbar(getString(R.string.getUserAgencyList_msg_err_01));
                        UserInfo.getInstance().selectedSubagency_idx = -1;
                        UserInfo.getInstance().selectedSubagency_name = "";
                        UserInfo.getInstance().selectedSubagency_addr = "";
                        if(userAgencySpinnerAdapter != null && userAgencySpinnerAdapter.getCount() > 0){
                            userAgencySpinnerAdapter.clearItem();

                        }

                    }

                } else {

                    Constant.LOG(TAG, "getUserAgencyList response err");
                    showSnackbar(getString(R.string.getUserAgencyList_msg_err_02));
                    UserInfo.getInstance().selectedSubagency_idx = -1;
                    UserInfo.getInstance().selectedSubagency_name = "";
                    UserInfo.getInstance().selectedSubagency_addr = "";
                    if(userAgencySpinnerAdapter != null && userAgencySpinnerAdapter.getCount() > 0){
                        userAgencySpinnerAdapter.clearItem();

                    }

                }

            }

            @Override
            public void onFailure(Call<List<UserAgencyListModel>> call, Throwable t) {
                showSnackbar(getString(R.string.getUserAgencyList_msg_err_03));
                Constant.LOG(TAG, "getUserAgencyList err : " + t.toString());
                UserInfo.getInstance().selectedSubagency_idx = -1;
                UserInfo.getInstance().selectedSubagency_name = "";
                UserInfo.getInstance().selectedSubagency_addr = "";
                if(userAgencySpinnerAdapter != null && userAgencySpinnerAdapter.getCount() > 0){
                    userAgencySpinnerAdapter.clearItem();

                }

            }

        });

    }

    /*매칭 제공기관 스피너 세팅*/
    public void setUserAgencySpinner(List<UserAgencyListModel> userAgencyArray) {

        if(userAgencySpinnerAdapter != null && userAgencySpinnerAdapter.getCount() > 0 ){
            userAgencySpinnerAdapter.clearItem();

        }

        for (UserAgencyListModel data : userAgencyArray) {
            userAgencyArrayData.add(data);
            Constant.LOG(TAG, data.getSubAgencyName());

        }

        //SpinnerAdapter
        userAgencySpinnerAdapter = new UserAgencySpinnerAdapter(this, R.layout.spinner_service_normal, userAgencyArrayData);
        userAgencySpinnerAdapter.setDropDownViewResource(R.layout.spinner_service_dropdown);
        //SpinnerAdapter 적용
        sp_selectAgency.setAdapter(userAgencySpinnerAdapter);

        /*앱 종료 후 다시 켰을 때 선택 처리*/
        int selectedPosition =  PreferenceManager.getInt(mContext, "selectedAgencyPosition");
        if(selectedPosition > -1 && selectedPosition < userAgencyArrayData.size() && UserInfo.getInstance().selectedSubagency_idx != -1){
            sp_selectAgency.setSelection(selectedPosition);

        }

        /*출퇴근상태에 따라 스피너 선택 불가 처리(제거)*/
//        if(UserInfo.getInstance().userCommuteState == 1 && !UserInfo.getInstance().TEST_MODE){
//
//            sp_selectAgency.setEnabled(false);
//
//        }else{
//
//            sp_selectAgency.setEnabled(true);
//
//        }

        sp_selectAgency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                PreferenceManager.setInt(mContext, "selectedAgencyPosition",position);
                Constant.LOG(TAG, "selectedType : " + selectedType + "/ position : " + position);

                UserAgencyListModel userAgencyListModel = userAgencySpinnerAdapter.getItem(position);
                assert userAgencyListModel != null;

                UserInfo.getInstance().selectedSubagency_idx = userAgencyListModel.getIdx();
                int sub_agency_idx = UserInfo.getInstance().selectedSubagency_idx;
                UserInfo.getInstance().selectedSubagency_name = userAgencyListModel.getSubAgencyName();
                UserInfo.getInstance().selectedSubagency_addr = userAgencyListModel.getSubAddr() + userAgencyListModel.getSubAddrDetail() + "";
                getProgramList(sub_agency_idx);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });





    }

    /*insteacher_att_info - 출퇴근 정보 가져오기*/
    public void getCommuteInfo(int idx) {

        List<CommuteInfoListModel> nowDateList = new ArrayList<>();
        CommuteInfoListModel emptyData = new CommuteInfoListModel();
        emptyData.setAttIdx(-1);

        /*현재날짜 및 시간*/
        String actDate = Utils.getNowDate();
        //  String actDate = "2020-06-29";  //  test

        apiService.getCommuteInfo(idx, actDate).enqueue(new Callback<List<CommuteInfoListModel>>() {
            @Override
            public void onResponse(Call<List<CommuteInfoListModel>> call, Response<List<CommuteInfoListModel>> response) {

                if (response.isSuccessful()) {

                    List<CommuteInfoListModel> dataList = new ArrayList<>();
                    dataList = response.body();

                    String nowDate = Utils.getNowDateKR()+"";
                    //   String nowDate = "2020년06월29일";   //  test

                    Constant.LOG(TAG, "getCommuteInfo result : " + dataList);
                    if (dataList.size() > 0) {

                        Constant.LOG(TAG,"!!!!!!!!!!!!att_idx : " + dataList.get(0).getAttIdx() + "nowDate : " + nowDate + " / dataList.get(0).getActDate() : " + dataList.get(0).getActDate());
                        if (dataList.get(0).getActDate().equals(nowDate)){

                            String nowAttTime = dataList.get(0).getAttTime()+"";
                            String nowLeaveTime = dataList.get(0).getLeaveTime()+"";

                            PreferenceManager.setInt(mContext, "attendance_att_idx", dataList.get(0).getAttIdx());
                            UserInfo.getInstance().attendance_att_idx = PreferenceManager.getInt(mContext, "attendance_att_idx");

                            /*출퇴근 기록 유무 확인*/
                            if(!nowAttTime.equals("")){

                                UserInfo.getInstance().userCommuteState = 1;

                            }
                            else{

                                UserInfo.getInstance().userCommuteState = 0;

                            }

                            /*출퇴근 완료 했을 경우*/
                            if(!nowAttTime.equals("") && !nowLeaveTime.equals("00:00:00")){
                                UserInfo.getInstance().userCommuteState = 0;

                            }

                            for(CommuteInfoListModel data : dataList){
                                if (data.getActDate().equals(nowDate)) {

                                    nowDateList.add(data);

                                }

                            }

                        }else{

                            nowDateList.add(emptyData);
                            UserInfo.getInstance().userCommuteState = 0;

                        }

                    }else{

                        nowDateList.add(emptyData);
                        UserInfo.getInstance().userCommuteState = 0;

                    }

                    rv_refreshLayout.finishRefreshing();

                } else {

                    showSnackbar(getString(R.string.getCommuteInfo_msg_err_01));
                    Constant.LOG(TAG, "getCommuteInfo response err");
                    nowDateList.add(emptyData);
                    UserInfo.getInstance().userCommuteState = -1;
                    rv_refreshLayout.finishRefreshing();

                }

                commuteInfoAdapter.addItem(nowDateList);
                commuteInfoAdapter.notifyDataSetChanged();
                Constant.LOG(TAG, "nowDateList : " + nowDateList);
                Constant.LOG(TAG, "UserInfo.getInstance().userCommuteState : " + UserInfo.getInstance().userCommuteState);

            }

            @Override
            public void onFailure(Call<List<CommuteInfoListModel>> call, Throwable t) {

                Constant.LOG(TAG, "getCommuteInfo err : " + t.toString());
                nowDateList.add(emptyData);
                UserInfo.getInstance().userCommuteState = -1;
                commuteInfoAdapter.addItem(nowDateList);
                commuteInfoAdapter.notifyDataSetChanged();
                Constant.LOG(TAG, "nowDateList : " + nowDateList);
                Constant.LOG(TAG, "UserInfo.getInstance().userCommuteState : " + UserInfo.getInstance().userCommuteState);
                showSnackbar(getString(R.string.getCommuteInfo_msg_err_02));
                rv_refreshLayout.finishRefreshing();

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

                        }

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

        setNavMenu();

    }



    /*child_att_info - 이용자 출결석 조회*/
    public void getChildAttInfo(){

        int att_idx = UserInfo.getInstance().attendance_att_idx;
        UserInfo.getInstance().isFinRollList = true;
        apiService.getChildAttInfo(att_idx).enqueue(new Callback<List<UserAttendanceListModel>>() {
            @Override
            public void onResponse(Call<List<UserAttendanceListModel>> call, Response<List<UserAttendanceListModel>> response) {
                if(response.isSuccessful()){
                    List<UserAttendanceListModel> userAttendanceList = new ArrayList<>();

                    userAttendanceList = response.body();
                    Constant.LOG(TAG, "getChildAttInfo : " + userAttendanceList);

                    if(userAttendanceList.size() > 0){
                        for(int i=0; i < userAttendanceList.size(); i++){
                            if(userAttendanceList.get(i).getChildAttendanceIdx() == -1){
                                UserInfo.getInstance().isFinRollList = false;
                                break;
                            }
                        }

                    }else{

                    }


                }else{
                    showSnackbar(getString(R.string.getChildAttInfo_msg_err_01));
                    Constant.LOG(TAG, "getChildAttInfo response err");


                }

            }

            @Override
            public void onFailure(Call<List<UserAttendanceListModel>> call, Throwable t) {
                showSnackbar(getString(R.string.getChildAttInfo_msg_err_02));
                Constant.LOG(TAG, "getChildAttInfo err : " + t.toString());

            }

        });



    }


    /*child_att_info - 이용자 출결석 조회*/
    public void getChildAttInfoBeforeFinishWork(){

        int att_idx = UserInfo.getInstance().attendance_att_idx;
        UserInfo.getInstance().isFinRollList = true;

        /*현재 출퇴근 상태 값*/
        int userCommuteState = UserInfo.getInstance().userCommuteState;

        /*이용자 출석 관리 체크*/
        getChildAttInfo();

        /*사용자 출퇴근 정보 못 가지고 왔을 때*/
        if(userCommuteState == -1){

            showSnackbar(getString(R.string.Common_msg_network_err));

        }
        /*출근하기 누르지 않은 상태*/
        else if(userCommuteState == 0){

            showSnackbar(getString(R.string.MainActivity_msg_setLeave_err));

        }
        else if(!Utils.getPermissionStatus(this)){

            showSnackbar(getString(R.string.Common_msg_permission_err));
        }
        else if(!Utils.getGPSState(this)) {

            showSnackbar(getString(R.string.Common_msg_gps_err));

        } else if(UserInfo.getInstance().selectedSubagency_idx == -1 || UserInfo.getInstance().selectedService_idx == -1){

            showSnackbar(getString(R.string.MainActivity_msg_spinner_err2));

        } else if(!UserInfo.getInstance().isFinRollList){

            showSnackbar(getString(R.string.MainActivity_msg_finishRollList_err));

        } else {

            apiService.getChildAttInfo(att_idx).enqueue(new Callback<List<UserAttendanceListModel>>() {
                @Override
                public void onResponse(Call<List<UserAttendanceListModel>> call, Response<List<UserAttendanceListModel>> response) {
                    if(response.isSuccessful()){
                        List<UserAttendanceListModel> userAttendanceList = new ArrayList<>();

                        userAttendanceList = response.body();
                        Constant.LOG(TAG, "getChildAttInfo : " + userAttendanceList);

                        if(userAttendanceList.size() > 0){
                            for(int i=0; i < userAttendanceList.size(); i++){
                                if(userAttendanceList.get(i).getChildAttendanceIdx() == -1){

                                    UserInfo.getInstance().isFinRollList = false;

                                }else{

                                    UserInfo.getInstance().isFinRollList = true;

                                }

                            }

                            Constant.LOG(TAG, " UserInfo.getInstance().isFinRollList : " +  UserInfo.getInstance().isFinRollList);

                            if(!UserInfo.getInstance().isFinRollList) {

                                showSnackbar(getString(R.string.MainActivity_msg_finishRollList_err));

                            } else {

                                nowLatitude = gpsTracker.getLatitude();
                                nowLongitude = gpsTracker.getLongitude();
                                String agencyName =  UserInfo.getInstance().selectedSubagency_name+"";
                                commuteConfirmDialog = new CommuteConfirmDialog_backup(MainActivity_backup.this, Utils.getNowDateDay(), Utils.getNowDate(),Utils.getNowTime(),
                                        nowLatitude, nowLongitude, commuteCloseButtonListener, 2, agencyName, "");

                                commuteConfirmDialog.show();

                            }


                        } else {

                            UserInfo.getInstance().isFinRollList = true;

                            nowLatitude = gpsTracker.getLatitude();
                            nowLongitude = gpsTracker.getLongitude();


                            commuteConfirmDialog = new CommuteConfirmDialog_backup(MainActivity_backup.this, Utils.getNowDateDay(), Utils.getNowDate(), Utils.getNowTime(),
                                    nowLatitude, nowLongitude, commuteCloseButtonListener, 2, "", "");

                            commuteConfirmDialog.show();



                        }


                    }else{
                        showSnackbar(getString(R.string.getChildAttInfo_msg_err_01));
                        Constant.LOG(TAG, "getChildAttInfo response err");

                    }

                }

                @Override
                public void onFailure(Call<List<UserAttendanceListModel>> call, Throwable t) {
                    showSnackbar(getString(R.string.getChildAttInfo_msg_err_02));
                    Constant.LOG(TAG, "getChildAttInfo err : " + t.toString());

                }

            });

        }





    }

}


