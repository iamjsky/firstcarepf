package kr.firstcare.android.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.data.UserInfo;
import kr.firstcare.android.app.model.AttAddressListModel;
import kr.firstcare.android.app.model.CommuteInfoListModel;

/**
 * ClassName            AttAddressAdapter
 * Created by JSky on   2020-07-03
 * <p>
 * Description          약속장소 선택 어댑터
 */
public class AttAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<AttAddressListModel> mData;
    private LayoutInflater mInflater;
    private AttAddressAdapter.ItemClickListener mClickListener;
    public int index = -1;


    public AttAddressAdapter(Context context) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);



    }

    public void addItem(List<AttAddressListModel> data) {
            this.mData = data;


        notifyDataSetChanged();
    }

    public void removeItem(int id){
        mData.remove(id);
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;

            view = mInflater.inflate(R.layout.item_place, parent, false);
            holder = new ItemViewHolder(view);


        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        AttAddressListModel list = mData.get(position);

        if(list.getChildName() != null && !list.getChildName().equals("")){
            itemViewHolder.tv_name.setText(list.getChildName());
        }else{
            itemViewHolder.tv_name.setText("이름 없음");
        }

        if(list.getAddr() != null && !list.getAddr().equals("")){
            itemViewHolder.tv_address.setText(list.getAddr());
        }else{
            itemViewHolder.tv_name.setText("-");
        }
        if(list.getAddrDetail() != null && !list.getAddrDetail().equals("")){
            String address = itemViewHolder.tv_address.getText().toString() + list.getAddrDetail() + "";
            itemViewHolder.tv_address.setText(address);
        }
        if(index == position){
            itemViewHolder.layout_item.setBackgroundResource(R.color.fc_lightGrayColor);
        }else{
            itemViewHolder.layout_item.setBackgroundResource(R.color.white);
        }





    }

    public void setSelectEmpty(){
        index = -1;
        notifyDataSetChanged();
    }
    // total number of cells
    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        }
        return 0;
    }


    public void waitTwoSec(View v){

        v.setClickable(false);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                v.setClickable(true);


            }
        }, 1500); // 2
    }


    // stores and recycles views as they are scrolled off screen
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        TextView tv_address;
        LinearLayout layout_item;




        ItemViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            layout_item = itemView.findViewById(R.id.layout_item);
            itemView.setOnClickListener(this);


        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
        public View getView(){
            return itemView;
        }


    }



    // convenience method for getting data at click position
    public AttAddressListModel getItem(int id) {

        return mData.get(id);
    }

    public void setClickListener(AttAddressAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }






}
