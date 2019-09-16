package inggitsemut.adminapps2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import inggitsemut.adminapps2.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Main2Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler, View.OnClickListener {

    // QR Code scanner using ZXing Lib
    private ZXingScannerView scannerView;
    private TextView txtResult;

    // Custom Popup
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init Custom Popup (dialog)
        myDialog = new Dialog(this);


        setContentView(R.layout.activity_main2);

        // init QR Code Scanner
        scannerView = new ZXingScannerView(this);
        scannerView = findViewById(R.id.zxscan);
        txtResult = findViewById(R.id.txt_result);

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
        txtResult.setText(rawResult.getText());

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
