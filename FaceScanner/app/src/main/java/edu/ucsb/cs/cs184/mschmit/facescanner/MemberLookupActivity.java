package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;

import java.lang.reflect.Member;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.ArrayAdapter;


public class MemberLookupActivity extends AppCompatActivity {

    EditText mSearchBar;
    Button mSearchButton;

    ListView mPossibleMembersList;

    List<String> person_list;
    ArrayAdapter<String> mArrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_lookup_layout);

        // Create a List from String Array elements
        person_list = new ArrayList<String>();

        // Create an ArrayAdapter from List
        mArrayAdapter = new ArrayAdapter<String>
                (this, R.layout.list_item_layout , person_list);

        mSearchBar = (EditText) findViewById(R.id.member_lookup_bar);
        mSearchButton = (Button) findViewById(R.id.lookup_button);
        mPossibleMembersList = (ListView) findViewById(R.id.possible_members_list);

        mPossibleMembersList.setAdapter(mArrayAdapter);

        mSearchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check if mSearchBar is empty

                // if not empty, look up the name in the database and populate the ListView with the names

                // add to person_list
                // mArrayAdapter.notifyDataSetChanged();
            }
        });


        mPossibleMembersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View row, int index, long rowID) {

                // create AlertDialog saying "are you sure that you are this person?"
                // open dialog to see if they are sure
                Object o = mPossibleMembersList.getItemAtPosition(index);
                final String name=(String)o;

                final AlertDialog alertDialog = new AlertDialog.Builder(MemberLookupActivity.this).create();
                alertDialog.setTitle("Alert Dialog");
                alertDialog.setMessage("Add " + name + " to event?" );
                alertDialog.setIcon(R.drawable.ic_subdirectory_arrow_left_black_24dp);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close event

                        // TODO: add person to event

                        // go to eventhomepage
                        Intent myIntent = new Intent(MemberLookupActivity.this, EventHomePage.class);
                        myIntent.putExtra("key", 1);
                        myIntent.putExtra("person_name", name);
                        MemberLookupActivity.this.startActivity(myIntent);
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // keep event open
                        alertDialog.cancel();
                    }
                });


                alertDialog.show();

            }
        });

    }

}
