package kr.firstcare.android.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import kr.firstcare.android.app.ui.fragment.faq.FaqCommuteFragment;
import kr.firstcare.android.app.ui.fragment.faq.FaqEtcFragment;
import kr.firstcare.android.app.ui.fragment.faq.FaqSignUpFragment;
import kr.firstcare.android.app.ui.fragment.userinfo.ChangePasswordFragment;
import kr.firstcare.android.app.ui.fragment.userinfo.ModifyUserInfoFragment;

/**
 * ClassName            UserInfoTabFragmentAdapter
 * Created by JSky on   2020-07-06
 * <p>
 * Description          기본정보 탭 어댑터
 */
public class UserInfoTabFragmentAdapter extends FragmentStateAdapter {
    public int mCount;

    public UserInfoTabFragmentAdapter(FragmentActivity fragmentActivity, int count) {
        super(fragmentActivity);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);


        switch (index) {
            case 0:
                return ModifyUserInfoFragment.newInstance(index + 1);
            case 1:
                return ChangePasswordFragment.newInstance(index + 1);
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
