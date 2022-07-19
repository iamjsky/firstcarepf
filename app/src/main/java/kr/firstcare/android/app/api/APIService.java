package kr.firstcare.android.app.api;



import java.util.ArrayList;
import java.util.List;

import kr.firstcare.android.app.model.AgencyListModel;
import kr.firstcare.android.app.model.AttAddressListModel;
import kr.firstcare.android.app.model.CommuteInfoListModel;
import kr.firstcare.android.app.model.BoardListModel;
import kr.firstcare.android.app.model.FaqListModel;
import kr.firstcare.android.app.model.NoticeListModel;
import kr.firstcare.android.app.model.ProgramListModel;
import kr.firstcare.android.app.model.SignUpModel;
import kr.firstcare.android.app.model.UserAgencyListModel;
import kr.firstcare.android.app.model.UserAttendanceListModel;
import kr.firstcare.android.app.model.UserModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * ClassName            APIService
 * Created by JSky on   2020-06-03
 *
 * Description          API Interface
 */

public interface APIService {


    /* test
    @GET("_appV1/IF103.php")
    Call<IF103> getIF103(@Query("token") String token,
                         @Query("page") int page);
    */

    /*ID 중복확인*/
    @GET("confirm_id")
    Call<ResponseBody> duplicateCheckUserId(@Query("user_id") String user_id);

    /*로그인*/
    @GET("login_insteacher")
    Call<ResponseBody> userLogin(@Query("vteacher_id") String vteacher_id,
                                 @Query("userpw") String userpw);

    /*사용자 정보 가져오기*/
    @GET("insteacher_user_info")
    Call<List<UserModel>> getUserInfo(@Query("vteacher_id") String vteacher_id);

    /*모든 제공기관 리스트 가져오기*/
    @GET("agency_list")
    Call<List<AgencyListModel>> getAgencyList();

    /*사용자와 매칭된 제공기관 불러오기*/
    @GET("my_asso")
    Call<List<UserAgencyListModel>> getUserAgencyList(@Query("idx") int idx);

    /*출퇴근 정보 가져오기*/
    @GET("insteacher_att_info")
    Call<List<CommuteInfoListModel>> getCommuteInfo(@Query("vteacher_idx") int vteacher_idx,
                                                    @Query("act_date") String act_date);

    /*프로그램 정보 가져오기*/
    @GET("program_list")
    Call<List<ProgramListModel>> getProgramList(@Query("vteacher_idx") int vteacher_idx,
                                                @Query("sub_agency_idx") int sub_agency_idx);

    /*휴대폰 인증 문자 발송(토큰발급)*/
    @GET("gabia_token")
    Call<ResponseBody> getPhoneAuthToken();

    /*휴대폰 인증 문자 발송(인증번호 문자 전송)*/
    @GET("phonenum_authorization")
    Call<ResponseBody> getPhoneAuthNumber(@Query("access_token") String access_token,
                                          @Query("hp") String hp);

    /*출근처리*/
    @FormUrlEncoded
    @POST("insert_att1_data")
    Call<ResponseBody> getToWork(@Field("vteacher_idx") int vteacher_idx,
                                 @Field("program_idx") int program_idx,
                                 @Field("subagency_idx") int subagency_idx,
                                 @Field("vteacher_timetable_idx") int vteacher_timetable_idx,
                                 @Field("act_date") String act_date,
                                 @Field("att_time") String att_time,
                                 @Field("att_latitude") String att_latitude,
                                 @Field("att_longitude") String att_longitude,
                                 @Field("att_addr") String att_addr,
                                 @Field("leave_time") String leave_time,
                                 @Field("leave_latitude") String leave_latitude,
                                 @Field("leave_longitude") String leave_longitude,
                                 @Field("leave_addr") String leave_addr,
                                 @Field("add_date") String add_date,
                                 @Field("add_ip") String add_ip,
                                 @Field("add_id") String add_id
    );

    /*퇴근처리*/
    @FormUrlEncoded
    @POST("insert_att2_data")
    Call<ResponseBody> finishWork(@Field("att_idx") int att_idx,
                                  @Field("leave_time") String leave_time,
                                  @Field("leave_latitude") String leave_latitude,
                                  @Field("leave_longitude") String leave_longitude,
                                  @Field("leave_addr") String leave_addr,
                                  @Field("edit_date") String edit_date,
                                  @Field("edit_ip") String edit_ip,
                                  @Field("edit_id") String edit_id

    );

    /*이용자 출결석 조회*/
    @GET("child_att_info")
    Call<List<UserAttendanceListModel>> getChildAttInfo(@Query("att_idx") int att_idx);

