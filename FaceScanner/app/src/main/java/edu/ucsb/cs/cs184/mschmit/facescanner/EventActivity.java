package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.RelativeLayout;
import android.view.ViewGroup;

import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventActivity extends AppCompatActivity {
    Button createEvent;
    static int orgId=-1;
    Context context;
    EventActivity ea;
    JSONArray jsonResponse = null;
    static final String [] jsonKeys=new String[]{"id","org_id","name","start_date","end_date"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        createEvent = findViewById(R.id.event_create_button);
        Intent intent = getIntent();
        if(intent.hasExtra("id"))
            orgId = Integer.valueOf(intent.getStringExtra("id"));
        context=this;
        ea=this;
        loadEvents();
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(context, CreateEventActivity.class);
                intent.putExtra("id",orgId);
                startActivity(intent);
            }
        });
    }
    private void injectEventsToGUI(){
        LinearLayout container=findViewById(R.id.event_container);
        LinearLayout menu=new LinearLayout(context);
        float mnWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        for(String s:jsonKeys){
            TextView txt=new TextView(context);
            txt.setText(s);
            txt.setId(View.generateViewId());
            txt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            txt.setWidth((int)mnWidth);
            menu.addView(txt);
        }
        container.addView(menu);
        for(int i=0;i<jsonResponse.length();i++){
            LinearLayout ll=new LinearLayout(context);
            ll.setId(View.generateViewId());
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.HORIZONTAL);
            final JSONObject obj;
            try{
                obj=jsonResponse.getJSONObject(i);
            }catch(Exception e) {
                e.printStackTrace();
                continue;
            }
            for(String s:jsonKeys){
                TextView txt=new TextView(context);
                txt.setId(View.generateViewId());
                txt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                txt.setWidth((int)mnWidth);
                try {
                    String str = obj.getString(s);
                    txt.setText(str);
                }catch(Exception e){
                    e.printStackTrace();
                }
                ll.addView(txt);
            }
            Button viewEvent=new Button(context);
            viewEvent.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            viewEvent.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            viewEvent.setId(View.generateViewId());
            viewEvent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            viewEvent.setText("View Event");
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
                            intent.putExtra("eventId", Integer.toString(obj.getInt("id")));
                            intent.putExtra("orgId", orgId);
                            startActivity(intent);
//                            Toast.makeText(context, "This is intended to start EventHomepage activity. This does nothing until you uncomment the 4 lines above where this is in code.",
//                                    Toast.LENGTH_SHORT).show();
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 25);
            viewEvent.setLayoutParams(params);
            ll.addView(viewEvent);
            container.addView(ll);
        }






