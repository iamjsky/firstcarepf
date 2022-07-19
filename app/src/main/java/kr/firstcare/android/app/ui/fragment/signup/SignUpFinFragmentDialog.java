package kr.firstcare.android.app.ui.fragment.signup;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.data.SignUp;
import kr.firstcare.android.app.ui.activity.LoginActivity;
import kr.firstcare.android.app.ui.fragment.BaseDialogFragment;

/**
 * ClassName            SignUpFinFragmentDialog
 * Created by JSky on   2020-06-12
 * <p>
 * Description          회원가입 완료 프래그먼트
 */
public class SignUpFinFragmentDialog extends BaseDialogFragment {

    public static final String TAG = "fc_debug";
    private Fragment fragment;

    private int num;

    ViewGroup mContentView;

    public SignUpFinFragmentDialog() {

    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);


//        Constant.LOG(TAG, "SignUp DATA : " + SignUp.getInstance().vteacher_id);
//        Constant.LOG(TAG, "SignUp DATA : " + SignUp.getInstance().userpw);
//        Constant.LOG(TAG, "SignUp DATA : " + SignUp.getInstance().vteacher_name);
//        Constant.LOG(TAG, "SignUp DATA : " + SignUp.getInstance().hp);
//        Constant.LOG(TAG, "SignUp DATA : " + SignUp.getInstance().email);
//        Constant.LOG(TAG, "SignUp DATA : " + SignUp.getInstance().email_flag);
//        Constant.LOG(TAG, "SignUp DATA : " + SignUp.getInstance().addr);
//        Constant.LOG(TAG, "SignUp DATA : " + SignUp.getInstance().addr_detail);
//        Constant.LOG(TAG, "SignUp DATA : " + SignUp.getInstance().agency_idx);
//        Constant.LOG(TAG, "SignUp DATA : " + SignUp.getInstance().sex_flag);


    }   //  onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_fin_dialog, container, false);
        ButterKnife.bind(this, view);

        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("SignUpFinFragmentDialog");

        return view;

    }   //  onCreateView

    /*확인 클릭*/
    @OnClick(R.id.btn_confirm)
    public void btn_confirmClicked(){
        if (fragment != null) {

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

        }

    }



}
