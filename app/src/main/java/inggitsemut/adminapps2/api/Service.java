package inggitsemut.adminapps2.api;

import inggitsemut.adminapps2.model.Result;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Service {

    // login
    @FormUrlEncoded
    @POST("auth/login")
    Call<Result> loginAdmin(
            @Field("user_email") String email,
            @Field("user_password") String password
    );

}