//        JSONArray arr=null;
//        try {
//            arr=jsonResponse.getJSONArray("events");
//            Log.wtf("rejjioid",jsonResponse.toString());
//            Log.wtf("rejjioid",arr.toString());
//        } catch (JSONException e) {
//            Log.wtf("DSIJSJDOIF",jsonResponse.toString());
//            e.printStackTrace();
//        }
//        LinearLayout container=findViewById(R.id.event_container);
//        LinearLayout menu=new LinearLayout(context);
//        TextView menuName=new TextView(context);
//        menuName.setText("Event Name");
//        menuName.setId(View.generateViewId());
//        menuName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//        float mnWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, getResources().getDisplayMetrics());
//        menuName.setWidth((int)mnWidth);
//        TextView menuMemberCount=new TextView(context);
//        menuMemberCount.setText("Number of Members");
//        menuMemberCount.setId(View.generateViewId());
//        menuMemberCount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//        float mmcWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
//        menuMemberCount.setWidth((int)mmcWidth);
//        TextView v=new TextView(context);
//        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//        float vWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
//        v.setWidth((int)vWidth);
//        menu.addView(menuName);
//        menu.addView(menuMemberCount);
//        menu.addView(v);
//        container.addView(menu);
//        for(int i=0;i<arr.length();i++){
//            try {
//                final JSONObject obj=(JSONObject)arr.get(i);
//                Log.wtf("EWJIEJOIWE",obj.toString());
//                LinearLayout ll=new LinearLayout(context);
//                ll.setId(View.generateViewId());
//                TextView name=new TextView(context);
//                // event name
//                name.setText(obj.getString("name"));
//                name.setId(View.generateViewId());
//                name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//                float nameWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, getResources().getDisplayMetrics());
//                name.setWidth((int)nameWidth);
//                TextView memberCount=new TextView(context);
//                memberCount.setText(obj.getString("member_count"));
//                memberCount.setId(View.generateViewId());
//                memberCount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//                float mcWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
//                memberCount.setWidth((int)mcWidth);
//                Button viewEvent=new Button(context);
//                viewEvent.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                viewEvent.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//// add rule
//                // params.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.ll_status);
//
//
//
//                viewEvent.setId(View.generateViewId());
//                viewEvent.setText("View Event");
//                viewEvent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//                viewEvent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent;
//                        intent = new Intent(EventActivity.this, EventHomePage.class);
//                        // put event info here
//                        // event name
//                        try {
//                            intent.putExtra("key", 0);
//                            intent.putExtra("event_name", obj.getString("name"));
//                            startActivity(intent);
////                            Toast.makeText(context, "This is intended to start EventHomepage activity. This does nothing until you uncomment the 4 lines above where this is in code.",
////                                    Toast.LENGTH_SHORT).show();
//                        }catch(JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                float buttonWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
//                viewEvent.setWidth((int)buttonWidth);
//
//                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//                params.setMargins(0, 0, 0, 25);
//                viewEvent.setLayoutParams(params);
//                ll.setOrientation(LinearLayout.HORIZONTAL);
//                ll.addView(name);
//                ll.addView(memberCount);
//                ll.addView(viewEvent);
//                container.addView(ll);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        Log.wtf("INJECTED ELEMENTS TO GUI","DSLLDSFS");

    }
    private void loadEvents(){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://csquids-cs184-final-project.herokuapp.com/api/v1/getEvents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EventActivity.this,response,Toast.LENGTH_LONG).show();
                        Log.wtf(response,response);
                        try {
                            jsonResponse=new JSONArray(response);
                            injectEventsToGUI();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EventActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        Log.wtf(error.toString(),error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
//                        Log.wtf(email.getText().toString(),pw.getText().toString());
                params.put("orgId",String.valueOf(orgId));
                return params;
            }

        };
        queue.add(stringRequest);
//        JSONObject json = new JSONObject();
//        try {
//            json.put("token",token);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestQueue queue = Volley.newRequestQueue(context);
//        String url = "http://54.212.61.66/get_events";
//        JsonObjectRequest postRequest = new JsonObjectRequest(url, json,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // response
//                        Log.wtf("hello", response.toString()+" token: "+token);
//                        try {
//                            if (response.getBoolean("token_correct")) {
//                                jsonResponse=response;
//                            }else{
//                                jsonResponse.put("token_correct",true);
//
//                                JSONObject data=new JSONObject();
//                                data.put("name","Giraffe");
//                                data.put("member_count",12);
//                                JSONObject data2=new JSONObject();
//                                data2.put("name","Horse");
//                                data2.put("member_count",14);
//                                JSONObject data3=new JSONObject();
//                                data3.put("name","Penguin");
//                                data3.put("member_count",45);
//                                JSONArray defaultData=new JSONArray();
//                                defaultData.put(data);
//                                defaultData.put(data2);
//                                defaultData.put(data3);
//                                jsonResponse.put("events",defaultData);
//
//                                Toast.makeText(context,"You entered the wrong username or password, but for debug purposes we'll pretend you got it right.",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }catch(JSONException e){
//                            Toast.makeText(context,"Server error this should never happen",
//                                    Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                        ea.injectEventsToGUI();
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        try {
//                            jsonResponse.put("token_correct",true);
//                            JSONObject data=new JSONObject();
//                            data.put("name","Giraffe");
//                            data.put("member_count",12);
//                            JSONObject data2=new JSONObject();
//                            data2.put("name","Horse");
//                            data2.put("member_count",14);
//                            JSONObject data3=new JSONObject();
//                            data3.put("name","Penguin");
//                            data3.put("member_count",45);
//                            JSONArray defaultData=new JSONArray();
//                            defaultData.put(data);
//                            defaultData.put(data2);
//                            defaultData.put(data3);
//                            jsonResponse.put("events",defaultData);
//                            Toast.makeText(context,"Failed to connect to the server (server is probably down), but for debug purposes we'll pretend everything went alright.",
//                                    Toast.LENGTH_SHORT).show();
//                            ea.injectEventsToGUI();
//                        } catch (JSONException e1) {
//                            e1.printStackTrace();
//                        }
//
//                        error.printStackTrace();
//                    }
//                }
//        );
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                500,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(postRequest);
    }
}
