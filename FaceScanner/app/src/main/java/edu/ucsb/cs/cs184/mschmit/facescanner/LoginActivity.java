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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    Button button;
    EditText user;
    EditText pw;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button=findViewById(R.id.login_button);
        user=findViewById(R.id.login_user);
        pw=findViewById(R.id.login_pw);
        context=this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("user",user.getText().toString());
                    json.put("pw",pw.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://54.212.61.66/login";
                JsonObjectRequest postRequest = new JsonObjectRequest(url, json,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                // response
                                Log.wtf("Respdafsonse", response.toString());
                                try {
                                    if (response.getBoolean("logged_in")) {
                                        String token=response.getString("token");
                                        Intent intent;
                                        intent = new Intent(user.getContext(), EventActivity.class);
                                        intent.putExtra("token", token);
                                        startActivity(intent);
                                    }else{
                                        String token="token";
                                        Intent intent;
                                        intent = new Intent(user.getContext(), EventActivity.class);
                                        intent.putExtra("token", token);
                                        startActivity(intent);
                                        Toast.makeText(context,"You entered the wrong username or password, but for debug purposes we'll pretend you got it right.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }catch(JSONException e){
                                    Toast.makeText(context,"Server error this should never happen",
                                            Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String token="token";
                                Intent intent;
                                intent = new Intent(user.getContext(), EventActivity.class);
                                intent.putExtra("token", token);
                                startActivity(intent);
                                Toast.makeText(context,"Failed to connect to the server (server is probably down), but for debug purposes we'll pretend everything went right.",
                                        Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        }
                );
                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        500,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(postRequest);
            }
        });
    }
}
