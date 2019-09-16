package inggitsemut.adminapps2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

import inggitsemut.adminapps2.R;
import inggitsemut.adminapps2.adapter.SearchAdapter;
import inggitsemut.adminapps2.api.ConfigUtils;
import inggitsemut.adminapps2.api.Service;
import inggitsemut.adminapps2.model.Ticket;
import inggitsemut.adminapps2.model.TicketList;
import inggitsemut.adminapps2.storage.SharedPrefManager;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler, View.OnClickListener {

    // QR Code scanner using ZXing Lib
    private ZXingScannerView scannerView;

    // Custom Popup
    Dialog myDialog;

    // Bottom Sheet Layout
    LinearLayout linearLayout;
    BottomSheetBehavior bottomSheetBehavior;
    SearchView searchView;

    //Recycler view
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Ticket> tickets = new ArrayList<>();
    private RecyclerView.Adapter searchAdapter;
    private Service service;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        
        // init QR Code Scanner
        scannerView = new ZXingScannerView(this);
        scannerView = findViewById(R.id.zxscan);
        
        // init Custom Popup (dialog)
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custom_popup);
        
        // Request permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(Main2Activity.this);
                        scannerView.startCamera();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(Main2Activity.this, "You must accept this permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        // add bottom sheet layout
        linearLayout = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        // search oon bottom sheet layout
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchTickets(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchTickets(newText);
                return false;
            }
        });

        // recycler view
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.rv_data_user);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        fetchTickets(""); // without keyword
    }

    private void fetchTickets(String key) {
        service = ConfigUtils.getApiClient().create(Service.class);
        Call<TicketList> call = service.getTicket(key);

        call.enqueue(new Callback<TicketList>() {
            @Override
            public void onResponse(Call<TicketList> call, Response<TicketList> response) {
                progressBar.setVisibility(View.GONE);
                tickets = response.body().getTicketList();
                Log.i("INI APA YA", "onResponse: " + tickets.size());

                searchAdapter = new SearchAdapter(tickets, Main2Activity.this);
                recyclerView.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<TicketList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Main2Activity.this, "Error on: " + t.toString() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.setResultHandler(this);
        scannerView.stopCamera();

    }

    @Override
    protected void onStop() {
        super.onStop();
        scannerView.setResultHandler(this);
        scannerView.stopCamera();

    }

    @Override
    public void handleResult(Result rawResult) {
        // here we receive the result
        // Vibration when QR Detected
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        // Show Popup when QR Detected
        ImageView imgClose;
        Button btnAttend;
        TextView tvName, tvEmail, tvPhone;

        imgClose = myDialog.findViewById(R.id.x_button);
        btnAttend = myDialog.findViewById(R.id.btn_attend);
        tvName = myDialog.findViewById(R.id.tv_name);
        tvEmail = myDialog.findViewById(R.id.tv_email);
        tvPhone = myDialog.findViewById(R.id.tv_phone);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        tvName.setText(rawResult.getText());

        imgClose.setOnClickListener(v -> {
            myDialog.dismiss();
            scannerView.resumeCameraPreview(this);
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }

    @Override
    public void onClick(View view) {

    }

}
