package com.shahrukhmahmood.android_setting_preference;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class Login extends AppCompatActivity {

    EditText user;
    EditText passw;
    public static String token;
    Button loginn;

    public static final String AppPreferences = "AppPreferences";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.username);
        passw =  findViewById(R.id.password);
        loginn = findViewById(R.id.login);

        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userlogin();
            }
        });
    }

    public void userlogin() {

        // final

        final String a = user.getText().toString();
        final String  b = passw.getText().toString();
        String url = "http://52.15.104.184/login/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getString("status").equals("200")) {
                                token = json.getString("token");
                                //Bundle bundle = new Bundle();

                                //bundle.putString("message", token );
//                                    MyFragment myObj = new MyFragment();
                                //SettingsFragment fragInfo = new SettingsFragment();
                                //fragInfo.setArguments(bundle);
                                Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();

                                Intent i = new Intent(Login.this, SettingsActivity.class);
                                Login.this.startActivity(i);

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
                        Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })


        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email_or_phone",a);
                params.put("password",b);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
