package inggitsemut.adminapps2.model;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("status_code")
    private Integer status_code;

    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Admin admin;

    @SerializedName("token")
    private String token;

    public Result(Integer status_code, Boolean error, String message, Admin admin, String token) {
        this.status_code = status_code;
        this.error = error;
        this.message = message;
        this.admin = admin;
        this.token = token;
    }

    public Integer getStatus_code() {
        return status_code;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Admin getAdmin() {
        return admin;
    }

    public String getToken() {
        return token;
    }
}
