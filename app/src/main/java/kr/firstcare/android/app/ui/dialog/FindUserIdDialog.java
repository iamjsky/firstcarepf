package kr.firstcare.android.app.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.firstcare.android.app.R;


/**
 * ClassName            CheckUserIdDialog
 * Created by JSky on   2020-06-22
 *
 * Description          아이디 중복확인
 */

public class FindUserIdDialog extends BaseDialog {

    private boolean isFullScreen = false;
    private View.OnClickListener mCloseButtonListener;

    private String mTitle="";

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.layout_contentArea)
    LinearLayout layout_contentArea;

    @BindView(R.id.iv_close)
    ImageView iv_close;

    @BindView(R.id.layout_content)
    LinearLayout layout_content;

    Context mContext;
    String content;

    public FindUserIdDialog(Context context, String title, String content, View.OnClickListener closeButtonListener) {
        super(context, R.style.FullScreenDialogStyle);
        mCloseButtonListener = closeButtonListener;
        mTitle = title;
        mContext = context;
        this.content = content;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_find_userid);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        iv_close.setOnClickListener(mCloseButtonListener);

        tv_title.setText(mTitle);
        tv_content.setText(content);

    }   //  onCreate

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);

    }

}
