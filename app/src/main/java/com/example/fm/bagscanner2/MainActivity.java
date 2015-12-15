package com.example.fm.bagscanner2;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MainFragment.AddItemListener, MainFragment.ViewItemListener,
        AllItemFragment.AddItemListener2, AllItemFragment.ViewItemListener2{
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();

        MainFragment f1 = new MainFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fl1, f1)
                .commit();
    }

    @Override
    public void addItem() {
        AddItemFragment addItemFragment = new AddItemFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fl1, addItemFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewItem() {
        AllItemFragment allItemFragment = new AllItemFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fl1, allItemFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addItem2() {
        AddItemFragment addItemFragment = new AddItemFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fl1, addItemFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewItem2(String bagId) {
        ViewItemFragment viewItemFragment = new ViewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ID", bagId);
        viewItemFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fl1, viewItemFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed(){
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fragmentManager.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
}
