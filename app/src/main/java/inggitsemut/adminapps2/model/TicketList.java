package inggitsemut.adminapps2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;



//    @Expose
//    @SerializedName("Data")
//    private List<Ticket> data;
//    @Expose
//    @SerializedName("Message")
//    private String message;
//    @Expose
//    @SerializedName("Error")
//    private boolean error;
//    @Expose
//    @SerializedName("StatusCode")
//    private int statuscode;
//
//
//    public List<Ticket> getData() {
//        return data;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//
//    public boolean getError() {
//        return error;
//    }
//
//
//    public int getStatuscode() {
//        return statuscode;
//    }

public class TicketList {
    private ArrayList<Ticket> Data;

    public TicketList() {
    }

    public ArrayList<Ticket> getTicketList() {
        return Data;
    }

    public void setTicketList(ArrayList<Ticket> Data) {
        this.Data = Data;
    }

}
