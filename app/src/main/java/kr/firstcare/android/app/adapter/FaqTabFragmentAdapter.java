package kr.firstcare.android.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import kr.firstcare.android.app.ui.fragment.faq.FaqCommuteFragment;
import kr.firstcare.android.app.ui.fragment.faq.FaqEtcFragment;
import kr.firstcare.android.app.ui.fragment.faq.FaqSignUpFragment;

/**
 * ClassName            FaqTabFragmentAdapter
 * Created by JSky on   2020-06-04
 * <p>
 * Description          FAQ 탭 어댑터 (백업)
 */
public class FaqTabFragmentAdapter extends FragmentStateAdapter {
    public int mCount;

    public FaqTabFragmentAdapter(FragmentActivity fragmentActivity, int count) {
        super(fragmentActivity);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);


        switch (index) {
            case 0:
                return FaqSignUpFragment.newInstance(index + 1);
            case 1:
                return FaqCommuteFragment.newInstance(index + 1);
            case 2:
                return FaqEtcFragment.newInstance(index + 1);
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
