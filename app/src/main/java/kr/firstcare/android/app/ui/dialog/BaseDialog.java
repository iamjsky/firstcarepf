package kr.firstcare.android.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import butterknife.ButterKnife;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.RetrofitGenerator;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.api.APIService;


/**
 * ClassName            BaseDialog
 * Created by JSky on   2020-06-03
 *
 * Description          기본 정의 다이얼로그
 */

public class BaseDialog extends Dialog {
    public Context mContext;
    public static final String TAG = "fc_debug";

    public RetrofitGenerator retrofitGenerator;
    public APIService apiService;

    public View mainLayout;

    public BaseDialog(@NonNull Context context) {
        super(context);

    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    }   //  onCreate

    @Override
    public void setContentView(int layoutResID) {
        retrofitGenerator = new RetrofitGenerator();
        apiService = retrofitGenerator.getApiService();

        super.setContentView(layoutResID);
        ButterKnife.bind(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point pt = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(pt);

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);

    }





}
