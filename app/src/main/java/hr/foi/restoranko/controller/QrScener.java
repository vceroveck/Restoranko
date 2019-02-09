package hr.foi.restoranko.controller;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission_group.CAMERA;

public class QrScener extends AppCompatActivity  implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    List<String> listSifri= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    // Dohvaca rezultat skeniranja i usporeduje ga sa podatkom iz baze
    public void potvrdiRezervaciju(final String result) {
        potvrdaRezervacije();
        final AlertDialog.Builder builder = new AlertDialog.Builder(QrScener.this);
        builder.setTitle("Rezultat skeniranja");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(QrScener.this);
            }
        });


        builder.setMessage(result);

        for (int i=0; i<listSifri.size(); i++){

            if (result.equals(listSifri.get(i))) {
                builder.setMessage("Uspješna potvrda rezervacije");
                azurirajRezervaciju(listSifri.get(i));
            }
        }



        AlertDialog alert = builder.create();
        alert.show();
    }

    public void handleResult(Result result) {
        final String scanResult = String.valueOf(result);
        this.potvrdiRezervaciju(scanResult);

    }
    //Azurira polje potvrdeno ako je rezervacija uspješno potvrdena
    private void azurirajRezervaciju(String sifra){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference baf=reference.child("user").child(Korisnik.prijavljeniKorisnik.getuId()).child("rezervacijeKorisnika").child(sifra).child("potvrdeno");
        baf.setValue("true");

    }
    //Dohvaća ID od rezervacije
    private void potvrdaRezervacije() {
        if (Korisnik.prijavljeniKorisnik != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user").child(Korisnik.prijavljeniKorisnik.getuId()).child("rezervacijeKorisnika");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                                listSifri.add(data.getKey());


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else{

        }
    }

    //Dopuštenje za kameru
    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    //Dijalog gdje se pita korisnika za dopuštenje da koristi kameru
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }


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

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }


}