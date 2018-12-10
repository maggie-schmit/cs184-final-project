package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static edu.ucsb.cs.cs184.mschmit.facescanner.EventActivity.orgId;

public class CreateEventActivity extends AppCompatActivity {
    EditText name;
//    EditText memberCount;
    Button button;
    Context context;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        name=findViewById(R.id.create_event_name);
//        memberCount=findViewById(R.id.create_event_member_count);
        button=findViewById(R.id.create_event_button);
        context=this;
        Intent intent=getIntent();
//        Log.wtf("sdjijere",intent.getExtras().toString());
        id=intent.getIntExtra("id",-1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals(""))return;
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://csquids-cs184-final-project.herokuapp.com/api/v1/createEvent",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(CreateEventActivity.this,response,Toast.LENGTH_LONG).show();
                                Log.wtf(response,response);
                                try {
                                    JSONObject obj=new JSONObject(response);
                                    Intent intent;
                                    intent = new Intent(context, EventHomePage.class);
                                    intent.putExtra("key", 0);
                                    intent.putExtra("event_name", obj.getString("name"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CreateEventActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                                Log.wtf(error.toString(),error.toString());
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
//                        Log.wtf(email.getText().toString(),pw.getText().toString());
                        params.put("orgId",String.valueOf(id));
                        params.put("name",name.getText().toString());
                        params.put("startDate",String.valueOf(System.currentTimeMillis()/1000));
                        Log.wtf("dfsfd",params.toString());
                        return params;
                    }

                };
                Log.wtf("dfdgfd",stringRequest.toString());
                queue.add(stringRequest);
            }
        });
    }
}
