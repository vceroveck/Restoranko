package hr.foi.restoranko.controller;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
                .centerCrop()
                .into(imageView);
    }

    public static Slika dohvatiSlikuKorisnika(Korisnik korisnik)
    {
        final Slika slika=new Slika();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(Korisnik.prijavljeniKorisnik.getPutanjaSlike());
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
}
