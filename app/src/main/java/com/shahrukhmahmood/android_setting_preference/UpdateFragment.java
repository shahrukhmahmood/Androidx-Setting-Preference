
package com.shahrukhmahmood.android_setting_preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.DialogPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

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

    SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.update_account_setting_preference);
        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();

        sharedPreferences = this.getActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        try{
            final EditTextPreference editTextFirstName = findPreference("firstName");
            final EditTextPreference editTextLastName = findPreference("lastName");
            final EditTextPreference editTextEmail = findPreference("email");
            final EditTextPreference editTextPhoneNumber = findPreference("phoneNumber");
            final  Preference preferencePassword = findPreference("password");

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

            preferencePassword.setIconSpaceReserved(false);


            editTextFirstName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    String firstNameEditValue = newValue.toString();
                    boolean result = name_update(firstNameEditValue, "");
                    if (result)
                    {
                        editTextFirstName.setSummary(firstNameEditValue);
                        editTextFirstName.setText(firstNameEditValue);
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            });


            editTextLastName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    String lastNameEditValue = newValue.toString();
                    boolean result = name_update("", lastNameEditValue);
                    if(result)
                    {
                        editTextLastName.setSummary(lastNameEditValue);
                        editTextLastName.setText(lastNameEditValue);
                        return true;
                    }
                    else{
                        return false;
                    }
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


            preferencePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(getActivity(), Verifypassword.class);
                    UpdateFragment.this.startActivity(i);

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

    private boolean flag = true;
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

}
