package kr.firstcare.android.app.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.GpsTracker;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.adapter.AttAddressAdapter;
import kr.firstcare.android.app.adapter.BoardAdapter;
import kr.firstcare.android.app.adapter.CommuteInfoAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.data.PreferenceManager;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.AttAddressListModel;
import kr.firstcare.android.app.model.BoardListModel;
import kr.firstcare.android.app.model.CommuteInfoListModel;
import kr.firstcare.android.app.ui.activity.BoardActivity;
import kr.firstcare.android.app.ui.activity.CommuteInfoActivity;
import kr.firstcare.android.app.ui.activity.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * ClassName            SelectProfileImageDialog
 * Created by JSky on   2020-06-29
 *
 * Description          프로필 사진 업로드 다이얼로그
 */

public class SelectPlaceDialog extends BaseDialog {

    private View.OnClickListener mCloseButtonListener;

    private String mTitle="";

    @BindView(R.id.iv_close)
    ImageView iv_close;

    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    @BindView(R.id.tv_agencyName)
    TextView tv_agencyName;
    @BindView(R.id.tv_agencyAddress)
    TextView tv_agencyAddress;
    @BindView(R.id.rv_userListView)
    RecyclerView rv_userListView;
    @BindView(R.id.layout_resultEmpty)
    LinearLayout layout_resultEmpty;
    @BindView(R.id.layout_agency)
            LinearLayout layout_agency;


    Context mContext;

    private double nowLatitude = 0;
    private double nowLongitude = 0;

    CommuteConfirmDialog_backup commuteConfirmDialog;
    private AttAddressAdapter attAddressAdapter;
    private boolean isCheck = false;

    private String selectedItemAddr = "";
    String agencyAddr = "";
    String selectedName = "";

    public SelectPlaceDialog(Context context, View.OnClickListener closeButtonListener, double nowLatitude, double nowLongitude) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mContext = context;
        this.nowLatitude = nowLatitude;
        this.nowLongitude = nowLongitude;



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_place);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        /*스낵바를 뷰 최상위에 출력하기 위한 부모 뷰 설정*/
        mainLayout = findViewById(R.id.main_layout);

        iv_close.setOnClickListener(mCloseButtonListener);

        rv_userListView.setLayoutManager(new LinearLayoutManager(mContext));
        attAddressAdapter = new AttAddressAdapter(mContext);
        attAddressAdapter.setClickListener(userItemClickListener);
        rv_userListView.setAdapter(attAddressAdapter);

        selectedName =  UserInfo.getInstance().selectedSubagency_name+"";
        agencyAddr = UserInfo.getInstance().selectedSubagency_addr+"";
        tv_agencyName.setText(selectedName);
        tv_agencyAddress.setText(agencyAddr);

        getSelectAttAddr();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);



    }

    /*이용자 아이템 선택 리스너*/
    public AttAddressAdapter.ItemClickListener userItemClickListener = new AttAddressAdapter.ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            attAddressAdapter.index = position;
            AttAddressListModel selectedItem = attAddressAdapter.getItem(position);
            Constant.LOG(TAG, selectedItem.toString());
            selectedItemAddr = selectedItem.getAddr()+selectedItem.getAddrDetail()+"";
            selectedName = selectedItem.getChildName();
            layout_agency.setBackgroundResource(R.color.white);
            isCheck = true;
            attAddressAdapter.notifyDataSetChanged();

        }

    };

    /*제공기관 선택*/
    @OnClick(R.id.layout_agency)
    public void layout_agencyClicked(){
        isCheck = true;
        layout_agency.setBackgroundResource(R.color.fc_lightGrayColor);
        attAddressAdapter.setSelectEmpty();
        attAddressAdapter.notifyDataSetChanged();
        selectedItemAddr = UserInfo.getInstance().selectedSubagency_addr+"";
        selectedName =  UserInfo.getInstance().selectedSubagency_name+"";
    }


    /*선택하기 클릭 (**new**)*/
    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked(){

        Constant.LOG(TAG, "주석 해제 필요");
        if(isCheck){
            /*카카오 지도 중복 로딩 방지(충돌 일어남)*/
            Utils.waitTwoSec(btn_confirm);

            commuteConfirmDialog = new CommuteConfirmDialog_backup(mContext, Utils.getNowDateDay(),
                    Utils.getNowDate(),Utils.getNowTime(),
                    nowLatitude,nowLongitude,getToWorkCloseButtonListener, 1, selectedName, selectedItemAddr);

            /*출근확인 팝업*/
            commuteConfirmDialog.show();
            dismiss();

        }else{

            ((MainActivity) mContext).showSnackbar(mainLayout, "이용자를 선택해 주세요.");

        }



    }


    // 출근 확인 팝업 닫기
    private View.OnClickListener getToWorkCloseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            commuteConfirmDialog.dismiss();
            ((MainActivity)mContext).refresh();

        }

    };

    public void getSelectAttAddr(){

        int selectedSubagency_idx = UserInfo.getInstance().selectedSubagency_idx;
        int selectedService_idx = UserInfo.getInstance().selectedService_idx;

        apiService.getSelectAttAddr(selectedSubagency_idx, selectedService_idx).enqueue(new Callback<List<AttAddressListModel>>() {
            @Override
            public void onResponse(Call<List<AttAddressListModel>> call, Response<List<AttAddressListModel>> response) {

                if(response.isSuccessful()){

                    List<AttAddressListModel> dataList = new ArrayList<AttAddressListModel>();
                    dataList = response.body();
                    Constant.LOG(TAG, "getSelectAttAddr RESULT : " + dataList);
                    if(dataList.size() > 0) {

                        layout_resultEmpty.setVisibility(View.GONE);
                        rv_userListView.setVisibility(View.VISIBLE);

                        attAddressAdapter.addItem(dataList);
                        attAddressAdapter.notifyDataSetChanged();

                    }else{

                        layout_resultEmpty.setVisibility(View.VISIBLE);
                        rv_userListView.setVisibility(View.GONE);

                    }

                }else{

                    layout_resultEmpty.setVisibility(View.VISIBLE);
                    rv_userListView.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<List<AttAddressListModel>> call, Throwable t) {

                layout_resultEmpty.setVisibility(View.VISIBLE);
                rv_userListView.setVisibility(View.GONE);

            }

        });

    }



}
