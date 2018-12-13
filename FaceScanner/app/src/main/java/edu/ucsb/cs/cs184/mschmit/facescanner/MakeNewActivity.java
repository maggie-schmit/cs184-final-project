package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

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

public class MakeNewActivity extends AppCompatActivity {

    EditText mFirstNameText;
    EditText mLastNameText;
    EditText mEmailText;
    Button mSaveButton;

    File mImageFile;

    String mOrgID = "";
    String mEventId = "";

    /*
     * addMember should take the following data:
     * * a file called 'face', sent as form-data
     * * an org id
     * * email
     * * first name
     * * last name
     * everything except the file should be sent as regular raw data (-d)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_new_layout);

        // TODO: put database message into toast

        mFirstNameText = (EditText) findViewById(R.id.firstname_edit_text);
        mLastNameText = (EditText) findViewById(R.id.lastname_edit_text);
        mEmailText = (EditText) findViewById(R.id.email_edit_text);
        mSaveButton = (Button) findViewById(R.id.save_button);

        Intent intent = getIntent();
        File image_file;
        if (intent.getExtras() != null) {
            // get name of file
            String file_name = intent.getStringExtra("image_path");

            // get image from file
            mImageFile = new File(file_name);

            mOrgID = intent.getStringExtra("orgId");
            mEventId = intent.getStringExtra("eventId");

        }

        mSaveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check that all fields are filled out
                String firstname = mFirstNameText.getText().toString();
                String lastname = mLastNameText.getText().toString();
                String email = mEmailText.getText().toString();

                if (firstname.equals("") || lastname.equals("") || email.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "All fields must be filled out",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    return;
                }

                ImageLoaderTask ilt = new ImageLoaderTask();

                try {
                    JSONObject results = ilt.execute().get();



                    // check if there was an error
                    boolean gotError = false;
                    if(results.has("userError")){
                        gotError = results.getBoolean("userError");
                    }



                    if(gotError){
                        String message = results.getString("message");
                        Intent myIntent = new Intent(MakeNewActivity.this, EventHomePage.class);
                        myIntent.putExtra("key", 5);
                        myIntent.putExtra("message", message);
                        myIntent.putExtra("orgId", mOrgID);
                        myIntent.putExtra("eventId", mEventId);
                        MakeNewActivity.this.startActivity(myIntent);

                    }else {
                        // add user to event

                        String memberID = results.getString("id");

                        AddToEventTask atet = new AddToEventTask();
                        atet.execute(memberID);


                        Intent myIntent = new Intent(MakeNewActivity.this, EventHomePage.class);
                        myIntent.putExtra("key", 2);
                        myIntent.putExtra("person_name", mFirstNameText.getText().toString());
                        myIntent.putExtra("orgId", mOrgID);
                        myIntent.putExtra("eventId", mEventId);
                        MakeNewActivity.this.startActivity(myIntent);
                    }

                }catch(ExecutionException ee){
                    ee.printStackTrace();
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }catch(JSONException je){
                    je.printStackTrace();
                }

            }
        });

    }

    private class ImageLoaderTask extends AsyncTask<String, Void, JSONObject>
    {


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected JSONObject doInBackground(String... imagePaths) {

            String url = "http://csquids-cs184-final-project.herokuapp.com/api/v1/addMember";
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
                // Send orgID param
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"orgId\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(mOrgID).append(CRLF).flush();

                //Send first name param
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"firstName\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(mFirstNameText.getText().toString()).append(CRLF).flush();

                //Send second name param
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"lastName\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(mLastNameText.getText().toString()).append(CRLF).flush();

                //Send email param
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"email\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(mEmailText.getText().toString()).append(CRLF).flush();


                // Send text file.
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"face\"; filename=\"" + "face" + "\"").append(CRLF);
                writer.append("Content-Type: image/jpg;").append(CRLF); // Text file itself must be saved in this charset!
                writer.append(CRLF).flush();
                Files.copy(mImageFile.toPath(), output);
                output.flush(); // Important before continuing with writer!
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.


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
            // super.onPostExecute(d);
        }
    }


    private class AddToEventTask extends AsyncTask<String, Void, JSONObject>

    {

        Dialog mLoadingDialog;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(String... imagePaths) {

            String memID = imagePaths[0];

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
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(mEventId).append(CRLF).flush();

                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"memberId\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(memID).append(CRLF).flush();

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
            // change the viewer
        }

    }

}