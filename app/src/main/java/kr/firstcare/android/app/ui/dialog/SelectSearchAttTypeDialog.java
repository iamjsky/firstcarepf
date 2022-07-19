package kr.firstcare.android.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.UserAttendanceAdapter;
import kr.firstcare.android.app.data.Constant;


/**
 * ClassName            SelectSearchAttTypeDialog
 * Created by JSky on   2020-06-25
 *
 * Description          이용자 출석관리 출결석 선택 다이얼로그
 */

public class SelectSearchAttTypeDialog extends BaseDialog {

    private View.OnClickListener mCloseButtonListener;

    private String mTitle="";

    @BindView(R.id.iv_close)
    ImageView iv_close;

    @BindView(R.id.iv_checkAttendance)
    ImageView iv_checkAttendance;
    @BindView(R.id.iv_checkAbsence)
    ImageView iv_checkAbsence;


    Context mContext;

    public int searchAttType = -1;
    private SearchUserAttDialog searchUserAttDialog;
    private int parentType = -1;


    public SelectSearchAttTypeDialog(Context context, View.OnClickListener closeButtonListener, int parentType) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mContext = context;
        this.parentType = parentType;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_search_att_type);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;



        iv_close.setOnClickListener(mCloseButtonListener);

        setCheck(0);

    }

    @Override
    public void onBackPressed() {
        UserAttendanceDialog.instance.refresh();
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);



    }



    private void setCheck(int selectType){
       switch (selectType){
           case 0:
               iv_checkAttendance.setImageResource(R.drawable.bg_circle_02);
               iv_checkAbsence.setImageResource(R.drawable.bg_circle_01);
               searchAttType = 0;
               Constant.LOG(TAG,"CLICKED4 : " + searchAttType);

           break;

           case 1:
               iv_checkAttendance.setImageResource(R.drawable.bg_circle_01);
               iv_checkAbsence.setImageResource(R.drawable.bg_circle_02);
               searchAttType = 1;
               Constant.LOG(TAG,"CLICKED5 : " + searchAttType);

               break;
       }

    }

    @OnClick(R.id.layout_checkAttendance)
    public void layout_checkAttendanceClicked(){
        Constant.LOG(TAG,"CLICKED1 : " + searchAttType);
        setCheck(0);

    }

    @OnClick(R.id.layout_checkAbsence)
    public void layout_checkAbsenceClicked(){
        Constant.LOG(TAG,"CLICKED2 : " + searchAttType);
        setCheck(1);
    }

    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked(){
        Constant.LOG(TAG,"CLICKED3 : " + searchAttType);
        if (searchAttType > -1) {
            String title = "";
            switch (searchAttType){

                case 0:
                    title="출석";
                    break;

                case 1:
                    title="결석";
                    break;
            }

            searchUserAttDialog = new SearchUserAttDialog(mContext, title, searchUserAttCloseButtonListener, searchAttType, parentType);
            searchUserAttDialog.show();
            dismiss();

        }else{
            Constant.LOG("dingding", "조회할 종류를 체크 해주세요");
        }




    }

    // 출석 결석 팝업 닫기
    private View.OnClickListener searchUserAttCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            searchUserAttDialog.dismiss();
            UserAttendanceDialog.instance.refresh();


        }

    };

}
