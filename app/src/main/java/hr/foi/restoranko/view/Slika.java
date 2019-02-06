package hr.foi.restoranko.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
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

import hr.foi.restoranko.model.Korisnik;

public class Slika {
    private Uri uriSlike;

    public Slika() {
    }

    public Slika(Uri uriSlike)
    {
        this.uriSlike = uriSlike;
    }

    public Uri getUriSlike()
    {
        return uriSlike;
    }

    public void setUriSlike(Uri uriSlike)
    {
        this.uriSlike = uriSlike;
    }

    public static void postaviSlikuUImageView(Slika slika , ImageView imageView, Context context)
    {
        Glide.with(context)
                .load(slika.getUriSlike())
                .asBitmap()
                .centerCrop()
                .into(imageView);
    }

    public static Slika dohvatiSlikuKorisnika(Korisnik korisnik)
    {
        final Slika slika=new Slika();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(korisnik.getPutanjaSlike());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                slika.setUriSlike(uri);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
        return slika;
    }

    public void dohvatiSlikuKorisnika(Korisnik korisnik, SuccessListener listener){
        final Slika slika=new Slika();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(korisnik.getPutanjaSlike());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                slika.setUriSlike(uri);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
        listener.addOnSuccessListener(slika);
    }

    public static void PohraniSlikuUbazu(Slika slika, final Context context, Activity activityContext)
    {
        FirebaseStorage storage;
        StorageReference storageReference;

        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        if(slika.getUriSlike()!=null){
            final ProgressDialog progressDialog=new ProgressDialog(activityContext);
            progressDialog.setTitle("Uploading... ");
            progressDialog.show();
            final String putanjaUPohrani="images/"+Korisnik.prijavljeniKorisnik.getKorisnickoIme();

            StorageReference reference=storageReference.child(putanjaUPohrani);
            reference.putFile(slika.getUriSlike())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("user").child(Korisnik.prijavljeniKorisnik.getuId()).child("putanjaSlike").setValue("gs://hr-foi-restoranko.appspot.com/"+putanjaUPohrani);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

    }
    public void dohvatiIPostaviSliku(String putanja, final ImageView view, final Context context){
        final Slika slika=new Slika();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(putanja);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                slika.setUriSlike(uri);
                Glide.with(context)
                        .load(slika.getUriSlike())
                        .asBitmap()
                        .fitCenter()
                        .into(view);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

    }
}
