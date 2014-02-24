package at.XDDominik.fi_d.fiatd;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;

import at.XDDominik.fi_d.fiatd.ClientServerZiehung.MainClientServerZiehung;
import at.XDDominik.fi_d.fiatd.Ziehung.MainZiehung;
import at.XDDominik.fi_d.fiatd.ZiehungBearb.MainZiehungBearb;

/**
 * Created by dominik on 18.02.14.
 */
public class Tabs implements ActionBar.TabListener{
    private Activity main;
    private ActionBar ab;
    private int select = -1;
    private boolean active = false;
    public Tabs(Activity a){
        main=a;
        ab = main.getActionBar();
    }
    public void initTab(int i){
        ab.removeAllTabs();
        select = i;
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.addTab(ab.newTab().setText("PROBEN ZIEHEN").setTabListener(this));
        ab.addTab(ab.newTab().setText("ZIEHUNG ERSTELLEN/BEARBEITEN").setTabListener(this));
        ab.addTab(ab.newTab().setText("CLIENT/SERVER ZIEHUNGEN").setTabListener(this));
        ab.selectTab(ab.getTabAt(i));
        this.active=true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Intent intent;
        if(this.active)
            if(tab.getPosition() != select)
                switch (tab.getPosition()){
                    case 0:
                        select = 0;
                        intent = new Intent(main, MainZiehung.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        main.startActivity(intent);
                        main.finish();
                        main.overridePendingTransition(0,0);
                        break;
                    case 1:
                        select = 1;
                        intent = new Intent(main, MainZiehungBearb.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        main.startActivity(intent);
                        main.finish();
                        main.overridePendingTransition(0,0);
                        break;
                    case 2:
                        select = 2;
                        intent = new Intent(main, MainClientServerZiehung.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        main.startActivity(intent);
                        main.finish();
                        main.overridePendingTransition(0,0);
                        break;
                }
    }
    public void unselect(){
        try{
            ab.selectTab(null);
        }catch(NullPointerException e){}
        select = -1;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
