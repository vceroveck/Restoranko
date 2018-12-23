package hr.foi.restoranko.controller;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.autofill.AutofillValue;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import hr.foi.restoranko.R;
import hr.foi.restoranko.model.Korisnik;

public class KorisnickiProfil extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private Button forgotPassword;
    private Button promijeniSlikuProfila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnicki_profil);

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
                PostaviSliku();
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

            StorageReference reference=storageReference.child("images/"+UUID.randomUUID().toString());
            reference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(KorisnickiProfil.this, "Uploaded", Toast.LENGTH_SHORT).show();                ;
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
        TextView Ime = (TextView) findViewById(R.id.outputKorisnikIme);
        TextView Prezime = (TextView) findViewById(R.id.outputKorisnikPrezime);
        ImageView slikaProfila = (ImageView) findViewById(R.id.imgKorisnik);

        Ime.setText(Korisnik.prijavljeniKorisnik.getIme());
        Prezime.setText(Korisnik.prijavljeniKorisnik.getPrezime());
        //Picasso.get().load(Korisnik.prijavljeniKorisnik.getSlika()).into(slikaProfila);


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
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
