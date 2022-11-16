package com.example.kontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;

public class ControllActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int temp = 16;
    int valueOn = 1;
    int valueOff = 0;
    private Button btnMinus;
    private Button btnPlus;
    private Button btnOn;
    private Button btnOff;
    private TextView nilaiSuhu;
    private TextView switchStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controll);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.control);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Data");

        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnOn = findViewById(R.id.btnOn);
        btnOff = findViewById(R.id.btnOff);
        nilaiSuhu = findViewById(R.id.txtSuhu);
        switchStatus = (TextView) findViewById(R.id.switchStatus);

        updateSuhu();
        getSuhu();
        updateOn();
        updateOff();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.control:
                        break;
                    case R.id.home:
                        startActivity(new Intent(ControllActivity.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.about:
                        startActivity(new Intent(ControllActivity.this, AboutActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
    }

    private void getSuhu() {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String dataTemperature = dataSnapshot.child("Suhu").getValue().toString();
                    nilaiSuhu.setText(dataTemperature);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateSuhu() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data");
        btnPlus = findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp++;
                if (temp <= 30) {
                    reference.child("Suhu").setValue(temp);
                    btnMinus = findViewById(R.id.btnMinus);
                    btnMinus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            temp--;
                            if (temp >= 16) {
                                reference.child("Suhu").setValue(temp);
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateOn() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("kontrolac");
        btnOn = findViewById(R.id.btnOn);
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("mati").setValue(valueOn);

            }

        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int dataStatus = Integer.parseInt(dataSnapshot.child("mati").getValue().toString());

                    if (dataStatus == 1) {
                        switchStatus.setText(" Status On");
                    } else {
                        switchStatus.setText("Status Off");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateOff() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("kontrolac");
        btnOff = findViewById(R.id.btnOff);
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("mati").setValue(valueOff);

            }
        });
    }
}