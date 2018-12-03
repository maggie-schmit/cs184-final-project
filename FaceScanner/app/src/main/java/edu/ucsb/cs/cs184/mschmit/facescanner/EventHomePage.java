package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.EventLog;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import android.util.Base64;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


import android.content.Intent;
import android.provider.MediaStore;

import android.graphics.Bitmap;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.json.JSONException;
import com.android.volley.VolleyError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.AuthFailureError;
import android.util.Log;

import java.util.Map;
import java.util.HashMap;
import android.support.design.widget.FloatingActionButton;


public class EventHomePage extends AppCompatActivity {

    // TODO: edit how the text looks in each activity

    // TODO: ensure that each activity knows which event they are dealing with

    TextView mTitle;
    Button mCameraButton;
    Button mMetricsButton;
    Button mCloseButton;

    FloatingActionButton mBackButton;

    String mEventName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_home_page);
        // make title
        mTitle = (TextView) findViewById(R.id.welcome_text);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            // returning to home page after going to another page
            // get confirmation code
            int code = intent.getIntExtra("key", 0);


            switch(code){
                case 0:
                    String event_name = intent.getStringExtra("event_name");
                    String title = "Welcome to " + event_name+ " home page";
                    mEventName = event_name;
                    mTitle.setText(title);
                    break;
                case 1:
                    // person already existed and was added to the event
                    // get person name
                    String display_name = (String) intent.getStringExtra("person_name");
                    String toast_string = display_name + " was added to the event";
                    Toast toast = Toast.makeText(getApplicationContext(),
                            toast_string,
                            Toast.LENGTH_SHORT);

                    toast.show();
                    break;
                case 2:
                    // person did not exist and was added to the database
                    display_name = (String) intent.getStringExtra("person_name");
                    toast_string = display_name + " was added to the database";
                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            toast_string,
                            Toast.LENGTH_SHORT);

                    toast2.show();
                    break;
                case 3:
                    // changed the event details
                    toast_string = "Event details were updated";
                    Toast toast3 = Toast.makeText(getApplicationContext(),
                            toast_string,
                            Toast.LENGTH_SHORT);

                    toast3.show();
                    break;
                case 4:
                    // the event details were not changed
                    toast_string = "Event details were not changed";
                    Toast toast4 = Toast.makeText(getApplicationContext(),
                            toast_string,
                            Toast.LENGTH_SHORT);

                    toast4.show();
                    break;
                default:
                    // do nothing
                    break;
            }


        }



        // make buttons
        mCameraButton = (Button) findViewById(R.id.camera_button);
        mMetricsButton = (Button) findViewById(R.id.metrics_button);
        mCloseButton = (Button) findViewById(R.id.close_button);

        mBackButton = (FloatingActionButton) findViewById(R.id.fab_sign_out);

        // set up button activities

        mCameraButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                // go to  camera widget

                // take picture
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }

                // TODO: send jpeg to API to ensure that I can do that


            }
        });

        mMetricsButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                // go to metrics page
            Intent myIntent = new Intent(EventHomePage.this, MetricsActivity.class);
            // myIntent.putExtra("key", value); //Optional parameters
            EventHomePage.this.startActivity(myIntent);

            }
        });

        mCloseButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                // open dialog to see if they are sure
                final AlertDialog alertDialog = new AlertDialog.Builder(EventHomePage.this).create();
                alertDialog.setTitle("Alert Dialog");
                alertDialog.setMessage("Are you sure? Once you close the event you cannot reopen it");
                alertDialog.setIcon(R.drawable.ic_subdirectory_arrow_left_black_24dp);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close event

                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // keep event open
                    }
                });


                alertDialog.show();

                // if sure, close the event in the database
            }
        });


        mBackButton.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {

                // ask are you sure

                // go back to select event activity
                // go back to the home page
                final AlertDialog alertDialog = new AlertDialog.Builder(EventHomePage.this).create();
                alertDialog.setTitle("Alert Dialog");
                alertDialog.setMessage("Sign out of event?");
                alertDialog.setIcon(R.drawable.ic_subdirectory_arrow_left_black_24dp);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // save changes to interface
                        // return to EventHomePage
                        Intent myIntent = new Intent(EventHomePage.this, EventActivity.class);
                        EventHomePage.this.startActivity(myIntent);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do not save changes to interface

                        alertDialog.cancel();
                    }
                });

                alertDialog.show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            final Bitmap imageBitmap = (Bitmap) extras.get("data");

            System.out.println("got bitmap");
            String url = "http://csquids-cs184-final-project.herokuapp.com/api/v1/checkFace";

            //converting image to base64 string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            final String imageString = Base64.encodeToString(imageBytes, 0);

            //sending image to server
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {

                    Intent myIntent = new Intent(EventHomePage.this, ConfirmationActivity.class);
                    myIntent.putExtra("image", imageBitmap); //Optional parameters
                    EventHomePage.this.startActivity(myIntent);

                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    System.out.println("got an error response");
                }
            }) {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("image", imageString);
                    return parameters;
                }
            };

            RequestQueue rQueue = Volley.newRequestQueue(EventHomePage.this);
            rQueue.add(request);
//
//            JSONObject json = new JSONObject();
//            try {
//                json.put("user","hello");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            RequestQueue queue = Volley.newRequestQueue(this);
//            String url = "http://csquids-cs184-final-project.herokuapp.com/api/v1/checkFace";
//            JsonObjectRequest postRequest = new JsonObjectRequest(url, json,
//                    new Response.Listener<JSONObject>()
//                    {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            // response
//                            Log.wtf("Respdafsonse", response.toString());
//                            try {
//                                if (response.getBoolean("logged_in")) {
////                                    String token=response.getString("token");
////                                    Intent intent;
////                                    intent = new Intent(user.getContext(), CreateEventActivity.class);
////                                    intent.putExtra("token", token);
////                                    startActivity(intent);
//                                }
//                            }catch(JSONException e){
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener()
//                    {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // error
//                            error.printStackTrace();
//                        }
//                    }
//            );
//
//            queue.add(postRequest);

            // TODO: send bitmap to API

            // TODO: make "loading" activity so the user knows something is going on while the server side is running

            // TODO: if the user is not recognized, automatically jump to the "create user" page




        }
    }

}
