package kr.firstcare.android.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.data.Constant;
import kr.firstcare.android.app.model.AgencyListModel;
import kr.firstcare.android.app.model.UserAttendanceListModel;

/**
 * ClassName            UserAttendanceAdapter
 * Created by JSky on   2020-06-16
 * <p>
 * Description          이용자 출석 관리 어댑터
 */
public class UserAttendanceAdapter extends RecyclerView.Adapter<UserAttendanceAdapter.ViewHolder> {
    private Context mContext;
    private List<UserAttendanceListModel> mData, backUpData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private OnSetAttendanceButtonClickListener onSetAttendanceButtonClickListener;
    private OnSetAbsenceButtonClickListener onSetAbsenceButtonClickListener;
    private OnCancelAttendanceButtonClickListener onCancelAttendanceButtonClickListener;
    private OnDeleteAttendanceButtonClickListener onDeleteAttendanceButtonClickListener;

    public interface OnSetAttendanceButtonClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnSetAbsenceButtonClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnCancelAttendanceButtonClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnDeleteAttendanceButtonClickListener {
        public void onItemClick(View view, int position);
    }

    // data is passed into the constructor
    public UserAttendanceAdapter(Context context) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);



    }

    public void addItem(List<UserAttendanceListModel> data) {
            this.mData = data;
            this.backUpData = data;


        notifyDataSetChanged();
    }

    public void clearItem(){
        if(mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_user_attendance, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserAttendanceListModel list = mData.get(position);

        if(list.getPhoto() != null && !list.getPhoto().equals("")) {
            Glide.with(mContext).load(Constant.CHILD_PROFILE_IMG_URL+list.getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.iv_profile);

        }else{
            holder.iv_profile.setImageResource(R.color.fc_mainColor);
        }


        if(list.getChildName() != null && !list.getChildName().equals("")){
            holder.tv_userName.setText(list.getChildName());
        }else{
            holder.tv_userName.setText("-");
        }

        if(list.getBirthday() != null && !list.getBirthday().equals("")){
            Constant.LOG("fc_debug", "list.getBirthday():"+list.getBirthday());
            String age = Utils.birthDayToAge(list.getBirthday())+"세";

            holder.tv_userAge.setText(age);
        }else{
            holder.tv_userAge.setText("-");
        }

        if(list.getSexFlag() != null && !list.getSexFlag().equals("")){
            String gender = list.getSexFlag();
            gender = gender.substring(0, 1);
            holder.tv_userGender.setText(gender);
        }else{
            holder.tv_userGender.setText("-");
        }
        if(list.getHp() != null && !list.getHp().equals("")){
            String userPhoneNum = list.getHp();
            if(userPhoneNum.length() > 3){
                userPhoneNum = userPhoneNum.substring(userPhoneNum.length()-4, userPhoneNum.length());
            }else{
                userPhoneNum = "-" + userPhoneNum;
            }

            holder.tv_userPhoneNum.setText(userPhoneNum);
        }else{
            holder.tv_userPhoneNum.setText("-");
        }

        /*getChildAttendanceIdx = -1 일 경우 [출석] [결석] 버튼 활성화*/
        if((list.getChildAttendanceIdx() != null && list.getChildAttendanceIdx() != -1)) {

            holder.layout_Attendance.setVisibility(View.GONE);
            holder.layout_cancelAttendance.setVisibility(View.VISIBLE);

        } else {
            holder.layout_Attendance.setVisibility(View.VISIBLE);
            holder.layout_cancelAttendance.setVisibility(View.GONE);
        }

        if(list.getAttTime() != null && !list.getAttTime().equals("")) {

            holder.tv_changedAt.setText(list.getAttTime());
        } else {

            holder.tv_changedAt.setText("-");
        }

        /*출결석 취소 버튼*/
        if(list.getChildAttendanceIdx() != -1 && list.getAttAbsFlag() != null
                && !list.getAttAbsFlag().equals("")){
            String attFlag = list.getAttAbsFlag()+"취소";
            holder.btn_cancelAttendance.setText(attFlag);

        }

        /*미등록 버튼*/
        if(list.getChildAttendanceIdx() == -1 && list.getAttAbsFlag() != null
                && !list.getAttAbsFlag().equals("") && list.getAttAbsFlag().equals("미등록") ){
            String attFlag = list.getAttAbsFlag();
            holder.btn_deleteAttendance.setText(attFlag);
        }


        holder.btn_setAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetAttendanceButtonClickListener.onItemClick(v, position);
            }
        });

        holder.btn_setAbsence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetAbsenceButtonClickListener.onItemClick(v, position);
            }
        });

        holder.btn_cancelAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelAttendanceButtonClickListener.onItemClick(v, position);
            }
        });

        holder.btn_deleteAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteAttendanceButtonClickListener.onItemClick(v, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        }
        return 0;
    }




    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView iv_profile;
        TextView tv_userName;
        TextView tv_userAge;
        TextView tv_userGender;
        TextView tv_userPhoneNum;



        LinearLayout layout_Attendance;
        Button btn_setAttendance;
        Button btn_setAbsence;

        LinearLayout layout_cancelAttendance;
        TextView tv_changedAt;
        Button btn_cancelAttendance;
        Button btn_deleteAttendance;

        ViewHolder(View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_userName = itemView.findViewById(R.id.tv_userName);
            tv_userAge = itemView.findViewById(R.id.tv_userAge);
            tv_userGender = itemView.findViewById(R.id.tv_userGender);
            tv_userPhoneNum = itemView.findViewById(R.id.tv_userPhoneNum);

            layout_Attendance = itemView.findViewById(R.id.layout_Attendance);
            btn_setAttendance = itemView.findViewById(R.id.btn_setAttendance);
            btn_setAbsence = itemView.findViewById(R.id.btn_setAbsence);

            layout_cancelAttendance = itemView.findViewById(R.id.layout_cancelAttendance);
            tv_changedAt = itemView.findViewById(R.id.tv_changedAt);
            btn_cancelAttendance = itemView.findViewById(R.id.btn_cancelAttendance);
            btn_deleteAttendance = itemView.findViewById(R.id.btn_deleteAttendance);


            itemView.setOnClickListener(this);

        }



        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }



    // convenience method for getting data at click position
    public UserAttendanceListModel getItem(int id) {

        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public void setOnSetAttendanceButtonClickListener(OnSetAttendanceButtonClickListener onSetAttendanceButtonClickListener){
        this.onSetAttendanceButtonClickListener = onSetAttendanceButtonClickListener;
    }

    public void setOnSetAbsenceButtonClickListener(OnSetAbsenceButtonClickListener onSetAbsenceButtonClickListener){
        this.onSetAbsenceButtonClickListener = onSetAbsenceButtonClickListener;
    }

    public void setOnCancelAttendanceButtonClickListener(OnCancelAttendanceButtonClickListener onCancelAttendanceButtonClickListener){
        this.onCancelAttendanceButtonClickListener = onCancelAttendanceButtonClickListener;
    }

    public void setOnDeleteAttendanceButtonClickListener(OnDeleteAttendanceButtonClickListener onDeleteAttendanceButtonClickListener){
        this.onDeleteAttendanceButtonClickListener = onDeleteAttendanceButtonClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void filter(String searchText) {

        if (searchText.length() == 0) {//jika string kosong
            List<UserAttendanceListModel> filteredList = new ArrayList<>();
            for (UserAttendanceListModel data : backUpData) {
                //   if (Utils.SoundSearcher.matchString(data.getAgencyName(),searchText)) {
                filteredList.add(data);
                //  }
            }

            mData = filteredList;
        } else {//filter dan buat arraybaru dengan filter query model
            List<UserAttendanceListModel> filteredList = new ArrayList<>();

            for (UserAttendanceListModel data : backUpData) {
                if (Utils.SoundSearcher.matchString(data.getChildName(),searchText)) {
                    filteredList.add(data);
                }
            }




            mData = filteredList;

        }
        //memperbarui list adapter
        notifyDataSetChanged();
    }


}
