package kr.firstcare.android.app.ui.fragment.signup.typev.check;

import android.content.Intent;
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
import kr.firstcare.android.app.ui.activity.SignUpActivity;
import kr.firstcare.android.app.ui.fragment.BaseFragment;

/**
 * ClassName            TypeVPermissionFragment
 * Created by JSky on   2020-06-12
 * <p>
 * Description          위치기반 서비스 이용약관 동의
 */
public class TypeVPermissionFragment extends BaseFragment {
    private int frag_num;
    public static TypeVPermissionFragment instance;

    @BindView(R.id.chkbox_permission)
    CheckBox chkbox_permission;

    @BindView(R.id.tv_permission)
    TextView tv_permission;

    public TypeVPermissionFragment() {
    }

    // newInstance constructor for creating fragment with arguments
    public static TypeVPermissionFragment newInstance(int num) {
        TypeVPermissionFragment fragment = new TypeVPermissionFragment();
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

        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        ButterKnife.bind(this, view);
        instance = this;
        tv_permission.setText(Utils.textFileReader(getActivity(), "fc_permission.txt"));

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
                    ((TypeVCheckSignUpFragment)getParentFragment()).setFragment(0);
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

    /*위치기반 서비스 이용약관 레이어 클릭*/
    @OnClick(R.id.layout_checkArea_01)
    public void layout_checkArea_01Clicked(){
        if(chkbox_permission.isChecked()) { chkbox_permission.setChecked(false); }
        else { chkbox_permission.setChecked(true); }

    }

    /*다음 클릭*/
    @OnClick(R.id.btn_next)
    public void btn_nextClicked(){
        if(chkbox_permission.isChecked()) {

            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

        } else {

            showSnackbar(getView(),getString(R.string.TypeVPermissionFragment_msg_permission_err));

        }

    }


}
