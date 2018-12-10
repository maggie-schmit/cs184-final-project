package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.view.WindowManager;

import android.widget.TextView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class MetricsActivity extends AppCompatActivity {

    FloatingActionButton mFab;

    TextView mNameText;
    TextView mAttendeeNumber;

    ListView mAttendeeList;

    String mEventID;
    String mOrgID;

    ArrayList<String> mAttendeeArrayList;

    int numAttendees = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.metrics_layout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();



        mNameText = (TextView) findViewById(R.id.event_name_placeholder);
        mAttendeeNumber = (TextView) findViewById(R.id.attendees_number);

        mAttendeeList = (ListView) findViewById(R.id.attendees_list);


        if(intent.getExtras() != null){
            // get the event ID
            mEventID = intent.getStringExtra("eventID");
            mNameText.setText(mEventID);

            mOrgID = intent.getStringExtra("orgId");
        }


        // get attendees
        ImageLoaderTask ilt = new ImageLoaderTask();
        try {


            JSONArray resultsArray = ilt.execute().get();

            mAttendeeArrayList = new ArrayList<String>();
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, mAttendeeArrayList);

            mAttendeeList.setAdapter(arrayAdapter);

            if(mAttendeeArrayList != null) {
                numAttendees = resultsArray.length();
            }else{
                numAttendees = 0;
            }

            // add all things to the array
            for(int i=0; i < numAttendees; i++){
                JSONObject curr_object = resultsArray.getJSONObject(i);
                String first_name = curr_object.getString("first_name");
                String last_name = curr_object.getString("last_name");

                String display_name = first_name + " " + last_name;

                mAttendeeArrayList.add(display_name);
                arrayAdapter.notifyDataSetChanged();
            }

        }catch(ExecutionException ee){
            ee.printStackTrace();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }catch(JSONException je){
            je.printStackTrace();
        }

        System.out.println("num attendees is: " + numAttendees);
        mAttendeeNumber.setText(Integer.toString(numAttendees));


        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(MetricsActivity.this, EventHomePage.class);
                myIntent.putExtra("key", 4);
                myIntent.putExtra("eventId", mEventID);
                myIntent.putExtra("orgId", mOrgID);
                MetricsActivity.this.startActivity(myIntent);
            }
        });
    }

    private class ImageLoaderTask extends AsyncTask<String, Void, JSONArray>

    {

        @Override
        protected JSONArray doInBackground(String... imagePaths){

            String url = "http://csquids-cs184-final-project.herokuapp.com/api/v1/getEventAttendance";
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
                // Send ORGID
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"orgId\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(mOrgID).append(CRLF).flush();


                // Send Event ID
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"eventId\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(mEventID).append(CRLF).flush();

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

                JSONArray jsonArray = new JSONArray(builder.toString());
                // JSONObject jsonObject = new JSONObject(builder.toString());
                return jsonArray;
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }


    }

}
