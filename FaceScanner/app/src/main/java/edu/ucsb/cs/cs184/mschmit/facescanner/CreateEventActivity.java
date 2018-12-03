package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {
    EditText name;
    EditText memberCount;
    Button button;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        name=findViewById(R.id.create_event_name);
        memberCount=findViewById(R.id.create_event_member_count);
        button=findViewById(R.id.create_event_button);
        context=this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // first does api call adding an event
                // TODO: add event to the API

                Intent intent;
                intent = new Intent(context, EventHomePage.class);
                intent.putExtra("key", 0);
                intent.putExtra("event_name", name.getText().toString());
                startActivity(intent);
            }
        });
    }
}