    /*이용자 출결석 데이터 입력*/
    @FormUrlEncoded
    @POST("insert_child_att1")
    Call<ResponseBody> insertChildAttendance(@Field("program_idx") int program_idx,
                                         @Field("vteacher_idx") int vteacher_idx,
                                         @Field("subagency_idx") int subagency_idx,
                                         @Field("vteacher_attendance_idx") int vteacher_attendance_idx,
                                         @Field("child_idx") int child_idx,
                                         @Field("act_date") String act_date,
                                         @Field("att_time") String att_time,
                                         @Field("att_abs_flag") String att_abs_flag,
                                         @Field("zipcode") String zipcode,
                                         @Field("addr") String addr,
                                         @Field("addr_detail") String addr_detail,
                                         @Field("add_date") String add_date,
                                         @Field("add_id") String add_id,
                                         @Field("add_ip") String add_ip
    );

    /*이용자 출결석 데이터 수정*/
    @FormUrlEncoded
    @POST("update_child_att")
    Call<ResponseBody> updateChildAttendance(@Field("act_date") String act_date,
                                             @Field("att_time") String att_time,
                                             @Field("att_abs_flag") String att_abs_flag,
                                             @Field("child_attendance_idx") int child_attendance_idx
    );

    /*이용자 출결석 데이터 수정(미등록 상태 변경)*/
    @FormUrlEncoded
    @POST("delete_child_att")
    Call<ResponseBody> deleteChildAttendance(@Field("child_attendance_idx") int child_attendance_idx);

    /*출퇴근 정보 가져오기(이전기록조회)*/
    @GET("insteacher_select_att_info")
    Call<List<CommuteInfoListModel>> getSelectCommuteInfo(@Query("vteacher_idx") int vteacher_idx,
                                                    @Query("act_date") String act_date);

    /*제공인력 출근 시 이용자 기록 조회*/
    @GET("select_att_addr")
    Call<List<AttAddressListModel>> getSelectAttAddr(@Query("subagency_idx") int subagency_idx,
                                                     @Query("program_idx") int program_idx);

    /*공지사항 게시판*/
    @GET("notice_list")
    Call<List<BoardListModel>> getNoticeList();

    /*공지사항 게시판 (**new**)*/
    @GET("notice_list")
    Call<List<NoticeListModel>> getNewNoticeList();

    /*질문 답변 게시판*/
    @GET("faq_list")
    Call<List<BoardListModel>> getFaqList();

    /*FAQ - 회원가입 게시글*/
    @GET("XXXXXXXX")
    Call<List<FaqListModel>> getFaqSignUpList();

    /*FAQ - 출퇴근 기록하기 게시글*/
    @GET("XXXXXXXX")
    Call<List<FaqListModel>> getFaqCommuteList();

    /*FAQ - 기타 게시글*/
    @GET("XXXXXXXX")
    Call<List<FaqListModel>> getFaqEtcList();

    /*회원가입 (제공인력 등록 및 기관매칭)*/
    @FormUrlEncoded
    @POST("vteacher_joinus")
    Call<ResponseBody> userSignUp(@Field("vteacher_id") String vteacher_id,
                                  @Field("userpw") String userpw,
                                  @Field("vteacher_name") String vteacher_name,
                                  @Field("hp") String hp,
                                  @Field("email") String email,
                                  @Field("email_flag") String email_flag,
                                  @Field("sms_flag") String sms_flag,
                                  @Field("sex_flag") String sex_flag,
                                  @Field("zipcode") String zipcode,
                                  @Field("addr") String addr,
                                  @Field("addr_detail") String addr_detail,
                                  @Field("photo") String photo,
                                  @Field("user_code") String user_code,
                                  @Field("withdraw_id") String withdraw_id,
                                  @Field("withdraw_ip") String withdraw_ip,
                                  @Field("withdraw_date") String withdraw_date,
                                  @Field("withdraw_memo") String withdraw_memo,
                                  @Field("add_date") String add_date,
                                  @Field("add_id") String add_id,
                                  @Field("add_ip") String add_ip,
                                  @Field("select_agency_idx[]") ArrayList<String> select_agency_idx
    );

    @FormUrlEncoded
    @POST("vteacher_change_pwd")
    Call<ResponseBody> changePassword(@Field("userpw") String userpw,
                                      @Field("idx") int idx,
                                      @Field("edit_ip") String edit_ip,
                                      @Field("vteacher_id") String vteacher_id
    );

    @FormUrlEncoded
    @POST("vteacher_change_myinfo")
    Call<ResponseBody> modifyUserInfo(@Field("idx") int idx,
                                      @Field("hp") String hp,
                                      @Field("email") String email,
                                      @Field("email_flag") String email_flag,
                                      @Field("zipcode") String zipcode,
                                      @Field("addr") String addr,
                                      @Field("addr_detail") String addr_detail,
                                      @Field("edit_date") String edit_date,
                                      @Field("edit_id") String edit_id,
                                      @Field("edit_ip") String edit_ip,
                                      @Field("select_agency_idx[]") ArrayList<String> select_agency_idx
    );


}