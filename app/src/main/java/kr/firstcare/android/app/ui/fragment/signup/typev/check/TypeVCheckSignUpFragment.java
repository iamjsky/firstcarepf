package kr.firstcare.android.app.ui.fragment.signup.typev.check;

import android.app.Dialog;
import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.vejei.viewpagerindicator.indicator.CircleIndicator;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.adapter.CheckSignUpFragmentAdapter;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.ui.activity.SelectSignUpTypeActivity;
import kr.firstcare.android.app.ui.fragment.BaseDialogFragment;

/**
 * ClassName            TypeVCheckSignUpFragment
 * Created by JSky on   2020-06-12
 * <p>
 * Description          약관동의 프래그먼트
 */
public class TypeVCheckSignUpFragment extends BaseDialogFragment {

    public static final String TAG = "fc_debug";

    private Fragment fragment;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.vp_fragmentPager)
    ViewPager2 vp_fragmentPager;
    @BindView(R.id.ic_indicator)
    CircleIndicator ic_indicator;

    private FragmentStateAdapter pagerAdapter;
    private int numPage = 2;
    private int nowPage = 0;

    private String[] titleList;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("CheckSignUpFragment");

    }   //  onCreate

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_signup, container, false);
        ButterKnife.bind(this, view);

        titleList = new String[] { getString(R.string.TypeVCheckSignUpFragment_text_CheckTitle_01), getString(R.string.TypeVCheckSignUpFragment_text_CheckTitle_02) };

        pagerAdapter = new CheckSignUpFragmentAdapter(this, numPage);

        vp_fragmentPager.setAdapter(pagerAdapter);
        vp_fragmentPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vp_fragmentPager.setCurrentItem(2);
        vp_fragmentPager.setOffscreenPageLimit(2);
        vp_fragmentPager.setUserInputEnabled(false);

        ic_indicator.setWithViewPager2(vp_fragmentPager, false);
        ic_indicator.setItemCount(numPage);
        ic_indicator.setAnimationMode(CircleIndicator.AnimationMode.SLIDE);

        vp_fragmentPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    vp_fragmentPager.setCurrentItem(position);

                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                nowPage = position;
                Constant.LOG(TAG, "nowPage" + nowPage + "position" + position + " titleList[position]" + titleList[position]);
                tv_title.setText(titleList[position]);

            }

        });



        return view;

    }   //  onCreateView

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);
    }   //  onViewCreated

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    iv_backClicked();

                    return true;
                }

                return false;
            }


        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*회원가입 약관 동의 (이용약관, 위치기반 약관) 프래그먼트 컨트롤*/
    public void setFragment(int num) {

        /**
         *  0   이용약관 동의
         *  1   위치기반 서비스 이용약관 동의
         */

        switch (num) {

            case 0:

                vp_fragmentPager.setCurrentItem(0);

                break;

            case 1:

                vp_fragmentPager.setCurrentItem(1);

                break;

        }

    }

    /*뒤로가기 클릭*/
    @OnClick(R.id.iv_back)
    public void iv_backClicked(){

        switch (vp_fragmentPager.getCurrentItem()) {

            case 0:

                Dialog dialog = getDialog();
                ((SelectSignUpTypeActivity)getActivity()).setSelectType(-1);
                if (dialog != null) {

                    dialog.dismiss();

                }

                break;

            case 1:

                vp_fragmentPager.setCurrentItem(0);

                break;

        }

    }


}
