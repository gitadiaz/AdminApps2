package inggitsemut.adminapps2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;
import inggitsemut.adminapps2.R;
import inggitsemut.adminapps2.adapter.SearchAdapter;
import inggitsemut.adminapps2.api.ConfigUtils;
import inggitsemut.adminapps2.api.Service;
import inggitsemut.adminapps2.model.Ticket;
import inggitsemut.adminapps2.model.TicketList;
import inggitsemut.adminapps2.storage.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MyActivity kenapa nih";

    private TextView tvResult;
    private SurfaceView surfaceView;
    private QREader qrEader;
    private Dialog myDialog;

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
        setContentView(R.layout.activity_main);

        // Request permission camera
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        if (qrEader != null){
//                            qrEader.releaseAndCleanup();
//                        }
                        setupCamera();
//                        fetchTickets("");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You must enable this permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        // add bottom sheet
        linearLayout = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        searchView = findViewById(R.id.search_view);

        // recycler view
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.rv_data_user);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        fetchTickets(""); // without keyword
    }

    private void setupCamera() {
//        tvResult = findViewById(R.id.code_info);
        surfaceView = findViewById(R.id.camera_view);
        setupQREader();

    }

    private void setupQREader() {
        qrEader = new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                tvResult.post(new Runnable() {
                    @Override
                    public void run() {
                        // Vibration when QR detected
                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);

                        // show dialog / popup
                        myDialog = new Dialog(MainActivity.this);

                        ImageView imgClose;
                        Button btnAttend;
                        TextView tvName, tvEmail, tvPhone;
                        myDialog.setContentView(R.layout.custom_popup);

                        imgClose = myDialog.findViewById(R.id.x_button);
                        btnAttend = myDialog.findViewById(R.id.btn_attend);
                        tvName = myDialog.findViewById(R.id.tv_name);
                        tvEmail = myDialog.findViewById(R.id.tv_email);
                        tvPhone = myDialog.findViewById(R.id.tv_phone);

                        qrEader.stop();

                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        myDialog.show();

                        tvName.setText(data);

                        imgClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.dismiss();
                                qrEader.start();
                            }
                        });

//                        tvResult.setText(data);
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(surfaceView.getHeight())
                .width(surfaceView.getWidth())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (qrEader != null){
                            qrEader.initAndStart(surfaceView);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You must enable this permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        setupCamera();
//                        fetchTickets("");

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You must enable this permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else{
            setupCamera();
//            fetchTickets("");
        }

    }

    public void fetchTickets(String key){

        service = ConfigUtils.getApiClient().create(Service.class);
        Call<TicketList> call = service.getTicket(key);

        call.enqueue(new Callback<TicketList>() {
            @Override
            public void onResponse(Call<TicketList> call, Response<TicketList> response) {
                progressBar.setVisibility(View.GONE);
                tickets = response.body().getTicketList();
                Log.i(TAG, "onResponse: " + tickets.size());

                searchAdapter = new SearchAdapter(tickets, MainActivity.this);
                recyclerView.setAdapter(searchAdapter);

//                searchAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<TicketList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error on: " + t.toString() , Toast.LENGTH_SHORT).show();
            }
        });
    }

}
