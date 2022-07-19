package kr.firstcare.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName            UserAttendanceListModel
 * Created by JSky on   2020-06-22
 * <p>
 * Description          이용자 출석관리 리스트
 */
@Data
public class UserAttendanceListModel implements Serializable {

//    /*dummy*/
//    @SerializedName("idx")
//    @Expose
//    public Integer idx;
//    @SerializedName("profile_img")
//    @Expose
//    public String profile_img;
//    @SerializedName("user_name")
//    @Expose
//    public String user_name;
//    @SerializedName("user_age")
//    @Expose
//    public String user_age;
//    @SerializedName("user_gender")
//    @Expose
//    public String user_gender;
//    @SerializedName("user_phoneNum")
//    @Expose
//    public String user_phoneNum;
//    @SerializedName("att_stat")
//    @Expose
//    public String att_stat;
//    @SerializedName("ab_stat")
//    @Expose
//    public String ab_stat;
//    @SerializedName("att_changedTime")
//    @Expose
//    public String att_changedTime;
//    @SerializedName("ab_changedTime")
//    @Expose
//    public String ab_changedTime;


    @SerializedName("idx")
    @Expose
    public Integer idx;
    @SerializedName("vteacher_idx")
    @Expose
    public Integer vteacherIdx;
    @SerializedName("program_idx")
    @Expose
    public Integer programIdx;
    @SerializedName("child_idx")
    @Expose
    public Integer childIdx;
    @SerializedName("subagency_idx")
    @Expose
    public Integer subagencyIdx;
    @SerializedName("vteacher_timetable_idx")
    @Expose
    public Integer vteacherTimetableIdx;
    @SerializedName("vteacher_attendance_idx")
    @Expose
    public Integer vteacherAttendanceIdx;
    @SerializedName("child_attendance_idx")
    @Expose
    public Integer childAttendanceIdx;
    @SerializedName("child_name")
    @Expose
    public String childName;
    @SerializedName("sex_flag")
    @Expose
    public String sexFlag;
    @SerializedName("parent_name")
    @Expose
    public String parentName;
    @SerializedName("hp")
    @Expose
    public String hp;
    @SerializedName("photo")
    @Expose
    public String photo;
    @SerializedName("birthday")
    @Expose
    public String birthday;
    @SerializedName("att_abs_flag")
    @Expose
    public String attAbsFlag;
    @SerializedName("sub_agency_name")
    @Expose
    public String subAgencyName;
    @SerializedName("sub_zipcode")
    @Expose
    public String subZipcode;
    @SerializedName("sub_addr")
    @Expose
    public String subAddr;
    @SerializedName("sub_addr_detail")
    @Expose
    public String subAddrDetail;
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
    public String attLatitude;
    @SerializedName("att_longitude")
    @Expose
    public String attLongitude;
    @SerializedName("att_addr")
    @Expose
    public String attAddr;
    @SerializedName("add_date")
    @Expose
    public String addDate;
    @SerializedName("add_id")
    @Expose
    public String addId;
    @SerializedName("leave_time")
    @Expose
    public String leaveTime;
    @SerializedName("leave_latitude")
    @Expose
    public String leaveLatitude;
    @SerializedName("leave_longitude")
    @Expose
    public String leaveLongitude;
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
    @SerializedName("zipcode")
    @Expose
    public String zipcode;
    @SerializedName("addr")
    @Expose
    public String addr;
    @SerializedName("addr_detail")
    @Expose
    public String addrDetail;

}