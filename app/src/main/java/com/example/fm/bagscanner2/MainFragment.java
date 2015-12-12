package com.example.fm.bagscanner2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by FM on 12/12/2015.
 */
public class MainFragment extends Fragment {
    AddItemListener addItemCallback;
    ViewItemListener viewItemCallback;

    public interface AddItemListener {
        public void addItem();
    }

    public interface ViewItemListener {
        public void viewItem();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addItemCallback = (AddItemListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " The MainActivity activity must " +
                    "implement OnContactSelectedListener");
        }

        try {
            viewItemCallback = (ViewItemListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " The MainActivity activity must " +
                    "implement OnContactSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        Button b = (Button) v.findViewById(R.id.makeSchedule);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemCallback.addItem();
            }
        });

        Button b2 = (Button) v.findViewById(R.id.viewB);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewItemCallback.viewItem();
            }
        });
        return v;
    }
}
