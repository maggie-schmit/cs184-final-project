package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.util.EventLog;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import android.content.Intent;

import android.graphics.Bitmap;

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

public class ConfirmationActivity extends AppCompatActivity {

    TextView mConfirmationText;
    TextView mNameText;
    ImageView mFaceImage;
    Button mYesButton;
    Button mNoButton;

    String person_name = "placeholder";

    String eventID = "";
    String memID = "";
    String orgID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_layout);

        mConfirmationText = (TextView) findViewById(R.id.is_this_you_text);

        mNameText = (TextView) findViewById(R.id.person_name);
        mFaceImage = (ImageView) findViewById(R.id.face_image);

        Intent intent = getIntent();
        // Bitmap image = (Bitmap) intent.getParcelableExtra("image");
        eventID = intent.getStringExtra("eventID");
        memID = intent.getStringExtra("memberID");
        orgID = intent.getStringExtra("orgID");

        // mFaceImage.setImageBitmap(image);

        mYesButton = (Button) findViewById(R.id.yes_button_2);
        mNoButton = (Button) findViewById(R.id.no_button_2);

        // if the database could not find a match, automatically go to add a person

        mYesButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageLoaderTask ilt = new ImageLoaderTask();

                try {
                    JSONObject results = ilt.execute().get();


                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }catch(ExecutionException ee){
                    ee.printStackTrace();
                }
                // go back to event homepage

                // create toast that confirms the person has been signed in

                Intent myIntent = new Intent(ConfirmationActivity.this, EventHomePage.class);
                myIntent.putExtra("key", 1);
                myIntent.putExtra("person_name", person_name);
                myIntent.putExtra("orgID", orgID);
                ConfirmationActivity.this.startActivity(myIntent);


            }
        });

        mNoButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back to event homepage

                Intent myIntent = new Intent(ConfirmationActivity.this, MemberLookupActivity.class);
                myIntent.putExtra("orgID", orgID);
                myIntent.putExtra("key", 6);
                startActivity(myIntent);
            }
        });


    }

    private class ImageLoaderTask extends AsyncTask<String, Void, JSONObject>

    {

        Dialog mLoadingDialog;


        @Override
        protected void onPreExecute() {

            mLoadingDialog = new Dialog(ConfirmationActivity.this);
            mLoadingDialog.setContentView(R.layout.loading_dialog);
            mLoadingDialog.create();
            mLoadingDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... imagePaths) {


            String url = "http://csquids-cs184-final-project.herokuapp.com/api/v1/markAttendance";
            String charset = "UTF-8";
            String param = "1";
            // File binaryFile = new File("/path/to/file.bin");
            String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
            String CRLF = "\r\n"; // Line separator required by multipart/form-data.
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) (new URL(url).openConnection());
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            } catch (Exception e) {
                System.out.println("dsfdsgh");
            }

            try (
                    OutputStream output = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
            ) {
                // Send normal param.
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"eventId\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + eventID).append(CRLF);
                writer.append(CRLF).append(param).append(CRLF).flush();

                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"memberId\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + memID).append(CRLF);
                writer.append(CRLF).append(param).append(CRLF).flush();

                output.flush(); // Important before continuing with writer!
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

                // End of multipart/form-data.
                writer.append("--" + boundary + "--").append(CRLF).flush();
            } catch (Exception e) {
                System.out.println("filaed");
                e.printStackTrace();
            }
            try {
                System.out.println(connection.toString());
                int responseCode = ((HttpURLConnection) connection).getResponseCode();
                System.out.println(responseCode); // Should be 200
            } catch (Exception e) {
                System.out.println("lul");
                e.printStackTrace();
            }

            // InputStream response = connection.getInputStream();
            BufferedReader rd;
            try {
                rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                    builder.append(line);

                }

                JSONObject jsonObject = new JSONObject(builder.toString());
                return jsonObject;
            } catch (Exception e) {
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