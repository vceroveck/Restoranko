package hr.foi.graphicdisplayoftable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

import hr.foi.graphicdisplayoftable.PrikazStolovaManager;
import hr.foi.graphicdisplayoftable.OnOdabirStolaCompleteListener;

public class OdabirStolaActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odabir_stola);
        PrikazStolovaManager.getInstance().setDependencies(this, (ConstraintLayout) findViewById(R.id.cosnt_layout), new OnOdabirStolaCompleteListener() {
            @Override
            public void OnOdabirStolaCompleteListener(String odabraniStol) {
                Intent returnDataIntent = getIntent();
                returnDataIntent.putExtra("stolId", odabraniStol);
                setResult(Activity.RESULT_OK, returnDataIntent);
                finish();
            }
        });
    }

}
