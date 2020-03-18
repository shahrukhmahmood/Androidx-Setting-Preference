
package com.shahrukhmahmood.android_setting_preference;

import android.content.Context;
import android.content.Intent;
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
import java.util.Objects;

import static com.shahrukhmahmood.android_setting_preference.Login.AppPreferences;

import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceCategory;

public class SettingsFragment extends PreferenceFragmentCompat {

    private String name;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
            addPreferencesFromResource(R.xml.account_setting_preference);
            account_details();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        Preference pref = findPreference("name");

        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getActivity(), UpdateActivity.class);
                SettingsFragment.this.startActivity(i);

                return true;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        /*setPreferencesFromResource(R.xml.AccountSettingPreference, rootKey);*/
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

                                firstName = jsonObject.getString("first_name");
                                lastName = jsonObject.getString("last_name");
                                phoneNumber = jsonObject.getString("phone_number");
                                email = jsonObject.getString("email");

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("first_name", firstName);
                                editor.putString("last_name", lastName);
                                editor.putString("phone_number", phoneNumber);
                                editor.putString("email", email);
                                editor.apply();

                                name = firstName+" "+lastName;
                                Preference mPref2 = findPreference("name");
                                mPref2.setTitle(name);
                                mPref2.setSummary(phoneNumber + "\n" + email);

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
                headers.put("Authorization", sharedPreferences.getString("Token", ""));
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

}
