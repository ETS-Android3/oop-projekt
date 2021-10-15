package com.example.testmaddafakka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testmaddafakka.view.MainView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private MainView mainView;
    private TextView escape;
    private ImageView escapeLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        escapeLogo = findViewById(R.id.escapeImage);
        escape = findViewById(R.id.escape);

        escape.setOnClickListener(view -> setMainView());
        escapeLogo.setOnClickListener(view -> setMainView());

        mainView = new MainView();
        setMainView();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("Test");

        /*
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(in);
            }
        });

         */

    }
    private void setMainView(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentInlogg, mainView);
        fragmentTransaction.commit();

    }
}

