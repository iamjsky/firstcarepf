package kr.firstcare.android.app.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import kr.firstcare.android.app.R;
import kr.firstcare.android.app.RetrofitGenerator;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.api.APIService;

/**
 * ClassName            BaseFragment
 * Created by JSky on   2020-06-12
 * <p>
 * Description          기본 정의 프래그먼트
 */
public class BaseFragment extends Fragment {

    public static final String TAG = "fc_debug";
    public RetrofitGenerator retrofitGenerator;
    public APIService apiService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofitGenerator = new RetrofitGenerator();
        apiService = retrofitGenerator.getApiService();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);



    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void showSnackbar(String msg){
        Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getResources().getColor(R.color.fc_boldColor));
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0,0,0,0);
        //   snackbarView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;




        snackbarView.setLayoutParams(params);

        TextView mTextView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        mTextView.setGravity(Gravity.TOP);
        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mTextView.setTypeface(mTextView.getTypeface(), Typeface.BOLD);
        mTextView.setTextColor(getActivity().getColor(R.color.white));
        mTextView.setTextSize(Utils.dpToPx(getContext(), getResources().getDimension(R.dimen._1sdp)));

        mTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);


        snackbar.getView().bringToFront();
        snackbar.show();
    }
    public void showSnackbar(View parent, String msg){
        Snackbar snackbar = Snackbar.make(parent, msg, Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(getResources().getColor(R.color.fc_boldColor));
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0,0,0,0);


        //   snackbarView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;




        snackbarView.setLayoutParams(params);

        TextView mTextView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        mTextView.setGravity(Gravity.TOP);
        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mTextView.setTypeface(mTextView.getTypeface(), Typeface.BOLD);
        mTextView.setTextColor(getActivity().getColor(R.color.white));
        mTextView.setTextSize(Utils.dpToPx(parent.getContext(), getResources().getDimension(R.dimen._1sdp)));

        mTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);


        snackbar.getView().bringToFront();
        snackbar.show();
    }


}
