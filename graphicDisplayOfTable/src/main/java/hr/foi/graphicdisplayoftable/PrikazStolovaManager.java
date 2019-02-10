package hr.foi.graphicdisplayoftable;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class PrikazStolovaManager
{
    private static PrikazStolovaManager instance;
    private Map<String, PrikazStolova> prikazStolovaList = new HashMap<>();

    private AppCompatActivity activity;


    private PrikazStolovaManager()
    {
        PrikazStolovaSlikaFragment prikazStolovaSlikaFragment = new PrikazStolovaSlikaFragment();
        PrikazStolovaListaFragment prikazStolovaListaFragment=new PrikazStolovaListaFragment();

        prikazStolovaList.put(prikazStolovaSlikaFragment.getName(), prikazStolovaSlikaFragment);
        prikazStolovaList.put(prikazStolovaListaFragment.getName(), prikazStolovaListaFragment);
    }

    public void setDependencies(AppCompatActivity activity, ConstraintLayout layout, OnOdabirStolaCompleteListener onOdabirStolaCompleteListener)
    {
        this.activity = activity;
        createDrawer(layout, onOdabirStolaCompleteListener);

    }

    public static PrikazStolovaManager getInstance()
    {
        if(instance == null)
        {
            instance = new PrikazStolovaManager();
        }
        return instance;
    }

    public void createDrawer(ConstraintLayout layout, OnOdabirStolaCompleteListener onOdabirStolaCompleteListener)
    {
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        int btnId=0;

        for (String key : prikazStolovaList.keySet())
        {
            PrikazStolova prikazStolova = prikazStolovaList.get(key);
            prikazStolova.setOnCompleteListener(onOdabirStolaCompleteListener);
            final Button button = new Button(activity);
            button.setText(prikazStolova.getName());
            button.setId(View.generateViewId());

            btnId=button.getId();

            layout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    selectPrikazStolaItem(button.getText().toString());
                }
            });

        }
        set.connect(btnId-1, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        set.connect(btnId-1, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        set.connect(btnId-1, ConstraintSet.RIGHT, btnId, ConstraintSet.LEFT, 0);
        set.constrainWidth(btnId-1,  500);

        set.connect(btnId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        set.connect(btnId, ConstraintSet.LEFT, btnId-1, ConstraintSet.RIGHT, 0);
        set.connect(btnId, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.constrainWidth(btnId, 500);
        set.applyTo(layout);
    }

    private void startModule(PrikazStolova module)
    {
        FragmentManager mFragmentManager = activity.getSupportFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, module.getFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void selectPrikazStolaItem(String kljuc)
    {
        if(!prikazStolovaList.isEmpty())
        {
            PrikazStolova prikazStolova = prikazStolovaList.get(kljuc);
            startModule(prikazStolova);
        }
    }
}
