package com.example.fm.bagscanner2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by FM on 12/12/2015.
 */
public class ViewItemFragment extends Fragment {
    private EditText idET;
    private EditText dateET;
    private EditText itemET;

    private Button updateB;
    private Button deleteB;

    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_item_fragment, container, false);
        idET = (EditText) v.findViewById(R.id.idET);
        dateET = (EditText) v.findViewById(R.id.dateET);
        itemET = (EditText) v.findViewById(R.id.itemET);

        updateB = (Button) v.findViewById(R.id.updateB);
        updateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem();
            }
        });
        deleteB = (Button) v.findViewById(R.id.deleteB);
        deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteItem();
            }
        });

        Bundle bundle = this.getArguments();
        id = bundle.getString("ID");
        idET.setText(id);

        getItem();

        return v;
    }

    private void getItem(){
        class GetItem extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showItem(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_EMP,id);
                return s;
            }
        }
        GetItem gi = new GetItem();
        gi.execute();
    }

    private void showItem(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String date = c.getString(Config.TAG_DATE);
            String item = c.getString(Config.TAG_ITEM);

            dateET.setText(date);
            itemET.setText(item);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateItem(){
        final String date = dateET.getText().toString().trim();
        final String item = itemET.getText().toString().trim();

        class UpdateItem extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_BAG_ID,id);
                hashMap.put(Config.KEY_BAG_DATE,date);
                hashMap.put(Config.KEY_BAG_ITEM,item);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        UpdateItem ui = new UpdateItem();
        ui.execute();
    }

    private void deleteItem(){
        class DeleteItem extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_DELETE_EMP, id);
                return s;
            }
        }

        DeleteItem di = new DeleteItem();
        di.execute();
    }

    private void confirmDeleteItem(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure you want to delete this item?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteItem();
                        FragmentManager fm = getFragmentManager();
                        AllItemFragment allItemFragment = new AllItemFragment();
                        fm.beginTransaction()
                                .replace(R.id.fl1, allItemFragment)
                                .commit();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
