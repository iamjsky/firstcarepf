package kr.firstcare.android.app.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import kr.firstcare.android.app.data.Constant;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.adapter.UserAttendanceAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.UserAttendanceListModel;
import kr.firstcare.android.app.model.UserModel;
import kr.firstcare.android.app.ui.activity.CommuteInfoActivity;
import kr.firstcare.android.app.ui.activity.LoginActivity;
import kr.firstcare.android.app.ui.activity.MainActivity;
import kr.firstcare.android.app.ui.activity.SignUpActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;


/**
 * ClassName            UserAttendanceDialog
 * Created by JSky on   2020-06-26
 * <p>
 * Description          이용자 출석 관리 출결석 다이얼로그
 */

public class UserAttendanceDialog extends BaseDialog {

    public static UserAttendanceDialog instance = null;
    private View.OnClickListener mCloseButtonListener;

    private String mTitle = "";

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.layout_progressBar)
    LinearLayout layout_progressBar;

    @BindView(R.id.rv_userListView)
    RecyclerView rv_userListView;

    @BindView(R.id.edtxt_inputUser)
    EditText edtxt_inputUser;


    private int numberOfColums = 2;

    Context mContext;


    private UserAttendanceAdapter userAttendanceAdapter;

    private SelectSearchAttTypeDialog selectSearchAttTypeDialog;

    private int parentType;
    /**
     * parentType
     * 0   :   비밀번호 찾기
     * 1   :   회원가입
     */

    List<UserAttendanceListModel> userAttendanceList = new ArrayList<>();

    public UserAttendanceDialog(Context context, String title, View.OnClickListener closeButtonListener, int parentType) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mTitle = title;
        mContext = context;
        this.parentType = parentType;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_attendance);
        ButterKnife.bind(this);
        instance = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        iv_close.setOnClickListener(mCloseButtonListener);
        tv_title.setText(mTitle);

        rv_userListView.setLayoutManager(new GridLayoutManager(mContext, numberOfColums));
        userAttendanceAdapter = new UserAttendanceAdapter(mContext);
        userAttendanceAdapter.setOnSetAttendanceButtonClickListener(setAttendanceButtonClickListener);
        userAttendanceAdapter.setOnSetAbsenceButtonClickListener(setAbsenceButtonClickListener);
        userAttendanceAdapter.setOnCancelAttendanceButtonClickListener(cancelAttendanceButtonClickListener);
        userAttendanceAdapter.setOnDeleteAttendanceButtonClickListener(deleteAttendanceButtonClickListener);

        rv_userListView.setAdapter(userAttendanceAdapter);
        edtxt_inputUser.addTextChangedListener(inputUserNameTextWatcher);

        refresh();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);

    }

    public void refresh() {
        Constant.LOG(TAG, "REFRESH USERATTEDANCEDAILOG");

        getChildAttInfo();


    }

    /*로딩 프로그래스바 처리*/
    public void showProgress(boolean value) {
        if (value) {

            layout_progressBar.setVisibility(View.VISIBLE);

        } else {

            layout_progressBar.setVisibility(View.GONE);

        }

    }

    /*부모뷰 메시지 스낵바*/
    private void showParentTypeSnackbar(String msg) {
        switch (parentType) {
            case 0:

                ((MainActivity) mContext).showSnackbar(mainLayout, msg);
                break;

            case 1:
                ((CommuteInfoActivity) mContext).showSnackbar(mainLayout, msg);
                break;
        }

    }

    @OnClick(R.id.tv_viewAttPopUp)
    public void tv_viewAttPopUpClicked() {

        selectSearchAttTypeDialog = new SelectSearchAttTypeDialog(mContext, selectSearchAttCloseButtonListener, parentType);
        selectSearchAttTypeDialog.show();

    }

    // 출석 결석 선택 팝업 닫기
    private View.OnClickListener selectSearchAttCloseButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {

            selectSearchAttTypeDialog.dismiss();
            refresh();

        }
    };

    /*출석 버튼*/
    public UserAttendanceAdapter.OnSetAttendanceButtonClickListener setAttendanceButtonClickListener = new UserAttendanceAdapter.OnSetAttendanceButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            Constant.LOG(TAG, userAttendanceAdapter.getItem(position).toString());
            UserAttendanceListModel selectData = new UserAttendanceListModel();
            selectData = userAttendanceAdapter.getItem(position);
            insertChildAttendance(selectData, 0, position);

        }
    };

    /*결석 버튼*/
    public UserAttendanceAdapter.OnSetAbsenceButtonClickListener setAbsenceButtonClickListener = new UserAttendanceAdapter.OnSetAbsenceButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Constant.LOG(TAG, userAttendanceAdapter.getItem(position).toString());
            UserAttendanceListModel selectData = new UserAttendanceListModel();
            selectData = userAttendanceAdapter.getItem(position);
            insertChildAttendance(selectData, 1, position);

        }
    };

    /*출결석취소 버튼*/
    public UserAttendanceAdapter.OnCancelAttendanceButtonClickListener cancelAttendanceButtonClickListener = new UserAttendanceAdapter.OnCancelAttendanceButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Constant.LOG(TAG, userAttendanceAdapter.getItem(position).toString());
            UserAttendanceListModel selectData = new UserAttendanceListModel();
            selectData = userAttendanceAdapter.getItem(position);
            Constant.LOG(TAG, "selectData (cancel) : " + selectData);

            if (selectData.getChildAttendanceIdx() != -1) {

                updateChildAttendance(selectData);

            }

        }

    };

    /*미등록 버튼*/
    public UserAttendanceAdapter.OnDeleteAttendanceButtonClickListener deleteAttendanceButtonClickListener = new UserAttendanceAdapter.OnDeleteAttendanceButtonClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Constant.LOG(TAG, userAttendanceAdapter.getItem(position).toString());
            UserAttendanceListModel selectData = new UserAttendanceListModel();
            selectData = userAttendanceAdapter.getItem(position);
            Constant.LOG(TAG, "selectData (cancel) : " + selectData);

            if (selectData.getChildAttendanceIdx() != -1) {

                deleteChildAttendance(selectData.getChildAttendanceIdx());

            }

        }

    };

    public TextWatcher inputUserNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (userAttendanceList != null && userAttendanceList.size() > 0) {
                String text = edtxt_inputUser.getText().toString() + "";
                userAttendanceAdapter.filter(text);
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /*child_att_info - 이용자 출결석 조회*/
    public void getChildAttInfo() {

        showProgress(true);
        int att_idx = UserInfo.getInstance().att_idx;

        apiService.getChildAttInfo(att_idx).enqueue(new Callback<List<UserAttendanceListModel>>() {
            @Override
            public void onResponse(Call<List<UserAttendanceListModel>> call, Response<List<UserAttendanceListModel>> response) {
                if (response.isSuccessful()) {

                    userAttendanceList = response.body();
                    Constant.LOG(TAG, "getChildAttInfo : " + userAttendanceList);

                    if (userAttendanceList.size() > 0) {

                        userAttendanceAdapter.addItem(userAttendanceList);
                        String text = edtxt_inputUser.getText().toString() + "";
                        userAttendanceAdapter.filter(text);
                        userAttendanceAdapter.notifyDataSetChanged();
                    }

                    showProgress(false);

                } else {
                    showParentTypeSnackbar(mContext.getString(R.string.getChildAttInfo_msg_err_01));
                    Constant.LOG(TAG, "getChildAttInfo response err");
                    showProgress(false);


                }

            }

            @Override
            public void onFailure(Call<List<UserAttendanceListModel>> call, Throwable t) {
                showParentTypeSnackbar(mContext.getString(R.string.getChildAttInfo_msg_err_02));
                Constant.LOG(TAG, "getChildAttInfo err : " + t.toString());
                showProgress(false);


            }

        });


    }

    /*insert_child_att1 - 이용자 출결석 데이터 입력*/
    public void insertChildAttendance(UserAttendanceListModel selectData, int type, int position) {
        int program_idx = selectData.getProgramIdx();
        int vteacher_idx = selectData.getVteacherIdx();
        int subagency_idx = selectData.getSubagencyIdx();
        int vteacher_attendance_idx = selectData.getVteacherAttendanceIdx();
        int child_idx = selectData.getChildIdx();
        String act_date = Utils.getNowDate() + "";
        String att_time = Utils.getNowTime() + "";
        String att_abs_flag = "";
        if (type == 0) {
            att_abs_flag = "att";
        } else if (type == 1) {
            att_abs_flag = "abs";
        }
        String zipcode = "";
        if (selectData.getZipcode() != null && !selectData.getZipcode().equals("")) {
            zipcode = selectData.getZipcode();
        } else {
            zipcode = "-";
        }
        String addr = "";
        if (selectData.getAddr() != null && !selectData.getAddr().equals("")) {
            addr = selectData.getAddr();
        } else {
            addr = "-";
        }
        String addr_detail = "";
        if (selectData.getAddrDetail() != null && !selectData.getAddrDetail().equals("")) {
            addr_detail = selectData.getAddrDetail();
        } else {
            addr_detail = "-";
        }
        String add_date = Utils.getNowDateTime() + "";
        String add_id = UserInfo.getInstance().vteacherId;
        String add_ip = UserInfo.getInstance().userIp;

        apiService.insertChildAttendance(program_idx, vteacher_idx, subagency_idx, vteacher_attendance_idx,
                child_idx, act_date, att_time, att_abs_flag, zipcode, addr, addr_detail, add_date, add_id, add_ip)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            try {

                                String result = response.body().string();
                                Constant.LOG(TAG, "insertChildAttendance result : " + result);

                                if (result.equals("success")) {

                                    if (type == 0) {

                                        showParentTypeSnackbar(mContext.getString(R.string.insertChildAttendace_msg_fin_01));

                                    } else if (type == 1) {

                                        showParentTypeSnackbar(mContext.getString(R.string.insertChildAttendace_msg_fin_02));

                                    }


                                } else {

                                    showParentTypeSnackbar(mContext.getString(R.string.insertChildAttendace_msg_err_01));

                                }

                            } catch (IOException e) {
                                showParentTypeSnackbar(mContext.getString(R.string.insertChildAttendace_msg_err_02));
                                e.printStackTrace();

                            }

                            refresh();

                        } else {

                            showParentTypeSnackbar(mContext.getString(R.string.insertChildAttendace_msg_err_02));
                            Constant.LOG(TAG, "insertChildAttendance response err");

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showParentTypeSnackbar(mContext.getString(R.string.insertChildAttendace_msg_err_03));
                        Constant.LOG(TAG, "insertChildAttendance err : " + t.toString());


                    }

                });


    }

    /*deleteChildAttendance - 이용자 출결석 미등록 상태 변경*/
    public void deleteChildAttendance(int child_attendance_idx) {

        apiService.deleteChildAttendance(child_attendance_idx)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            try {

                                String result = response.body().string();
                                Constant.LOG(TAG, "deleteChildAttendance result : " + result);

                                if (result.equals("success")) {

                                    showParentTypeSnackbar(mContext.getString(R.string.deleteChildAttendance_msg_fin_01));

                                } else {

                                    showParentTypeSnackbar(mContext.getString(R.string.deleteChildAttendance_msg_err_01));

                                }

                            } catch (IOException e) {
                                showParentTypeSnackbar(mContext.getString(R.string.deleteChildAttendance_msg_err_02));
                                e.printStackTrace();

                            }

                            refresh();

                        } else {

                            showParentTypeSnackbar(mContext.getString(R.string.deleteChildAttendance_msg_err_02));
                            Constant.LOG(TAG, "deleteChildAttendance response err");

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showParentTypeSnackbar(mContext.getString(R.string.deleteChildAttendance_msg_err_03));
                        Constant.LOG(TAG, "deleteChildAttendance err : " + t.toString());


                    }

                });
    }


    /*update_child_att - 이용자 출결석 데이터 수정*/
    public void updateChildAttendance(UserAttendanceListModel selectData) {

        String act_date = Utils.getNowDate() + "";
        String att_time = Utils.getNowTime() + "";
        String att_abs_flag = "";
        int type = -1;

        if (selectData.getAttAbsFlag().equals("출석")) {

            att_abs_flag = "abs";
            type = 0;

        } else if (selectData.getAttAbsFlag().equals("결석")) {

            att_abs_flag = "att";
            type = 1;

        }

        int child_attendance_idx = -1;
        if (selectData.getChildAttendanceIdx() != null && selectData.getChildAttendanceIdx() != -1) {
            child_attendance_idx = selectData.getChildAttendanceIdx();

        }

        Constant.LOG(TAG, "updateChildAttendance data : " + act_date + " / " + att_time + " / " + att_abs_flag + " / " + child_attendance_idx);
        int finalType = type;
        apiService.updateChildAttendance(act_date, att_time, att_abs_flag, child_attendance_idx).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {

                        String result = response.body().string();
                        Constant.LOG(TAG, "updateChildAttendance result : " + result);

                        if (result.equals("success")) {

                            if (finalType == 0) {

                                showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_fin_01));

                            } else if (finalType == 1) {

                                showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_fin_02));

                            }


                        } else {

                            showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_err_01));

                        }

                    } catch (IOException e) {
                        showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_err_02));
                        e.printStackTrace();

                    }
                    refresh();
                } else {

                    showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_err_02));
                    Constant.LOG(TAG, "updateChildAttendance response err");
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_err_03));
                Constant.LOG(TAG, "updateChildAttendance err : " + t.toString());

            }
        });


    }


