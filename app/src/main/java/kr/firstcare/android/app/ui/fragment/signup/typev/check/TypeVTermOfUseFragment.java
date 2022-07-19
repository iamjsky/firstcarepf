package kr.firstcare.android.app.ui.fragment.signup.typev.check;

import android.os.Bundle;
import kr.firstcare.android.app.data.Constant;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.ui.activity.SelectSignUpTypeActivity;
import kr.firstcare.android.app.ui.fragment.BaseFragment;

/**
 * ClassName            TypeVTermOfUseFragment
 * Created by JSky on   2020-06-12
 * <p>
 * Description          이용약관 동의
 */
public class TypeVTermOfUseFragment extends BaseFragment {
    private int frag_num;
    public static TypeVTermOfUseFragment instance;

    @BindView(R.id.chkbox_term)
    CheckBox chkbox_term;
    @BindView(R.id.chkbox_privacy)
    CheckBox chkbox_privacy;

    @BindView(R.id.tv_termOfUse)
    TextView tv_termOfUse;
    @BindView(R.id.tv_privacy)
    TextView tv_privacy;

    public TypeVTermOfUseFragment() {
    }

    // newInstance constructor for creating fragment with arguments
    public static TypeVTermOfUseFragment newInstance(int num) {
        TypeVTermOfUseFragment fragment = new TypeVTermOfUseFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frag_num = getArguments().getInt("num", 0);

    }   //  onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_term_of_use, container, false);
        ButterKnife.bind(this, view);
        instance = this;

        tv_termOfUse.setText(Utils.textFileReader(getActivity(), Constant.FILE_TERM_OF_USE));
        tv_privacy.setText(Utils.textFileReader(getActivity(), Constant.FILE_PRIVACY));

        return view;

    }   //  onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
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
                    ((SelectSignUpTypeActivity)getActivity()).setSelectType(-1);
                    ((TypeVCheckSignUpFragment)getParentFragment()).iv_backClicked();

                    return true;
                }

                return false;
            }


        });

    }   //  onResume

    @Override
    public void onStop() {
        super.onStop();
    }

    /*이용약관 동의 레이어 클릭*/
    @OnClick(R.id.layout_checkArea_01)
    public void layout_checkArea_01Clicked(){
        if(chkbox_term.isChecked()) { chkbox_term.setChecked(false);  }
        else { chkbox_term.setChecked(true); }

    }

    /*개인정보 수집 동의 레이어 클릭*/
    @OnClick(R.id.layout_checkArea_02)
    public void layout_checkArea_02Clicked(){
        if(chkbox_privacy.isChecked()) { chkbox_privacy.setChecked(false); }
        else { chkbox_privacy.setChecked(true); }

    }

    /*다음 클릭*/
    @OnClick(R.id.btn_Next)
    public void btn_NextClicked(){
        if(!chkbox_term.isChecked()){

            showSnackbar(getView(),getString(R.string.TypeVTermOfUseFragment_msg_termofuse_err));

        }else if(!chkbox_privacy.isChecked()){

            showSnackbar(getView(),getString(R.string.TypeVTermOfUseFragment_msg_privacy_err));

        }
        else if(chkbox_term.isChecked() && chkbox_privacy.isChecked()) {

            ((TypeVCheckSignUpFragment)getParentFragment()).setFragment(1);

        }


    }

}
