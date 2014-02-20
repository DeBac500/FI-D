package at.XDDominik.fi_d.fiatd;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;

import at.XDDominik.fi_d.fiatd.Ziehung.MainZiehung;

/**
 * Created by dominik on 18.02.14.
 */
public class Tabs implements ActionBar.TabListener{
    private Activity main;
    private ActionBar ab;
    private int select = -1;
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
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if(tab.getPosition() != select)
            switch (tab.getPosition()){
                case 0:
                    select = 0;
                    Intent intent = new Intent(main, MainZiehung.class);
                    main.startActivity(intent);
                    break;
                case 1:
                    break;
                case 2:
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
