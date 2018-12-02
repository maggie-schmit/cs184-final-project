package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class MakeNewActivity extends AppCompatActivity {

    EditText mNameText;
    EditText mEmailText;
    EditText mIdText;
    Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_new_layout);


        mNameText = (EditText) findViewById(R.id.name_edit_text);
        mEmailText = (EditText) findViewById(R.id.email_edit_text);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mSaveButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                // check that all fields are filled out
                String name = mNameText.getText().toString();
                String email = mEmailText.getText().toString();

                if(name.equals("") || email.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "All fields must be filled out",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    return;
                }

                // TODO: create unique id


                // TODO: add person to database


                Intent myIntent = new Intent(MakeNewActivity.this, EventHomePage.class);
                myIntent.putExtra("key", 2);
                myIntent.putExtra("person_name", name);
                MakeNewActivity.this.startActivity(myIntent);


            }
        });
    }
}
