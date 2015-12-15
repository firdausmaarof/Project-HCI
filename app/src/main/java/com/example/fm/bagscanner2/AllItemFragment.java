package com.example.fm.bagscanner2;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by FM on 16/12/2015.
 */
public class AllItemFragment extends Fragment {
    AddItemListener2 addItemCallback2;
    ViewItemListener2 viewItemCallback2;
    private ListView listView;
    private String JSON_STRING;

    public interface AddItemListener2 {
        public void addItem2();
    }

    public interface ViewItemListener2 {
        public void viewItem2(String id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addItemCallback2 = (AddItemListener2) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " The MainActivity activity must " +
                    "implement AddItemListener");
        }
        try {
            viewItemCallback2 = (ViewItemListener2) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " The MainActivity activity must " +
                    "implement ViewItemListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_item, container, false);
        FloatingActionButton floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemCallback2.addItem2();
            }
        });
        listView = (ListView) v.findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
                String bagId = map.get(Config.TAG_ID).toString();
                viewItemCallback2.viewItem2(bagId);
            }
        });
        getJSON();
        return v;
    }

    private void showItem(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String date = jo.getString(Config.TAG_DATE);
                String item = jo.getString(Config.TAG_ITEM);

                HashMap<String,String> items = new HashMap<>();
                items.put(Config.TAG_ID, id);
                items.put(Config.TAG_DATE, date);
                items.put(Config.TAG_ITEM, item);
                list.add(items);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                getActivity(), list, R.layout.item_layout,
                new String[]{Config.TAG_DATE,Config.TAG_ITEM},
                new int[]{R.id.dateTV, R.id.itemTV});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showItem();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
