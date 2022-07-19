package kr.firstcare.android.app.ui.dialog;

import android.content.Context;
import android.os.Bundle;

import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.data.Constant;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.UserAttendanceAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.UserAttendanceListModel;
import kr.firstcare.android.app.ui.activity.CommuteInfoActivity;
import kr.firstcare.android.app.ui.activity.MainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * ClassName            SearchUserAttDialog
 * Created by JSky on   2020-06-17
 * <p>
 * Description          이용자 출석 관리 다이얼로그
 */


public class SearchUserAttDialog extends BaseDialog {
    public static final String TAG = "fc_debug";
    private View.OnClickListener mCloseButtonListener;

    private String mTitle = "";

    private Context mContext;


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.rv_userListView)
    RecyclerView rv_userListView;

    @BindView(R.id.layout_progressBar)
    LinearLayout layout_progressBar;

    @BindView(R.id.edtxt_inputUser)
    EditText edtxt_inputUser;

    private UserAttendanceAdapter userAttendanceAdapter;

    /**
     * type
     * 출석       0
     * 결석       1
     */
    private int type = -1;

    private int numberOfColums = 2;
    private int parentType = -1;

    List<UserAttendanceListModel> userAttendanceList = new ArrayList<>();


    public SearchUserAttDialog(Context context, String title, View.OnClickListener closeButtonListener, int type, int parentType) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mTitle = title;
        mContext = context;
        this.type = type;
        this.parentType = parentType;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search_user_att);
        ButterKnife.bind(this);

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        iv_close.setOnClickListener(mCloseButtonListener);
        tv_title.setText(mTitle);

        rv_userListView.setLayoutManager(new GridLayoutManager(mContext, numberOfColums));
        userAttendanceAdapter = new UserAttendanceAdapter(mContext);

        userAttendanceAdapter.setOnCancelAttendanceButtonClickListener(cancelAttendanceButtonClickListener);
        userAttendanceAdapter.setOnDeleteAttendanceButtonClickListener(deleteAttendanceButtonClickListener);
        rv_userListView.setAdapter(userAttendanceAdapter);
        edtxt_inputUser.addTextChangedListener(inputUserNameTextWatcher);


        getChildAttInfo();

    }

    public void refresh() {
        Constant.LOG(TAG, "REFRESH USERATTEDANCEDAILOG");

        getChildAttInfo();


    }

    @Override
    public void onBackPressed() {
        UserAttendanceDialog.instance.refresh();
        super.onBackPressed();
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

                    List<UserAttendanceListModel> searchList = new ArrayList<>();

                    userAttendanceList = response.body();
                    Constant.LOG(TAG, "getChildAttInfo : " + userAttendanceList);

                    if (userAttendanceList.size() > 0) {
                        for (UserAttendanceListModel data : userAttendanceList) {
                            if (type == 0) {
                                Constant.LOG(TAG, "childdata : " + data.getChildAttendanceIdx() + " / " + data.getAttAbsFlag());
                                if (data.getChildAttendanceIdx() != null &&
                                        data.getChildAttendanceIdx() != -1 &&
                                        data.getAttAbsFlag() != null &&
                                        data.getAttAbsFlag().equals("출석")) {

                                    searchList.add(data);
                                }


                            } else if (type == 1) {
                                if (data.getChildAttendanceIdx() != null &&
                                        data.getChildAttendanceIdx() != -1 &&
                                        data.getAttAbsFlag() != null &&
                                        data.getAttAbsFlag().equals("결석")) {

                                    searchList.add(data);
                                }

                            }

                        }


                        userAttendanceAdapter.addItem(searchList);
                        String text = edtxt_inputUser.getText().toString() + "";
                        userAttendanceAdapter.filter(text);
                        userAttendanceAdapter.notifyDataSetChanged();

                    } else {

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






    /*update_child_att - 이용자 출결석 데이터 수정*/
//    public void updateChildAttendance(UserAttendanceListModel selectData, int type, int position){
//
//        String act_date = Utils.getNowDate() + "";
//        String att_time = Utils.getNowTime() + "";
//        String att_abs_flag = "";
//        if(type == 0){
//            att_abs_flag = "att";
//        }else if (type == 1){
//            att_abs_flag = "abs";
//        }
//        int child_attendance_idx = -1;
//        if(selectData.getChildAttendanceIdx() != null && selectData.getChildAttendanceIdx() != -1){
//            child_attendance_idx = selectData.getChildAttendanceIdx();
//        }
//        Constant.LOG(TAG, "updateChildAttendance data : " + act_date + " / " + att_time + " / " + att_abs_flag + " / " + child_attendance_idx);
//        apiService.updateChildAttendance(act_date, att_time, att_abs_flag, child_attendance_idx).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        String result = response.body().string();
//                        Constant.LOG(TAG, "updateChildAttendance result : " + result);
//
//                        if (result.equals("success")) {
//
//                            if (type == 0) {
//
//                                showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_fin_01));
//
//                            } else if (type == 1) {
//
//                                showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_fin_02));
//
//                            }
//
//
//                        } else {
//
//                            showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_err_01));
//
//                        }
//
//                    } catch (IOException e) {
//                        showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_err_02));
//                        e.printStackTrace();
//
//                    }
//
//
//                    refresh();
//
//                } else {
//
//                    showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_err_02));
//                    Constant.LOG(TAG, "updateChildAttendance response err");
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                showParentTypeSnackbar(mContext.getString(R.string.updateChildAttendance_msg_err_03));
//                Constant.LOG(TAG, "updateChildAttendance err : " + t.toString());
//
//            }
//        });
//
//
//
//    }

//    private void setDummy(boolean stat){
//        List<UserAttendanceListModel> dataList = new ArrayList<>();
//
//        for(int i=0; i < 30; i++){
//            UserAttendanceListModel userAttendanceListModel = new UserAttendanceListModel();
//            userAttendanceListModel.setIdx(i+1);
//            userAttendanceListModel.setProfile_img("https://firstcare.kr/_public/images/sub/ic_q4.png");
//            userAttendanceListModel.setUser_name("출석결석홍길동"+(i+1)+"");
//            userAttendanceListModel.setUser_age((i+3)+"");
//            if(i%2==0){
//                userAttendanceListModel.setUser_gender("남");
//            }else{
//                userAttendanceListModel.setUser_gender("여");
//            }
//            userAttendanceListModel.setUser_phoneNum("010-1234-567"+(i+1));
//            if(stat){
//                userAttendanceListModel.setAtt_stat("Y");
//                userAttendanceListModel.setAb_stat("N");
//            }else{
//                userAttendanceListModel.setAtt_stat("N");
//                userAttendanceListModel.setAb_stat("Y");
//
//            }
//            userAttendanceListModel.setAtt_changedTime("15:52:59");
//            userAttendanceListModel.setAb_changedTime("10:00:00");
//            dataList.add(userAttendanceListModel);
//        }
//
//        userAttendanceAdapter.addItem(dataList);
//        userAttendanceAdapter.notifyDataSetChanged();
//    }


}
