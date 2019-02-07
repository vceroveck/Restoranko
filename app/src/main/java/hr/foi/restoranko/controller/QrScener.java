package hr.foi.restoranko.controller;



import java.util.ArrayList;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import hr.foi.restoranko.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission_group.CAMERA;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class QrScener extends AppCompatActivity  implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private String sifra;

    List<String> ListaSifri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DohvacanjeSifreRezervacije();
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qr_scener);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), "Permission is granted!", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }
    }

    public void validirajNarudzbuDostave(final String result) {



        final AlertDialog.Builder builder = new AlertDialog.Builder(QrScener.this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(QrScener.this);
            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                startActivity(intent);
            }
        });

        builder.setMessage(result);
        for(int i=0; i<ListaSifri.size(); i++){


            if (result.equals(ListaSifri.get(i))) {
                builder.setMessage("Rezervacija potvrdena");
            } /*else {
                builder.setMessage("Greska sa QR kodom");
            }*/
        }



        AlertDialog alert = builder.create();
        alert.show();
    }

    public void handleResult(Result result) {
        final String scanResult = String.valueOf(result);
        this.validirajNarudzbuDostave(scanResult);

    }


    public void DohvacanjeSifreRezervacije(){

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("rezervacija").child("1_stol1");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    ListaSifri.add(data.getKey());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    /*
     * Asks phone/tablet user to allow camera scanning.
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }

    /*
     * Starts camera scanner if it is allowed.
     */
    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResults[]) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "Permission granted!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission is denied!", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMessage("You need to allow access for both permissions!",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(getApplicationContext());
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(getApplicationContext()).setMessage(message).setPositiveButton("OK", listener).setNegativeButton("Cancel", null).create().show();
    }


    private String dohvatiID (){
        String ba="";

        return  ba;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }


}