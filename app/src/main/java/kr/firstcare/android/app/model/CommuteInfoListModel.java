package kr.firstcare.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * ClassName            CommuteInfoListModel
 * Created by JSky on   2020-06-17
 * <p>
 * Description          출퇴근 정보 리스트
 */

@Data
public class CommuteInfoListModel {



    @SerializedName("idx")
    @Expose
    public Integer idx;
    @SerializedName("vteacher_idx")
    @Expose
    public Integer vteacherIdx;
    @SerializedName("program_idx")
    @Expose
    public Integer programIdx;
    @SerializedName("subagency_idx")
    @Expose
    public Integer subagencyIdx;
    @SerializedName("vteacher_timetable_idx")
    @Expose
    public Integer vteacherTimetableIdx;
    @SerializedName("sub_agency_name")
    @Expose
    public String subAgencyName;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("act_date")
    @Expose
    public String actDate;
    @SerializedName("att_time")
    @Expose
    public String attTime;
    @SerializedName("att_latitude")
    @Expose
    public Double attLatitude;
    @SerializedName("att_longitude")
    @Expose
    public Double attLongitude;
    @SerializedName("att_addr")
    @Expose
    public String attAddr;
    @SerializedName("leave_time")
    @Expose
    public String leaveTime;
    @SerializedName("leave_latitude")
    @Expose
    public Double leaveLatitude;
    @SerializedName("leave_longityde")
    @Expose
    public Double leaveLongityde;
    @SerializedName("leave_addr")
    @Expose
    public String leaveAddr;
    @SerializedName("del_flag")
    @Expose
    public String delFlag;
    @SerializedName("add_ip")
    @Expose
    public String addIp;
    @SerializedName("att_idx")
    @Expose
    public Integer attIdx;
    @SerializedName("edit_date")
    @Expose
    public String editDate;
    @SerializedName("edit_id")
    @Expose
    public String editId;
    @SerializedName("edit_ip")
    @Expose
    public String editIp;


    //add
    @SerializedName("sub_zipcode")
    @Expose
    public String sub_zipcode;
    @SerializedName("sub_addr")
    @Expose
    public String sub_addr;
    @SerializedName("sub_addr_detail")
    @Expose
    public String sub_addr_detail;

    //add 0630
    @SerializedName("vteacher_attendance_idx")
    @Expose
    public int vteacher_attendance_idx;
    @SerializedName("child_idx")
    @Expose
    public int child_idx;
    @SerializedName("child_name")
    @Expose
    public String child_name;
    @SerializedName("sex_flag")
    @Expose
    public String sex_flag;
    @SerializedName("parent_name")
    @Expose
    public String parent_name;
    @SerializedName("photo")
    @Expose
    public String photo;
    @SerializedName("hp")
    @Expose
    public String hp;
    @SerializedName("birthday")
    @Expose
    public String birthday;
    @SerializedName("child_attendance_idx")
    @Expose
    public String child_attendance_idx;
    @SerializedName("att_abs_flag")
    @Expose
    public String att_abs_flag;
}
