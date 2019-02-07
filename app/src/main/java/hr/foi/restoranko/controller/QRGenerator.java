package hr.foi.restoranko.controller;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import hr.foi.restoranko.R;

public class QRGenerator extends AppCompatActivity {
    Button buttonQRCodeGenerate;
    ImageView imageViewGeneratedQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);
        buttonQRCodeGenerate = findViewById(R.id.buttonGenerateQRCode);
        imageViewGeneratedQRCode = findViewById(R.id.imageViewGeneratedQRCode);
    }

    public void generateCode(ImageView prikaz, String tekst) {
        if (tekst != null) {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(tekst, BarcodeFormat.QR_CODE, 500, 500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                prikaz.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickGenerateQRCode(View view) {
        generateCode(imageViewGeneratedQRCode,"barcica");
    }
}
