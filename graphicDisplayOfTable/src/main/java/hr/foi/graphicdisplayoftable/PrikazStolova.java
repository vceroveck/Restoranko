package hr.foi.graphicdisplayoftable;

import android.support.v4.app.Fragment;

public interface PrikazStolova
{
    Fragment getFragment();
    String getName();
    void setOnCompleteListener(OnOdabirStolaCompleteListener onCompleteListener);
}
