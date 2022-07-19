package kr.firstcare.android.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.firstcare.android.app.R;

/**
 * ClassName            DefaultPopUpDialog
 * Created by JSky on   2020-06-03
 *
 * Description          메세지 다이얼로그
 */

public class DefaultPopUpDialog extends BaseDialog {

    private boolean isFullScreen = false;
    private View.OnClickListener mCloseButtonListener;

    private String mTitle="";

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.layout_contentArea)
    LinearLayout layout_contentArea;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.layout_content)
    LinearLayout layout_content;

    ViewGroup mContentView;
 //   @BindView(R.id.btn_right)
 //   Button btnRight;
    Context mContext;

    public DefaultPopUpDialog(Context context, String title, ViewGroup contentView, View.OnClickListener closeButtonListener, boolean isFullScreen) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mTitle = title;
        mContentView = contentView;
        this.isFullScreen = isFullScreen;
        mContext = context;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_default_popup);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_content.getLayoutParams();
        if(isFullScreen){

            params.weight = 1;

        }else{

            params.weight = 0;

        }

        layout_content.setLayoutParams(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        iv_close.setOnClickListener(mCloseButtonListener);
        tv_title.setText(mTitle);

        layout_contentArea.removeAllViews();
        layout_contentArea.addView(mContentView);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);

    }

}
