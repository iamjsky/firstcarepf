package kr.firstcare.android.app.ui.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


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
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.CommuteInfoListModel;
import kr.firstcare.android.app.model.UserAgencyListModel;
import kr.firstcare.android.app.model.UserModel;
import kr.firstcare.android.app.ui.dialog.CommuteConfirmDialog;
import kr.firstcare.android.app.ui.dialog.DatePickerDialog;
import kr.firstcare.android.app.ui.dialog.SelectProfileImageDialog;
import kr.firstcare.android.app.ui.dialog.UserAttendanceDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ClassName            CommuteInfoActivity
 * Created by JSky on   2020-06-22
 * 
 * Description          출퇴근 상세기록
 */

public class CommuteInfoActivity extends BaseActivity {

    /*상단 메뉴*/
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

    @BindView(R.id.rv_commuteListView)
    RecyclerView rv_commuteListView;
    @BindView(R.id.layout_resultEmpty)
    LinearLayout layout_resultEmpty;
    @BindView(R.id.rv_refreshLayout)
    TwinklingRefreshLayout rv_refreshLayout;


    private CommuteInfoAdapter commuteInfoAdapter;
    UserAttendanceDialog userAttendanceDialog;
    CommuteConfirmDialog commuteConfirmDialog;
    DatePickerDialog datePickerDialog;
    SelectProfileImageDialog selectProfileImageDialog;

    /*GPS*/
    private GpsTracker gpsTracker;
    private double nowLatitude = 0;
    private double nowLongitude = 0;

