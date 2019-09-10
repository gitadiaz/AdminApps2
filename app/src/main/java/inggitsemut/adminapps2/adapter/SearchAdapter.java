package inggitsemut.adminapps2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import inggitsemut.adminapps2.R;
import inggitsemut.adminapps2.model.Ticket;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<Ticket> tickets;
    private Context context;

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public SearchAdapter(List<Ticket> tickets, Context context) {
        this.tickets = tickets;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.name.setText(tickets.get(position).getMember_name());
        holder.email.setText(tickets.get(position).getMember_email());
        holder.phoneNumber.setText(tickets.get(position).getMember_phone_number());

    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name, email, phoneNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_list);
            email = itemView.findViewById(R.id.tv_email_list);
            phoneNumber = itemView.findViewById(R.id.tv_phone_list);

        }
    }
}
