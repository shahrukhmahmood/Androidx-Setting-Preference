
package com.shahrukhmahmood.android_setting_preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceCategory;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPreferences;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String publicFirstName;
    private String publicLastName;
    private String publicphone;
    private String publicemail;
    public  String token1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();

        try{
            token1 = Login.token;

            addPreferencesFromResource(R.xml.preferences);
            account_details();

            final PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("name_cat");
            PreferenceScreen preferenceScreen = this.getPreferenceScreen();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        /*setPreferencesFromResource(R.xml.preferences, rootKey);*/
    }

    public void account_details()
    {
        String url = "http://52.15.104.184/my_details/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {

                                publicFirstName = jsonObject.getString("first_name");
                                publicLastName = jsonObject.getString("last_name");
                                publicphone = jsonObject.getString("phone_number");
                                publicemail = jsonObject.getString("email");

                                publicFirstName= publicFirstName+" "+publicLastName;
                                Preference mPref2 = findPreference("name");
                                mPref2.setTitle(publicFirstName);
                                mPref2.setTitle(publicemail);
                                mPref2.setSummary(publicphone);
                                Log.e("VOLLEY", publicFirstName);

                                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token1);
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);






        //  PreferenceCategory preferenceCategory = new PreferenceCategory(preferenceScreen.getContext());
        // preferenceCategory.setTitle("Wireless switches");
        // Preference mPref2 = findPreference("name");
        //mPref2.setTitle(publicphone);
        //  preferenceScreen.addPreference(preferenceCategory);

    }

}
