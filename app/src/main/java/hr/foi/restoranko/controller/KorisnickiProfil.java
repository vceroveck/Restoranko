package hr.foi.restoranko.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;

public class KorisnickiProfil extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private Button forgotPassword;
    private Button promijeniSlikuProfila;
    ImageView slikaProfila;

    boolean isImageFitToScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnicki_profil);

        slikaProfila = (ImageView) findViewById(R.id.imgKorisnik);

        UcitajKorisnickePodatke();

        forgotPassword = (Button)findViewById(R.id.btnPromijenitiLozinku);
        promijeniSlikuProfila = (Button) findViewById(R.id.btnPromijenitiSlikuProfila);


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KorisnickiProfil.this, Password.class));
            }
        });

        promijeniSlikuProfila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OdaberiSliku();
            }
        });
    }

    private void PostaviSliku() {
        FirebaseStorage storage;
        StorageReference storageReference;

        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        if(filePath!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final String putanjaUPohrani="images/"+Korisnik.prijavljeniKorisnik.getKorisnickoIme();

            StorageReference reference=storageReference.child(putanjaUPohrani);
            reference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(KorisnickiProfil.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("user").child(Korisnik.prijavljeniKorisnik.getuId()).child("slika").setValue("gs://hr-foi-restoranko.appspot.com/"+putanjaUPohrani);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(KorisnickiProfil.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+(int)progress+"%");
                        }
                    });
        }
    }

    private void  UcitajKorisnickePodatke(){



        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(Korisnik.prijavljeniKorisnik.getSlika());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getBaseContext())
                        .load(uri)
                        .centerCrop()
                        .into(slikaProfila);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

        TextView Ime = (TextView) findViewById(R.id.outputKorisnikIme);
        TextView Prezime = (TextView) findViewById(R.id.outputKorisnikPrezime);
        Ime.setText(Korisnik.prijavljeniKorisnik.getIme());
        Prezime.setText(Korisnik.prijavljeniKorisnik.getPrezime());



    }


    private Uri filePath;
    private ImageView imageView;

    private void OdaberiSliku(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,  "Odaberi sliku"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode ==RESULT_OK && data!=null && data.getData()!=null){
            filePath = data.getData();
            try{
                imageView = (ImageView) findViewById(R.id.imgKorisnik);
                imageView.setImageURI(null);
                imageView.setImageURI(filePath);
                Log.i("URISET", filePath.toString());
                imageView.refreshDrawableState();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            PostaviSliku();
        }
    }
}
