package inggitsemut.adminapps2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {


    @Expose
    @SerializedName("Token")
    private String token;
    @Expose
    @SerializedName("Data")
    private Admin data;
    @Expose
    @SerializedName("Message")
    private String message;
    @Expose
    @SerializedName("Error")
    private boolean error;
    @Expose
    @SerializedName("StatusCode")
    private int statuscode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Admin getData() {
        return data;
    }

    public void setData(Admin data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }


}
