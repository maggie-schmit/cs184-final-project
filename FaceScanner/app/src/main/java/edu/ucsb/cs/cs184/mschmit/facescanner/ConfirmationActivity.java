package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ConfirmationActivity extends AppCompatActivity  {

    TextView mConfirmationText;
    TextView mNameText;
    ImageView mFaceImage;
    Button mYesButton;
    Button mNoButton;

    String person_name = "placeholder";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_layout);

        mConfirmationText = (TextView) findViewById(R.id.is_this_you_text);

        mNameText = (TextView) findViewById(R.id.person_name);
        mFaceImage = (ImageView) findViewById(R.id.face_image);

        Intent intent = getIntent();
        Bitmap image = (Bitmap) intent.getParcelableExtra("image");

        mFaceImage.setImageBitmap(image);

        mYesButton = (Button) findViewById(R.id.yes_button_2);
        mNoButton = (Button) findViewById(R.id.no_button_2);

        // if the database could not find a match, automatically go to add a person

        mYesButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                // go back to event homepage

                // create toast that confirms the person has been signed in

                Intent myIntent = new Intent(ConfirmationActivity.this, EventHomePage.class);
                myIntent.putExtra("key", 1);
                myIntent.putExtra("person_name", person_name);
                ConfirmationActivity.this.startActivity(myIntent);


            }
        });

        mNoButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                // go back to event homepage

                // create toast that confirms the person has been signed in

                // open dialog to see if they are sure
                final AlertDialog alertDialog = new AlertDialog.Builder(ConfirmationActivity.this).create();
                alertDialog.setTitle("Alert Dialog");
                alertDialog.setMessage("Are you already a club member?");
                alertDialog.setIcon(R.drawable.ic_subdirectory_arrow_left_black_24dp);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // go to lookup activity
                        Intent myIntent = new Intent(ConfirmationActivity.this, MemberLookupActivity.class);
                        startActivity(myIntent);

                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // go to make member activity
                        Intent myIntent = new Intent(ConfirmationActivity.this, MakeNewActivity.class);
                        startActivity(myIntent);

                    }
                });



                alertDialog.show();



            }
        });




    }
}
