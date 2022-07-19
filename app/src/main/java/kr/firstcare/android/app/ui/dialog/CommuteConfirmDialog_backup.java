package kr.firstcare.android.app.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.adapter.CustomCalloutBalloonAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.CommuteInfoListModel;
import kr.firstcare.android.app.ui.activity.MainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * ClassName            CommuteConfirmDialog
 * Created by JSky on   2020-06-22
 *
 * Description          출퇴근 다이얼로그 (백업)
 */

public class CommuteConfirmDialog_backup extends BaseDialog {
    public static final String TAG = "fc_debug";
    private static String BASEURL = "";

    private View.OnClickListener mCloseButtonListener;

    private String mTitle = "";
    private Context mContext;



    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_close)
    ImageView iv_close;

    @BindView(R.id.tv_nowLocation)
    TextView tv_nowLocation;
    @BindView(R.id.layout_nowLocation)
    LinearLayout layout_nowLocation;

    @BindView(R.id.layout_commuteDate)
    LinearLayout layout_commuteDate;

    @BindView(R.id.layout_mvContainer)
    RelativeLayout layout_mvContainer;

    @BindView(R.id.btn_commuteConfirm)
    Button btn_commuteConfirm;

    @BindView(R.id.tv_meter)
    TextView tv_meter;

    private MapView mapView;
    /**
     * commuteType
     * 1    :   출근
     * 2    :   퇴근
     * 3    :   출근확인
     * 4    :   퇴근확인
     * */
    private int commuteType=0;

    private String commuteText="";
    private String mDate = "";
    private String mTime = "";
    private double mLatitude = 0;
    private double mLongitude = 0;
    private double attLatitude = 0;
    private double attLongitude = 0;
    private double leaveLatitude = 0;
    private double leaveLongitude = 0;
    private double agencyLatitude = 0;
    private double agencyLongitude = 0;
    private String selectedName = "";
    private String agencyName = "";
    private String nowAddress = "";



    /*약속장소 선택하는 출퇴근하기, 출퇴근확인 (**new**)*/
    public CommuteConfirmDialog_backup(Context context, String title, String date, String time,
                                double latitude, double longitute, View.OnClickListener closeButtonListener, int type, String agencyName, String locationName) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mTitle = title;
        mContext = context;
        commuteType = type;
        mDate = date;
        mTime = time;
        mLatitude = latitude;
        mLongitude = longitute;
        if (type == 1) {

            attLatitude = Utils.getCurrentMapPoint(context, locationName, 0);
            attLongitude = Utils.getCurrentMapPoint(context, locationName, 1);

        }
        this.selectedName = agencyName;
        nowAddress = locationName;
        agencyLatitude = Utils.getCurrentMapPoint(context, locationName, 0);
        agencyLongitude = Utils.getCurrentMapPoint(context, locationName, 1);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commute_confirm);
        ButterKnife.bind(this);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        mainLayout = findViewById(R.id.main_layout);

        iv_close.setOnClickListener(mCloseButtonListener);
        tv_title.setText(mTitle);

        Constant.LOG(TAG, "Dialog userCommuteState : " + UserInfo.getInstance().userCommuteState);

        setMapLayout(commuteType);

    }   //  onCreate

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);

    }

    private void setMapLayout(int commuteType){

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) layout_mvContainer.getLayoutParams();

        switch (commuteType){

            case 1:

                layout_nowLocation.setVisibility(View.VISIBLE);
                layout_commuteDate.setVisibility(View.GONE);
                btn_commuteConfirm.setVisibility(View.VISIBLE);
                params.width = (int) mContext.getResources().getDimension(R.dimen._210sdp);
                params.height = (int) mContext.getResources().getDimension(R.dimen._140sdp);
                layout_mvContainer.setLayoutParams(params);
                commuteText = mContext.getString(R.string.Common_text_getToWork);

                break;

            case 2:

                layout_nowLocation.setVisibility(View.VISIBLE);
                layout_commuteDate.setVisibility(View.GONE);
                btn_commuteConfirm.setVisibility(View.VISIBLE);

                params.width = (int) mContext.getResources().getDimension(R.dimen._210sdp);
                params.height = (int) mContext.getResources().getDimension(R.dimen._140sdp);
                layout_mvContainer.setLayoutParams(params);

                commuteText = mContext.getString(R.string.Common_text_finishWork);

                break;

            case 3:

                layout_nowLocation.setVisibility(View.GONE);
                layout_commuteDate.setVisibility(View.VISIBLE);
                btn_commuteConfirm.setVisibility(View.GONE);

                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = (int) mContext.getResources().getDimension(R.dimen._160sdp);
                layout_mvContainer.setLayoutParams(params);

                commuteText = mContext.getString(R.string.Common_text_getToWork);

                break;

            case 4:

                layout_nowLocation.setVisibility(View.GONE);
                layout_commuteDate.setVisibility(View.VISIBLE);
                btn_commuteConfirm.setVisibility(View.GONE);

                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = (int) mContext.getResources().getDimension(R.dimen._160sdp);
                layout_mvContainer.setLayoutParams(params);

                commuteText = mContext.getString(R.string.Common_text_finishWork);

                break;
        }

        setMapView();

    }


    /*카카오 지도 설정 (**new**)*/
    private void setMapView() {

        mapView = new MapView(mContext);
        layout_mvContainer.addView(mapView);

        mapView.setZoomLevelFloat(1.35555f, true);

// 줌 인
        mapView.zoomIn(true);
// 줌 아웃
        mapView.zoomOut(true);

        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter(mContext));

        String balloonDateText = mDate;
        balloonDateText = balloonDateText.replace("년", "-");
        balloonDateText = balloonDateText.replace("월", "-");
        balloonDateText = balloonDateText.replace("일", "");
        String content = balloonDateText + "\n" + mTime + " " + commuteText + " 위치";

        btn_commuteConfirm.setText(commuteText + "확인");

        if (commuteType == 2) {

            // 중심점 변경 + 줌 레벨 변경
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mLatitude, mLongitude), true);
            nowAddress = Utils.getCurrentAddress(mContext, mLatitude, mLongitude);
            nowAddress = nowAddress.substring(5);
            tv_nowLocation.setText(nowAddress);
            MapPOIItem nowMarker = new MapPOIItem();

            nowMarker.setItemName(content);
            nowMarker.setTag(1);

            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mLatitude, mLongitude);
            nowMarker.setMapPoint(mapPoint);
            nowMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            nowMarker.setCustomImageResourceId(R.drawable.icn_marker_02); // 마커 이미지.
            nowMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            nowMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            mapView.addPOIItem(nowMarker);
            mapView.selectPOIItem(nowMarker, true);

        } else {


            // 중심점 변경 + 줌 레벨 변경
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mLatitude, mLongitude), true);

            //출퇴근위치마커
            MapPOIItem commuteMarker = new MapPOIItem();
            commuteMarker.setItemName(content);
            commuteMarker.setTag(2);

            MapPoint commuteMapPoint = MapPoint.mapPointWithGeoCoord(mLatitude, mLongitude);
            commuteMarker.setMapPoint(commuteMapPoint);
            commuteMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            commuteMarker.setCustomImageResourceId(R.drawable.icn_marker_02); // 마커 이미지.
            commuteMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            commuteMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            mapView.addPOIItem(commuteMarker);
            mapView.selectPOIItem(commuteMarker, true);

            //근처기관
            MapPOIItem agencyMarker = new MapPOIItem();
            agencyMarker.setItemName(selectedName);
            agencyMarker.setTag(3);

            MapPoint agencyMapPoint = MapPoint.mapPointWithGeoCoord(agencyLatitude, agencyLongitude);
            agencyMarker.setMapPoint(agencyMapPoint);
            agencyMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            agencyMarker.setCustomImageResourceId(R.drawable.icn_marker_03); // 마커 이미지.
            agencyMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            agencyMarker.setCustomImageAnchor(0.5f, 0.75f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            mapView.addPOIItem(agencyMarker);

            MapPolyline polyline = new MapPolyline();
            polyline.setTag(1000);
            polyline.setLineColor(Color.argb(128, 0, 0, 0)); // Polyline 컬러 지정.

            // Polyline 좌표 지정.
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(mLatitude, mLongitude));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(agencyLatitude, agencyLongitude));

