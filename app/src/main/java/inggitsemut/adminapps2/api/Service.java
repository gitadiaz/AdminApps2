package inggitsemut.adminapps2.api;

import java.util.List;

import inggitsemut.adminapps2.model.Result;
import inggitsemut.adminapps2.model.TicketList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    // login
    @FormUrlEncoded
    @POST("user/public/auth/login")
    Call<Result> loginAdmin(
            @Field("user_email") String email,
            @Field("user_password") String password
    );

    // search ticket by email or phone
    @GET ("product/public/ticket")
    Call <TicketList> getTicket (
            @Query("key") String keyword);

}
