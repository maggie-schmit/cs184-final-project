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

public class RegisterActivity extends AppCompatActivity {
    Button button;
    EditText email;
    EditText name;
    EditText rpw;
    EditText pw;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        button=findViewById(R.id.register_button);
        email=findViewById(R.id.register_email);
        name=findViewById(R.id.register_name);
        rpw=findViewById(R.id.register_confirm_pw);
        pw=findViewById(R.id.register_pw);
        button=findViewById(R.id.register_button);
        context=this;
        Intent intent = getIntent();
        email.setText(intent.getStringExtra("email"));

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(context);
                String pwStr=pw.getText().toString();
                if(!rpw.getText().toString().equals(pwStr))return;
                if(email.getText().toString().equals(""))return;
                if(name.getText().toString().equals(""))return;
                if(pwStr.equals(""))return;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://csquids-cs184-final-project.herokuapp.com/api/v1/createOrg",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_LONG).show();
                                Log.wtf(response,response);
                                try {
                                    JSONObject jsonObj=new JSONObject(response);
                                    Intent intent;
                                    intent = new Intent(context, EventActivity.class);
                                    intent.putExtra("id", jsonObj.getInt("id"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RegisterActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                                Log.wtf(error.toString(),error.toString());
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("email",email.getText().toString());
                        params.put("name",name.getText().toString());
                        params.put("password", pw.getText().toString());
                        return params;
                    }

                };
                queue.add(stringRequest);
            }
        });
    }
}
