package kr.firstcare.android.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.model.BoardListModel;

/**
 * ClassName            BoardAdapter
 * Created by JSky on   2020-06-16
 * <p>
 * Description          게시판 어댑터
 */
public class BoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<BoardListModel> mData;
    private LayoutInflater mInflater;
    private BoardAdapter.ItemClickListener mClickListener;


    public BoardAdapter(Context context) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);



    }

    public void addItem(List<BoardListModel> data) {
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

            view = mInflater.inflate(R.layout.item_board, parent, false);
            holder = new ItemViewHolder(view);


        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        BoardListModel list = mData.get(position);

        if(list.getSubject() != null && !list.getSubject().equals("")){
            itemViewHolder.tv_subject.setText(list.getSubject());
        }else{
            itemViewHolder.tv_subject.setText("제목 없음");
        }

        if(list.getAddDate() != null && !list.getAddDate().equals("")){
            String date =  Utils.formateDateFromstring("yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", list.getAddDate());
            itemViewHolder.tv_date.setText(date);
        }else{
            itemViewHolder.tv_date.setText("-");
        }

        if(list.getName() != null && !list.getName().equals("")){
            itemViewHolder.tv_name.setText(list.getName());
        }else{
            itemViewHolder.tv_name.setText("이름 없음");
        }





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
        LinearLayout layout_item;
        TextView tv_subject;
        TextView tv_date;
        TextView tv_name;




        ItemViewHolder(View itemView) {
            super(itemView);
            layout_item = itemView.findViewById(R.id.layout_item);
            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_name = itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);



        }

        public View getView(){
            return itemView;
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public void setClickListener(BoardAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // convenience method for getting data at click position
    public BoardListModel getItem(int id) {

        return mData.get(id);
    }





}
