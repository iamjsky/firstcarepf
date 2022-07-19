package kr.firstcare.android.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import kr.firstcare.android.app.ui.fragment.signup.typev.TypeVSignUpFirstFragment;
import kr.firstcare.android.app.ui.fragment.signup.typev.TypeVSignUpSecondFragment;

/**
 * ClassName            SignUpFragmentAdapter
 * Created by JSky on   2020-06-04
 * <p>
 * Description          로그인/회원가입/아이디,비밀번호찾기
 */
public class SignUpFragmentAdapter extends FragmentStateAdapter {
    public int mCount;

    public SignUpFragmentAdapter(FragmentActivity fragmentActivity, int count) {
        super(fragmentActivity);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);


        switch (index) {
            case 0:
                return TypeVSignUpFirstFragment.newInstance(index + 1);
            case 1:
                return TypeVSignUpSecondFragment.newInstance(index + 1);

            default:
                return null;
        }


    }


    @Override
    public int getItemCount() {
        return mCount;
    }

    public int getRealPosition(int position) {
        return position % mCount;
    }



}
