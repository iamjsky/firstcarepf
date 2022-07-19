package kr.firstcare.android.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import kr.firstcare.android.app.R;
import kr.firstcare.android.app.Utils;
import kr.firstcare.android.app.model.ProgramListModel;


public class ServiceSpinnerAdapter extends ArrayAdapter<ProgramListModel> implements SpinnerAdapter {

    Context context;
    List<ProgramListModel> data;
    LayoutInflater inflater;
    int dropResource;
    int topResource;



    public ServiceSpinnerAdapter(Context context, int resource, List<ProgramListModel> data){
        super(context, resource, data);
        clearItem();
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.topResource = resource;
    }


    @Override
    public int getCount() {
        if(data!=null) return data.size();
        else return 0;
    }
    @Override
    public ProgramListModel getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        if(convertView==null) {
//            convertView = inflater.inflate(R.layout.spinner_service_normal, parent, false);
//
//            if(data!=null){
//                //데이터세팅
//                ProgramListModel list = data.get(position);
//
//                TextView spinnerNormalText = (TextView) convertView.findViewById(R.id.spinnerText);
//                if(list.getTitle() != null){
//                    spinnerNormalText.setText(list.getTitle());
//                }
//
//            }
//        }



        return createTopView(position, convertView, parent);
    }



    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        this.dropResource = resource;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        if(convertView==null){
//            convertView = inflater.inflate(R.layout.spinner_service_dropdown, parent, false);
//
//        }
//
//        //데이터세팅
//        ProgramListModel list = data.get(position);
//        if(list.getTitle() != null){
//            ((TextView)convertView.findViewById(R.id.spinnerText)).setText(list.getTitle());
//        }


        return createDropView(position, convertView, parent);
    }
    private View createTopView(int position, View convertView, ViewGroup parent){
        final View view = inflater.inflate(topResource, parent, false);
        if (data != null) {
//                //데이터세팅
            ProgramListModel list = data.get(position);

            TextView spinnerNormalText = (TextView) view.findViewById(R.id.spinnerText);
            String year = "";
            String title = "";

            if (list.getYear() != null) {
                year = list.getYear();
            }

            if (list.getTitle() != null) {
                title = list.getTitle();
            }

            String spinnerText = year + " " + title;

            spinnerNormalText.setText(spinnerText);

        }

        return view;
    }

    private View createDropView(int position, View convertView, ViewGroup parent){
        final View view = inflater.inflate(dropResource, parent, false);
        if (data != null) {
//                //데이터세팅
            ProgramListModel list = data.get(position);

            TextView spinnerNormalText = (TextView) view.findViewById(R.id.spinnerText);
            String year = "";
            String title = "";

            if (list.getYear() != null) {
                year = list.getYear();
            }

            if (list.getTitle() != null) {
                title = list.getTitle();
            }

            String spinnerText = year + " " + title;

            spinnerNormalText.setText(spinnerText);


        }
        return view;
    }

    public void clearItem(){
        if(data != null){
            data.clear();
            notifyDataSetChanged();
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


}
