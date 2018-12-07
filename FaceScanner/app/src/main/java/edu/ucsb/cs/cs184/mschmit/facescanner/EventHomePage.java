package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.Manifest;


import android.content.Intent;
import android.provider.MediaStore;
import android.os.Environment;
import android.os.Build;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import java.text.SimpleDateFormat;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import android.util.JsonReader;

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

import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

import android.widget.ProgressBar;

import android.app.Dialog;




public class EventHomePage extends AppCompatActivity {

    // TODO: edit how the text looks in each activity

    // TODO: ensure that each activity knows which event they are dealing with

    TextView mTitle;
    Button mCameraButton;
    Button mMetricsButton;
    Button mCloseButton;

    FloatingActionButton mBackButton;

    String mEventName = "";
    String mOrgID = "";

    private String mCurrPath = "";


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

                case 5:
                    toast_string = "Error occurred while adding member to database";
                    Toast toast5 = Toast.makeText(getApplicationContext(),
                            toast_string,
                            Toast.LENGTH_SHORT);

                    toast5.show();
                    break;
                case 6:
                    toast_string = "Cannot add to event; try again";
                    Toast toast6 = Toast.makeText(getApplicationContext(),
                            toast_string,
                            Toast.LENGTH_SHORT);

                    toast6.show();
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
             final Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Uri photo = (Uri) extras.get("uri_input");

            mImageBitmap = imageBitmap;

            File file;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions, 1);
            onRequestPermissionsResult(requestCode,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},grantResults);





        }
    }

    private int grantResults[];
    private Bitmap mImageBitmap;

    @Override // android recommended class to handle permissions
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/face_photos";


        ImageLoaderTask ilt = new ImageLoaderTask();
        JSONObject infoFromDatabase;
        try {
            infoFromDatabase = ilt.execute(file_path).get();
        }catch(ExecutionException ee){
            ee.printStackTrace();
            return;
        }catch(InterruptedException ie){
            ie.printStackTrace();
            return;
        }


        // check if the person was already in the database
        try {
            boolean notInDatabase = infoFromDatabase.getBoolean("userError");
            if(!notInDatabase){
                // go to confirmation activity

                String memberID = infoFromDatabase.getString("member_id");
                String eventID = infoFromDatabase.getString("event_id");
                Intent intent;
                intent = new Intent(EventHomePage.this, ConfirmationActivity.class);
                intent.putExtra("image_path", mCurrPath);
                intent.putExtra("memberID", memberID);
                intent.putExtra("eventID", mEventName);
                intent.putExtra("orgID", mOrgID);
                // intent.putExtra("orgId", );
                startActivity(intent);
            }else{
                // go to make new user activity

                Intent intent;
                intent = new Intent(EventHomePage.this, MakeNewActivity.class);
                intent.putExtra("image_path", mCurrPath);
                startActivity(intent);
            }
        }catch(JSONException je){
            je.printStackTrace();
            return;
        }





    }


    private class ImageLoaderTask extends AsyncTask<String, Void, JSONObject>

    {

        Dialog mLoadingDialog;


        @Override
        protected void onPreExecute()
        {

            mLoadingDialog = new Dialog(EventHomePage.this);
            mLoadingDialog.setContentView(R.layout.loading_dialog);
            mLoadingDialog.create();
            mLoadingDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... imagePaths){


            File file;
            File dir = new File(imagePaths[0]);
            if(!dir.exists()) {
                dir.mkdir();
            }
            try {
                dir.createNewFile();


                System.out.println("directory exists: " + dir.exists());
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                file = new File(dir, timeStamp + ".jpeg");
                mCurrPath = file.getAbsolutePath();
                file.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
                return null;
            }

            try {
                FileOutputStream fOut = new FileOutputStream(file);
                mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();

            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException ie){
                ie.printStackTrace();
            }


            String url = "http://csquids-cs184-final-project.herokuapp.com/api/v1/checkFace";
            String charset = "UTF-8";
            String param = "1";
            // File binaryFile = new File("/path/to/file.bin");
            String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
            String CRLF = "\r\n"; // Line separator required by multipart/form-data.
            HttpURLConnection connection=null;
            try{
                connection = (HttpURLConnection)(new URL(url).openConnection());
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            }catch(Exception e){
                System.out.println("dsfdsgh");
            }

            try (
                    OutputStream output = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
            ) {
                // Send normal param.
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"orgId\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(param).append(CRLF).flush();

                // Send text file.
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"face\"; filename=\"" + file.getName() + "\"").append(CRLF);
                writer.append("Content-Type: image/jpg;").append(CRLF); // Text file itself must be saved in this charset!
                writer.append(CRLF).flush();
                Files.copy(file.toPath(), output);
                output.flush(); // Important before continuing with writer!
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.


                output.flush(); // Important before continuing with writer!
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

                // End of multipart/form-data.
                writer.append("--" + boundary + "--").append(CRLF).flush();
            }catch(Exception e){
                System.out.println("filaed");
                e.printStackTrace();
            }
            try{
                System.out.println(connection.toString());
                int responseCode = ((HttpURLConnection) connection).getResponseCode();
                System.out.println(responseCode); // Should be 200
            }catch(Exception e){
                System.out.println("lul");
                e.printStackTrace();
            }

            // InputStream response = connection.getInputStream();
            BufferedReader rd;
            try{
                rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                    builder.append(line);

                }

                JSONObject jsonObject = new JSONObject(builder.toString());
                return jsonObject;
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject d) {
            mLoadingDialog.hide();
            mLoadingDialog.dismiss();
            // change the viewer
        }

    }

}
