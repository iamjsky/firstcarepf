package kr.firstcare.android.app.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.firstcare.android.app.R;

/**
 * ClassName            BaseDialogFragment
 * Created by JSky on   2020-06-12
 * <p>
 * Description          기본 정의 다이얼로그 프래그먼트
 */
public class BaseDialogFragment extends DialogFragment {

    public static final String TAG = "fc_debug";


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

        }

    }   //  onStart

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

    }   //  onCreate






}
