package kr.firstcare.android.app.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.ui.activity.CommuteInfoActivity;


/**
 * ClassName            DatePickerDialog
 * Created by JSky on   2020-06-24
 * 
 * Description          이전기록조회 다이얼로그
 */

public class DatePickerDialog extends BaseDialog{
    public static final String TAG = "fc_debug";
    private View.OnClickListener mCloseButtonListener;

    private String mTitle = "";

    private Context mContext;


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_close)
    ImageView iv_close;

    @BindView(R.id.dp_datePicker)
    DatePicker dp_datePicker;
    @BindView(R.id.tv_date)
    TextView tv_date;

    private int year,month,day;
    private String strDate="";

    public DatePickerDialog(Context context, String title, View.OnClickListener closeButtonListener) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mTitle = title;
        mContext = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_picker);
        ButterKnife.bind(this);

        iv_close.setOnClickListener(mCloseButtonListener);
        tv_title.setText(mTitle);

        year = Utils.getNowDatePick(1);
        month = Utils.getNowDatePick(2);
        day = Utils.getNowDatePick(3);

        strDate = year + "-" + month + "-" + day;

        try {

            tv_date.setText(year + "년 " + month + "월 " + day + "일 " + Utils.getDateToDay(strDate,"yyyy-M-d")+"요일");

        } catch (Exception e) {
            e.printStackTrace();

        }

        Constant.LOG("dingding", "date : " + strDate);

        dp_datePicker.init(year, month-1, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                strDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                try {

                    tv_date.setText(year + "년 " + (monthOfYear+1) + "월 " + dayOfMonth + "일 " + Utils.getDateToDay(strDate,"yyyy-M-d")+"요일");

                } catch (Exception e) {
                    e.printStackTrace();

                }

                Constant.LOG("dingding", "date : " + strDate);

            }

        });

    }

    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked() {

        UserInfo.getInstance().selectCommuteInfoDate = strDate;
        dismiss();

        Intent intent = new Intent(mContext, CommuteInfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Constant.LOG(TAG, "UserInfo.getInstance().selectCommuteInfoDate : " + UserInfo.getInstance().selectCommuteInfoDate + " /Utils.getChangeDateFormat(UserInfo.getInstance().selectCommuteInfoDate) : " + Utils.getChangeDateFormat(UserInfo.getInstance().selectCommuteInfoDate));
        mContext.startActivity(intent);

        ((Activity)mContext).overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }

}
