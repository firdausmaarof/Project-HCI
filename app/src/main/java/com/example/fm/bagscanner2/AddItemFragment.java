package com.example.fm.bagscanner2;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by FM on 12/12/2015.
 */
public class AddItemFragment extends Fragment {
    private EditText dateET;
    private EditText itemET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_item, container, false);
        dateET = (EditText) v.findViewById(R.id.dateET);
        itemET = (EditText) v.findViewById(R.id.itemET);
        Button b = (Button) v.findViewById(R.id.insertB);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
        return v;
    }

    //Adding an item
    private void addItem(){

        final String date = dateET.getText().toString().trim();
        final String item = itemET.getText().toString().trim();

        class AddTask extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_BAG_DATE,date);
                params.put(Config.KEY_BAG_ITEM,item);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
        }

        AddTask at = new AddTask();
        at.execute();
    }
}
