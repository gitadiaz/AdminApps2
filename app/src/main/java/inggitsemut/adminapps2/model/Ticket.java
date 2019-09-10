package inggitsemut.adminapps2.model;


public class Ticket {

    private int id;
    private String ticket_number;
    private String event_code;
    private String member_name;
    private String member_email;
    private String member_phone_number;
    private String status;

    public Ticket(int id, String ticket_number, String event_code, String member_name, String member_email, String member_phone_number, String status) {
        this.id = id;
        this.ticket_number = ticket_number;
        this.event_code = event_code;
        this.member_name = member_name;
        this.member_email = member_email;
        this.member_phone_number = member_phone_number;
        this.status = status;
    }

    public Ticket(String member_name, String member_email, String member_phone_number) {
        this.member_name = member_name;
        this.member_email = member_email;
        this.member_phone_number = member_phone_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicket_number() {
        return ticket_number;
    }

    public void setTicket_number(String ticket_number) {
        this.ticket_number = ticket_number;
    }

    public String getEvent_code() {
        return event_code;
    }

    public void setEvent_code(String event_code) {
        this.event_code = event_code;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    public String getMember_phone_number() {
        return member_phone_number;
    }

    public void setMember_phone_number(String member_phone_number) {
        this.member_phone_number = member_phone_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