    private String selectCommuteInfoDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commute_info);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.drawer_layout);

        gpsTracker = new GpsTracker(mContext);

        rv_commuteListView.setLayoutManager(new LinearLayoutManager(this));
        commuteInfoAdapter = new CommuteInfoAdapter(this);

        /*이전 기록 조회*/
        commuteInfoAdapter.setOnDatePickerButtonClickListener(datePickerButtonClickListener);
        /*출근 위치 확인*/
        commuteInfoAdapter.setOnGetToWorkButtonClickListener(getToWorkInfoButtonClickListener);
        /*퇴근 위치 확인*/
        commuteInfoAdapter.setOnFinishWorkButtonClickListener(finishWorkInfoButtonClickListener);
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

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

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
        selectCommuteInfoDate = UserInfo.getInstance().selectCommuteInfoDate;
        getSelectCommuteInfo(UserInfo.getInstance().idx);
        getUserAgencyList(UserInfo.getInstance().idx);
        getUserInfo(UserInfo.getInstance().vteacherId);

    }

    /*네비 메뉴 세팅*/
    private void setNavMenu(){

        String userName = UserInfo.getInstance().vteacherName+"";
        tv_userName.setText(userName);

        switch (UserInfo.getInstance().selectLoginType)
        {
            case 0:

                break;

            case 1:

                tv_userType.setText(getString(R.string.CommuteInfoActivity_text_typeG));
                layout_navHeader_typeV.setVisibility(View.GONE);
                layout_navMenu_typeV.setVisibility(View.GONE);

                break;

            case 2:

                tv_userType.setText(getString(R.string.CommuteInfoActivity_text_typeN));
                layout_navHeader_typeV.setVisibility(View.GONE);
                layout_navMenu_typeV.setVisibility(View.GONE);

                break;

            case 3:

                tv_userType.setText(getString(R.string.CommuteInfoActivity_text_typeV));
                layout_navHeader_typeV.setVisibility(View.VISIBLE);
                layout_navMenu_typeV.setVisibility(View.VISIBLE);

                break;

        }

        /*네비메뉴의 프로필 사진*/
        String profileImgUrl = Constant.TEACHER_PROFILE_IMG_URL + UserInfo.getInstance().photo+"";
        if(!profileImgUrl.equals("") && profileImgUrl.contains("http")) {

            Glide.with(mContext).load(profileImgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_profile);

        }else{

            iv_profile.setImageResource(R.color.fc_mainColor);

        }

    }

    /*네비메뉴 햄버거 버튼*/
    @OnClick(R.id.btn_nav)
    public void btn_navClicked() {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    /*프로필사진 업로드*/
    @OnClick(R.id.btn_uploadProfileImg)
    public void btn_uploadProfileImgClicked() {
        selectProfileImageDialog = new SelectProfileImageDialog(this, selectProfileImageDialogCloseButtonListener, 0);
        selectProfileImageDialog.show();
    }
    /*기본 정보 클릭*/
    @OnClick(R.id.layout_userInfo_typeV)
    public void layout_userInfo_typeVClicked() {

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

    }


    // 프로필 사진 업로드 선택 팝업 닫기
    private View.OnClickListener selectProfileImageDialogCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectProfileImageDialog.dismiss();
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
    public CommuteInfoAdapter.OnGetToWorkButtonClickListener getToWorkInfoButtonClickListener = new CommuteInfoAdapter.OnGetToWorkButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            CommuteInfoListModel data = commuteInfoAdapter.getItem(position);

            String actDate = data.getActDate();
            String actTime = data.getAttTime();
            String attAddr = data.getAttAddr();
            double attLatitude = Utils.getCurrentMapPoint(mContext, attAddr, 0);
            double attLongitude = Utils.getCurrentMapPoint(mContext, attAddr, 1);
            String locationName = data.getSub_addr() + data.getSub_addr_detail() + "";
            String agencyName = data.getSubAgencyName();

            Constant.LOG(TAG, commuteInfoAdapter.getItem(position).toString());
            iv_getToWorkInfoClicked(actDate, actTime, attLatitude, attLongitude, locationName, agencyName);

        }

    };

    /*퇴근 위치 확인 리스너*/
    public CommuteInfoAdapter.OnFinishWorkButtonClickListener finishWorkInfoButtonClickListener = new CommuteInfoAdapter.OnFinishWorkButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            CommuteInfoListModel data = commuteInfoAdapter.getItem(position);

            String actDate = data.getActDate();
            String leaveTime = data.getLeaveTime();
            String leaveAddr = data.getLeaveAddr();
            double leaveLatitude = Utils.getCurrentMapPoint(mContext, leaveAddr, 0);
            double leaveLongitude = Utils.getCurrentMapPoint(mContext, leaveAddr, 1);
            String locationName = data.getSub_addr() + data.getSub_addr_detail() + "";
            String agencyName = data.getSubAgencyName();

            Constant.LOG(TAG, "leaveLatitude : " + leaveLatitude + " / leaveLongitude : " + leaveLongitude + " / locationName : " + locationName);

            Constant.LOG(TAG, commuteInfoAdapter.getItem(position).toString());
            iv_finishWorkInfoClicked(actDate, leaveTime, leaveLatitude, leaveLongitude, locationName, agencyName);

        }

    };

    /*이용자 출석 관리 리스너*/
    public CommuteInfoAdapter.OnUserRollListButtonClickListener userRollListButtonClickListener = new CommuteInfoAdapter.OnUserRollListButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Constant.LOG(TAG, commuteInfoAdapter.getItem(position).toString());
            CommuteInfoListModel selectData = new CommuteInfoListModel();
            selectData = commuteInfoAdapter.getItem(position);
            UserInfo.getInstance().att_idx = selectData.getAttIdx();
            btn_userRollListClicked();

        }

    };


    /*출근 확인 팝업*/
    public void iv_getToWorkInfoClicked(String actDate, String actTime, double attLatitude, double attLongitude, String locationName, String agencyName) {

        if(!Utils.getPermissionStatus(this)){

            showSnackbar(getString(R.string.Common_msg_permission_err));
        }
        else if(!Utils.getGPSState(this)) {

            showSnackbar(getString(R.string.Common_msg_gps_err));

        } else{

            commuteConfirmDialog = new CommuteConfirmDialog(this, getString(R.string.Dialog_title_getToWorkInfo), actDate, actTime,
                    attLatitude,attLongitude, commuteCloseButtonListener, 3, locationName, agencyName);

            commuteConfirmDialog.show();

        }

    }

    /*퇴근 확인 팝업*/
    public void iv_finishWorkInfoClicked(String actDate, String leaveTime, double leaveLatitude, double leaveLongitude, String locationName, String agencyName) {

        if(!Utils.getPermissionStatus(this)){

            showSnackbar(getString(R.string.Common_msg_permission_err));

        }
        else if(!Utils.getGPSState(this)) {

            showSnackbar(getString(R.string.Common_msg_gps_err));

        } else{

            commuteConfirmDialog = new CommuteConfirmDialog(this, getString(R.string.Dialog_title_finishWorkInfo), actDate, leaveTime,
                    leaveLatitude,leaveLongitude, commuteCloseButtonListener, 4, locationName, agencyName);

            commuteConfirmDialog.show();

        }

    }

    // 출퇴근 위치 확인 팝업 닫기
    private View.OnClickListener commuteCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            commuteConfirmDialog.dismiss();

        }

    };

    /*이전기록조회(날짜선택) 팝업*/
    @OnClick(R.id.tv_commuteList)
    public void tv_commuteListClicked(){
        datePickerDialog = new DatePickerDialog(this, getString(R.string.Dialog_title_searchCommuteList), datePickerCloseButtonListener);
        datePickerDialog.show();

    }

    // 이전기록조회 팝업 닫기
    private View.OnClickListener datePickerCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerDialog.dismiss();

        }

    };

    /*이용자 출석 관리 팝업*/
    public void btn_userRollListClicked(){
        userAttendanceDialog = new UserAttendanceDialog(this, getString(R.string.Dialog_title_userAttendance), userAttCloseButtonListener, 1);
        userAttendanceDialog.show();

    }

    // 이용자출석관리 팝업 닫기
    private View.OnClickListener userAttCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userAttendanceDialog.dismiss();

        }

    };

    /*뒤로가기 클릭*/
    @OnClick(R.id.iv_back)
    public void iv_backClicked(){
        onBackPressed();
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
















    // 매칭된 제공기관 리스트
    public void getUserAgencyList(int idx) {

        apiService.getUserAgencyList(idx).enqueue(new Callback<List<UserAgencyListModel>>() {
            @Override
            public void onResponse(Call<List<UserAgencyListModel>> call, Response<List<UserAgencyListModel>> response) {
                if (response.isSuccessful()) {
                    List<UserAgencyListModel> dataList = new ArrayList<>();
                    dataList = response.body();

                    Constant.LOG(TAG, "getUserAgencyList result : " + dataList);

                    if (dataList.size() > 0) {
                        for (UserAgencyListModel data : dataList) {

                            Constant.LOG(TAG, " data.getVteacherName() : " + data.getSubAgencyName());
                        }
//                        searchUserAgencyAdapter.addItem(dataList);
//                        searchUserAgencyAdapter.notifyDataSetChanged();
                    }


                } else {
                    Constant.LOG(TAG, "getUserAgencyList response err");
                }

            }

            @Override
            public void onFailure(Call<List<UserAgencyListModel>> call, Throwable t) {

                Constant.LOG(TAG, "getUserAgencyList err : " + t.toString());
            }
        });
    }
    // 출퇴근정보
    public void getSelectCommuteInfo(int idx) {
        String actDate = selectCommuteInfoDate;
        apiService.getSelectCommuteInfo(idx, actDate).enqueue(new Callback<List<CommuteInfoListModel>>() {
            @Override
            public void onResponse(Call<List<CommuteInfoListModel>> call, Response<List<CommuteInfoListModel>> response) {
                if (response.isSuccessful()) {
                    List<CommuteInfoListModel> dataList = new ArrayList<>();
                    dataList = response.body();




                    Constant.LOG(TAG, "getCommuteInfo result : " + dataList);

                    commuteInfoAdapter.clearItem();
                    if(dataList.size() > 0){

                        Constant.LOG(TAG,"!!!!!!!!!!!!att_idx : " + dataList.get(0).getAttIdx());

                        commuteInfoAdapter.addItem(dataList);
                        commuteInfoAdapter.notifyDataSetChanged();



                    }else{

                            layout_resultEmpty.setVisibility(View.VISIBLE);


                    }

                    rv_refreshLayout.finishRefreshing();

                } else {
                    Constant.LOG(TAG, "getCommuteInfo response err");
                    layout_resultEmpty.setVisibility(View.VISIBLE);
                    rv_refreshLayout.finishRefreshing();
                }

            }

            @Override
            public void onFailure(Call<List<CommuteInfoListModel>> call, Throwable t) {
                layout_resultEmpty.setVisibility(View.VISIBLE);
                Constant.LOG(TAG, "getCommuteInfo err : " + t.toString());
                rv_refreshLayout.finishRefreshing();
            }
        });
    }
}