//    private void setDummy(){
//        List<UserAttendanceListModel> dataList = new ArrayList<>();
//
//        for(int i=0; i < 30; i++){
//            UserAttendanceListModel userAttendanceListModel = new UserAttendanceListModel();
//            userAttendanceListModel.setIdx(i+1);
//            userAttendanceListModel.setProfile_img("https://firstcare.kr/_public/images/sub/subtitle_icon.png");
//            userAttendanceListModel.setUser_name("홍길동"+(i+1)+"");
//            userAttendanceListModel.setUser_age((i+3)+"");
//            if(i%2==0){
//                userAttendanceListModel.setUser_gender("남");
//            }else{
//                userAttendanceListModel.setUser_gender("여");
//            }
//
//            userAttendanceListModel.setUser_phoneNum("010-1234-567"+(i+1));
//            if(i%2==0){
//                userAttendanceListModel.setAtt_stat("N");
//                userAttendanceListModel.setAb_stat("N");
//            }else{
//                userAttendanceListModel.setAtt_stat("N");
//                userAttendanceListModel.setAb_stat("Y");
//            }
//
//            userAttendanceListModel.setAtt_changedTime("15:52:59");
//            userAttendanceListModel.setAb_changedTime("10:00:00");
//            dataList.add(userAttendanceListModel);
//        }
//
//        userAttendanceAdapter.addItem(dataList);
//        userAttendanceAdapter.notifyDataSetChanged();
//    }


}
