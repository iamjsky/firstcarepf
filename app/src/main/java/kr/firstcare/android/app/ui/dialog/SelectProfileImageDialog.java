package kr.firstcare.android.app.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.ui.activity.CommuteInfoActivity;
import kr.firstcare.android.app.ui.activity.MainActivity;


/**
 * ClassName            SelectProfileImageDialog
 * Created by JSky on   2020-06-29
 *
 * Description          프로필 사진 업로드 다이얼로그
 */

public class SelectProfileImageDialog extends BaseDialog {

    private View.OnClickListener mCloseButtonListener;

    private String mTitle="";

    @BindView(R.id.iv_close)
    ImageView iv_close;


    @BindView(R.id.iv_checkAlbum)
    ImageView iv_checkAlbum;
    @BindView(R.id.iv_checkCamera)
    ImageView iv_checkCamera;


    Context mContext;
    int parentType=-1;
    private int selectType = 0;


    public SelectProfileImageDialog(Context context, View.OnClickListener closeButtonListener, int parentType) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mContext = context;
        this.parentType = parentType;



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_profileimg);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        iv_close.setOnClickListener(mCloseButtonListener);

        setCheck(selectType);

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
               iv_checkAlbum.setImageResource(R.drawable.bg_circle_02);
               iv_checkCamera.setImageResource(R.drawable.bg_circle_01);

           break;

           case 1:
               iv_checkAlbum.setImageResource(R.drawable.bg_circle_01);
               iv_checkCamera.setImageResource(R.drawable.bg_circle_02);

               break;
       }

    }

    /*앨범 선택*/
    @OnClick(R.id.layout_checkAlbum)
    public void layout_checkAlbumClicked(){
        selectType = 0;
        setCheck(selectType);

    }

    /*촬영 선택*/
    @OnClick(R.id.layout_checkCamera)
    public void layout_checkCameraClicked(){
        selectType = 1;
        setCheck(selectType);
    }

    /*확인*/
    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked(){

        if(selectType == 0){


        }else if(selectType == 1){

        }
//        switch (parentType) {
//            case 0:
//                ((MainActivity) mContext).refresh();
//                break;
//
//            case 1:
//                ((CommuteInfoActivity) mContext).refresh();
//                break;
//        }
//
//        dismiss();

    }


}
