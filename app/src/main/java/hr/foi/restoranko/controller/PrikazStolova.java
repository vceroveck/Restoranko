package hr.foi.restoranko.controller;

import android.support.v4.app.Fragment;

import hr.foi.restoranko.view.OnOdabirStolaCompleteListener;

public interface PrikazStolova
{
    Fragment getFragment();
    String getName();
    void setOnCompleteListener(OnOdabirStolaCompleteListener onCompleteListener);
}
