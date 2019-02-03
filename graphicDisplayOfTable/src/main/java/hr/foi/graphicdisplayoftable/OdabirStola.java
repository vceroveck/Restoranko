package hr.foi.graphicdisplayoftable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class OdabirStola extends AppCompatActivity {
    public String restoran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        restoran=intent.getStringExtra("restoran");

        Stol stol;

    }


}
