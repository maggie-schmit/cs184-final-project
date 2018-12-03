package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class EventActivity extends AppCompatActivity {
    Button createEvent;
    String token;
    Context context;
    EventActivity ea;
    JSONObject jsonResponse= new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        createEvent = findViewById(R.id.event_create_button);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        context=this;
        ea=this;
        loadEvents();
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(context, CreateEventActivity.class);
                startActivity(intent);
            }
        });
    }
    private void injectEventsToGUI(){
        JSONArray arr=null;
        try {
            arr=jsonResponse.getJSONArray("events");
            Log.wtf("rejjioid",jsonResponse.toString());
            Log.wtf("rejjioid",arr.toString());
        } catch (JSONException e) {
            Log.wtf("DSIJSJDOIF",jsonResponse.toString());
            e.printStackTrace();
        }
        LinearLayout container=findViewById(R.id.event_container);
        LinearLayout menu=new LinearLayout(context);
        TextView menuName=new TextView(context);
        menuName.setText("Event Name");
        menuName.setId(View.generateViewId());
        menuName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        float mnWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, getResources().getDisplayMetrics());
        menuName.setWidth((int)mnWidth);
        TextView menuMemberCount=new TextView(context);
        menuMemberCount.setText("Number of Members");
        menuMemberCount.setId(View.generateViewId());
        menuMemberCount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        float mmcWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        menuMemberCount.setWidth((int)mmcWidth);
        TextView v=new TextView(context);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        float vWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        v.setWidth((int)vWidth);
        menu.addView(menuName);
        menu.addView(menuMemberCount);
        menu.addView(v);
        container.addView(menu);
        for(int i=0;i<arr.length();i++){
            try {
                final JSONObject obj=(JSONObject)arr.get(i);
                Log.wtf("EWJIEJOIWE",obj.toString());
                LinearLayout ll=new LinearLayout(context);
                ll.setId(View.generateViewId());
                TextView name=new TextView(context);
                // event name
                name.setText(obj.getString("name"));
                name.setId(View.generateViewId());
                name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                float nameWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, getResources().getDisplayMetrics());
                name.setWidth((int)nameWidth);
                TextView memberCount=new TextView(context);
                memberCount.setText(obj.getString("member_count"));
                memberCount.setId(View.generateViewId());
                memberCount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                float mcWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
                memberCount.setWidth((int)mcWidth);
                Button viewEvent=new Button(context);
                viewEvent.setId(View.generateViewId());
                viewEvent.setText("View Event");
                viewEvent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                viewEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        intent = new Intent(EventActivity.this, EventHomePage.class);
                        // put event info here
                        // event name
                        try {
                            intent.putExtra("key", 0);
                            intent.putExtra("event_name", obj.getString("name"));
                            startActivity(intent);
//                            Toast.makeText(context, "This is intended to start EventHomepage activity. This does nothing until you uncomment the 4 lines above where this is in code.",
//                                    Toast.LENGTH_SHORT).show();
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
                float buttonWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
                viewEvent.setWidth((int)buttonWidth);

                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.addView(name);
                ll.addView(memberCount);
                ll.addView(viewEvent);
                container.addView(ll);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.wtf("INJECTED ELEMENTS TO GUI","DSLLDSFS");

    }
    private void loadEvents(){
        JSONObject json = new JSONObject();
        try {
            json.put("token",token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://54.212.61.66/get_events";
        JsonObjectRequest postRequest = new JsonObjectRequest(url, json,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.wtf("hello", response.toString()+" token: "+token);
                        try {
                            if (response.getBoolean("token_correct")) {
                                jsonResponse=response;
                            }else{
                                jsonResponse.put("token_correct",true);

                                JSONObject data=new JSONObject();
                                data.put("name","Giraffe");
                                data.put("member_count",12);
                                JSONObject data2=new JSONObject();
                                data2.put("name","Horse");
                                data2.put("member_count",14);
                                JSONObject data3=new JSONObject();
                                data3.put("name","Penguin");
                                data3.put("member_count",45);
                                JSONArray defaultData=new JSONArray();
                                defaultData.put(data);
                                defaultData.put(data2);
                                defaultData.put(data3);
                                jsonResponse.put("events",defaultData);

                                Toast.makeText(context,"You entered the wrong username or password, but for debug purposes we'll pretend you got it right.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException e){
                            Toast.makeText(context,"Server error this should never happen",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        ea.injectEventsToGUI();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            jsonResponse.put("token_correct",true);
                            JSONObject data=new JSONObject();
                            data.put("name","Giraffe");
                            data.put("member_count",12);
                            JSONObject data2=new JSONObject();
                            data2.put("name","Horse");
                            data2.put("member_count",14);
                            JSONObject data3=new JSONObject();
                            data3.put("name","Penguin");
                            data3.put("member_count",45);
                            JSONArray defaultData=new JSONArray();
                            defaultData.put(data);
                            defaultData.put(data2);
                            defaultData.put(data3);
                            jsonResponse.put("events",defaultData);
                            Toast.makeText(context,"Failed to connect to the server (server is probably down), but for debug purposes we'll pretend everything went alright.",
                                    Toast.LENGTH_SHORT).show();
                            ea.injectEventsToGUI();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

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
}
