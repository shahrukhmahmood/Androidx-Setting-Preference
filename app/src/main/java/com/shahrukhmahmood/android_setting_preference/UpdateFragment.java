
package com.shahrukhmahmood.android_setting_preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

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

public class UpdateFragment extends PreferenceFragmentCompat {

//    private String firstNameEditValue;
//    private String lastNameEditValue;
    SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.update_account_setting_preference);

        sharedPreferences = this.getActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        try{
            final EditTextPreference editTextFirstName = findPreference("firstName");
            final EditTextPreference editTextLastName = findPreference("lastName");
            final EditTextPreference editTextEmail = findPreference("email");
            final EditTextPreference editTextPhoneNumber = findPreference("phoneNumber");
            final EditTextPreference editTextPassword = findPreference("password");

            editTextFirstName.setSummary(sharedPreferences.getString("first_name", ""));
            editTextFirstName.setText(sharedPreferences.getString("first_name", ""));
            editTextFirstName.setIconSpaceReserved(false);

            editTextLastName.setSummary(sharedPreferences.getString("last_name", ""));
            editTextLastName.setText(sharedPreferences.getString("last_name", ""));
            editTextLastName.setIconSpaceReserved(false);

            editTextEmail.setSummary(sharedPreferences.getString("email", ""));
            editTextEmail.setText(sharedPreferences.getString("email", ""));
            editTextEmail.setIconSpaceReserved(false);

            editTextPhoneNumber.setSummary(sharedPreferences.getString("phone_number", ""));
            editTextPhoneNumber.setText(sharedPreferences.getString("phone_number", ""));
            editTextPhoneNumber.setIconSpaceReserved(false);

            editTextPassword.setSummary("********");
            editTextPassword.setIconSpaceReserved(false);


            editTextFirstName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    String firstNameEditValue = newValue.toString();
                    if (name_update(firstNameEditValue, ""))
                    {
                        editTextFirstName.setSummary(firstNameEditValue);
                        return true;
                    }
                    return false;
                }
            });


            editTextLastName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    String lastNameEditValue = newValue.toString();
                    if(name_update("", lastNameEditValue))
                    {
                        editTextLastName.setSummary(lastNameEditValue);
                        return true;
                    }
                    return false;
                }
            });

            editTextPhoneNumber.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

//                    editTextPhoneNumber.setIntent(new Intent(getContext(), Login.class));
                    Intent i = new Intent(getContext(), Login.class);
                    startActivity(i);
                    return true;
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    boolean flag = false;
    public boolean name_update(final String firstNameEditValue, final String lastNameEditValue) {

        String url = "http://52.15.104.184/update/name/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("status").equals("200")) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("first_name", json.getString("first_name"));
                        editor.putString("last_name", json.getString("last_name"));
                        editor.apply();

//                        editTextFirstName.setText("");
//                        editTextLastName.setText("");

                        Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_LONG).show();
                        flag = true;

                    }
                    else if(json.getString("status").equals("404") || json.getString("status").equals("400")){
                        Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_LONG).show();
                        flag = false;
                    }

                }
                catch (JSONException e) {
                    flag = false;
                    e.printStackTrace();
                }
            }},
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    flag = false;
                }
            })

            {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    params.put("first_name", firstNameEditValue);
                    params.put("last_name", lastNameEditValue);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", sharedPreferences.getString("Token", ""));
                    return headers;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
            requestQueue.add(stringRequest);
            return flag;
    }

//    private SharedPreferences sharedPreferences;
//    private String firstName;
//    private String lastName;
//    private String phoneNumber;
//    private String email;
//    private String publicFirstName;
//    private String publicLastName;
//    private String publicphone;
//    private String publicemail;
//    public  String token1;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        getSupportActionBar().hide();
//
//        try{
//            token1 = Login.token;
//
//            addPreferencesFromResource(R.xml.AccountSettingPreference);
//            account_details();
//
//            final PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("name_cat");
//            PreferenceScreen preferenceScreen = this.getPreferenceScreen();
//
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        /*setPreferencesFromResource(R.xml.AccountSettingPreference, rootKey);*/
//    }
//
//    public void account_details()
//    {
//        String url = "http://52.15.104.184/my_details/";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getString("status").equals("200")) {
//
//                                publicFirstName = jsonObject.getString("first_name");
//                                publicLastName = jsonObject.getString("last_name");
//                                publicphone = jsonObject.getString("phone_number");
//                                publicemail = jsonObject.getString("email");
//
//                                publicFirstName= publicFirstName+" "+publicLastName;
//                                Preference mPref2 = findPreference("name");
//                                mPref2.setTitle(publicFirstName);
//                                mPref2.setTitle(publicemail);
//                                mPref2.setSummary(publicphone);
//                                Log.e("VOLLEY", publicFirstName);
//
//                                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
//                            }
//                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
//                    }
//                }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<>();
//
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", token1);
//                return headers;
//            }
//
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//
//
//
//
//
//
//        //  PreferenceCategory preferenceCategory = new PreferenceCategory(preferenceScreen.getContext());
//        // preferenceCategory.setTitle("Wireless switches");
//        // Preference mPref2 = findPreference("name");
//        //mPref2.setTitle(publicphone);
//        //  preferenceScreen.addPreference(preferenceCategory);
//
//    }

}
