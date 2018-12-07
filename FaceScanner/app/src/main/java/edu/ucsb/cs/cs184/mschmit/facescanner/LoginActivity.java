package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button button;
    Button registerButton;
    EditText email;
    EditText pw;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button=findViewById(R.id.login_button);
        email=findViewById(R.id.login_email);
        pw=findViewById(R.id.login_pw);
        registerButton=findViewById(R.id.login_register_button);
        context=this;
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(email.getContext(), RegisterActivity.class);
                intent.putExtra("user", email.getText().toString());
                intent.putExtra("pw", pw.getText().toString());
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(context);
                if(email.getText().toString().equals(""))return;
                if(pw.getText().toString().equals(""))return;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://csquids-cs184-final-project.herokuapp.com/api/v1/orgLogin",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                                Log.wtf(response,response);
                                try {
                                    JSONObject jsonObj=new JSONObject(response);
                                    Intent intent;
                                    intent = new Intent(context, EventActivity.class);
                                    intent.putExtra("id", String.valueOf(jsonObj.getInt("id")));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                                Log.wtf(error.toString(),error.toString());
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
//                        Log.wtf(email.getText().toString(),pw.getText().toString());
                        params.put("email",email.getText().toString());
                        params.put("password", pw.getText().toString());
                        return params;
                    }

                };
                queue.add(stringRequest);
            }
//                JSONObject json = new JSONObject();
//                try {
//                    json.put("user",email.getText().toString());
//                    json.put("pw",pw.getText().toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                RequestQueue queue = Volley.newRequestQueue(context);
//                String url = "http://54.212.61.66/login";
//                JsonObjectRequest postRequest = new JsonObjectRequest(url, json,
//                        new Response.Listener<JSONObject>()
//                        {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                // response
//                                Log.wtf("Respdafsonse", response.toString());
//                                try {
//                                    if (response.getBoolean("logged_in")) {
//                                        String token=response.getString("token");
//                                        Intent intent;
//                                        intent = new Intent(email.getContext(), EventActivity.class);
//                                        intent.putExtra("token", token);
//                                        startActivity(intent);
//                                    }else{
//                                        String token="token";
//                                        Intent intent;
//                                        intent = new Intent(email.getContext(), EventActivity.class);
//                                        startActivity(intent);
//                                        Toast.makeText(context,"You entered the wrong username or password, but for debug purposes we'll pretend you got it right.",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }catch(JSONException e){
//                                    Toast.makeText(context,"Server error this should never happen",
//                                            Toast.LENGTH_SHORT).show();
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener()
//                        {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                String token="token";
//                                Intent intent;
//                                intent = new Intent(email.getContext(), EventActivity.class);
//                                intent.putExtra("token", token);
//                                startActivity(intent);
//                                Toast.makeText(context,"Failed to connect to the server (server is probably down), but for debug purposes we'll pretend everything went right.",
//                                        Toast.LENGTH_SHORT).show();
//                                error.printStackTrace();
//                            }
//                        }
//                );
//                postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        500,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                queue.add(postRequest);
//            }
        });
    }
}