// Polyline 지도에 올리기.
            mapView.addPolyline(polyline);

// 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
            MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
            int padding = 140; // px
            mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

            String distanceToString = ((int) Utils.distance(mLatitude, mLongitude, agencyLatitude, agencyLongitude)) + "M";
            tv_meter.setText(distanceToString);

        }




    }


    @OnClick(R.id.btn_commuteConfirm)
    public void btn_commuteConfirmClicked() {
        switch (commuteType) {
            case 1:

                getToWork();

                break;

            case 2:

                finishWork();

                break;

        }

        Constant.LOG(TAG, "DEVICEIP: " + Utils.getIpAddress());

    }

    /*insert_att1_data - 출근 처리*/
    private void getToWork() {

        int vteacher_idx = UserInfo.getInstance().idx;
        int program_idx = UserInfo.getInstance().selectedService_idx;
        int subagency_idx = UserInfo.getInstance().selectedSubagency_idx;
        int vteacher_timetable_idx = 0;
        String act_date = mDate;
        String att_time = mTime;
        String att_latitude = String.valueOf(attLatitude);
        String att_longitude = String.valueOf(attLongitude);
        String att_addr = nowAddress;
        String leave_time = "00:00:00";
        String leave_latitude = String.valueOf(attLatitude);
        String leave_longitude = String.valueOf(attLongitude);
        String leave_addr = nowAddress;
        String add_date = mDate + " " + mTime;
        String add_ip = UserInfo.getInstance().userIp;
        String add_id = UserInfo.getInstance().vteacherId;

        Constant.LOG(TAG, "vteacher_idx : " + vteacher_idx + " / add_id : " + add_id);

        apiService.getToWork(vteacher_idx, program_idx, subagency_idx, vteacher_timetable_idx,
                act_date, att_time, att_latitude, att_longitude, att_addr, leave_time,
                leave_latitude, leave_longitude, leave_addr, add_date, add_ip, add_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    try {
                        Constant.LOG(TAG, "getToWork response : " + response.body().string());

                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                    afterGetToWorkCommuteInfo(vteacher_idx);

                } else {

                    Constant.LOG(TAG, "getToWork response err");
                    ((MainActivity) mContext).showSnackbar(mainLayout, mContext.getString(R.string.getToWork_msg_err_01));
                    ((MainActivity) mContext).refresh();
                    // dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.LOG(TAG, "getToWork onFailure : " + t.toString());
                ((MainActivity) mContext).showSnackbar(mainLayout, mContext.getString(R.string.getToWork_msg_err_02));
                ((MainActivity) mContext).refresh();
                // dismiss();
            }

        });

    }

    /*insert_att2_data - 퇴근 처리*/
    private void finishWork(){
      //  int att_idx = 122;  //test
        UserInfo.getInstance().attendance_att_idx =  PreferenceManager.getInt(mContext, "attendance_att_idx");
        int att_idx = UserInfo.getInstance().attendance_att_idx;
        String leave_time = mTime;
        String leave_latitude = String.valueOf(mLatitude);
        String leave_longitude = String.valueOf(mLongitude);
        String leave_addr = nowAddress;
        String edit_date = mDate + " " + mTime;
        String edit_ip = UserInfo.getInstance().userIp;
        String edit_id = UserInfo.getInstance().vteacherId;

        apiService.finishWork(att_idx, leave_time, leave_latitude, leave_longitude,
                leave_addr, edit_date, edit_ip, edit_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    try {
                        Constant.LOG(TAG, "finishWork response : " + response.body().string());

                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                    ((MainActivity) mContext).showSnackbar(mainLayout, mContext.getString(R.string.finishWork_msg_01));
                    ((MainActivity) mContext).refresh();
                    dismiss();

                } else {

                    Constant.LOG(TAG, "finishWork response err");
                    ((MainActivity) mContext).showSnackbar(mainLayout, mContext.getString(R.string.finishWork_msg_err_01));
                    ((MainActivity) mContext).refresh();
                    //   dismiss();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.LOG(TAG, "finishWork onFailure : " + t.toString());
                ((MainActivity) mContext).showSnackbar(mainLayout, mContext.getString(R.string.finishWork_msg_err_02));
                ((MainActivity) mContext).refresh();
                // dismiss();

            }

        });

    }

    // 갱신된 출퇴근정보 중 출근한 내역의 att_idx 가져오기
    public void afterGetToWorkCommuteInfo(int idx) {

        apiService.getCommuteInfo(idx, mDate).enqueue(new Callback<List<CommuteInfoListModel>>() {
            @Override
            public void onResponse(Call<List<CommuteInfoListModel>> call, Response<List<CommuteInfoListModel>> response) {

                if (response.isSuccessful()) {

                    List<CommuteInfoListModel> dataList = new ArrayList<>();
                    dataList = response.body();

                    String nowDate = Utils.getNowDateKR()+"";


                    Constant.LOG(TAG, "getCommuteInfo result : " + dataList);

                    if (dataList.size() > 0) {

                        Constant.LOG(TAG,"nowDate : " + nowDate + " / dataList.get(0).getActDate() : " + dataList.get(0).getActDate());

                        if (dataList.get(0).getActDate().equals(nowDate) && dataList.get(0).getAttTime().equals(mTime)) {

                            PreferenceManager.setInt(mContext, "attendance_att_idx", dataList.get(0).getAttIdx());
                            UserInfo.getInstance().attendance_att_idx = PreferenceManager.getInt(mContext, "attendance_att_idx");

                            ((MainActivity)mContext).showSnackbar(mainLayout, mContext.getString(R.string.afterGetToWorkCommuteInfo_msg_01));
                            ((MainActivity)mContext).refresh();
                            dismiss();

                        }else{

                            ((MainActivity)mContext).showSnackbar(mainLayout, mContext.getString(R.string.afterGetToWorkCommuteInfo_msg_err_01));
                            Constant.LOG(TAG, "출근 후 att_idx 내역 없음");

                        }

                    }else{

                        ((MainActivity)mContext).showSnackbar(mainLayout, mContext.getString(R.string.afterGetToWorkCommuteInfo_msg_err_01));
                        Constant.LOG(TAG, "출근 처리 안됨");

                    }

                } else {

                    ((MainActivity)mContext).showSnackbar(mainLayout, mContext.getString(R.string.afterGetToWorkCommuteInfo_msg_err_01));
                    Constant.LOG(TAG, "getCommuteInfo response err");

                }

            }

            @Override
            public void onFailure(Call<List<CommuteInfoListModel>> call, Throwable t) {
                ((MainActivity)mContext).showSnackbar(mainLayout, mContext.getString(R.string.afterGetToWorkCommuteInfo_msg_err_02));
                Constant.LOG(TAG, "getCommuteInfo err : " + t.toString());

            }

        });

    }

}
