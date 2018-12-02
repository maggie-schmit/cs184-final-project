package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.view.WindowManager;

public class MetricsActivity extends AppCompatActivity {

    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.metrics_layout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        // TODO: populate details with information from the database


        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new FloatingActionButton.OnClickListener(){
            @Override
            public void onClick(View view) {

                // go back to the home page
                final AlertDialog alertDialog = new AlertDialog.Builder(MetricsActivity.this).create();
                alertDialog.setTitle("Alert Dialog");
                alertDialog.setMessage("Are you sure? Any changes you made to the event will be saved to the database");
                alertDialog.setIcon(R.drawable.ic_subdirectory_arrow_left_black_24dp);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // save changes to interface

                        // TODO: save changes to interface
                        // return to EventHomePage
                        Intent myIntent = new Intent(MetricsActivity.this, EventHomePage.class);
                        myIntent.putExtra("key", 3);
                        MetricsActivity.this.startActivity(myIntent);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Don't Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do not save changes to interface

                        // return to EventHomePage
                        Intent myIntent = new Intent(MetricsActivity.this, EventHomePage.class);
                        myIntent.putExtra("key", 4);
                        MetricsActivity.this.startActivity(myIntent);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                        alertDialog.cancel();
                    }
                });


                alertDialog.show();


            }
        });
    }
}
