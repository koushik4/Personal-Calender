package com.example.personalcalender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class Calendar extends AppCompatActivity {
    String str="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
       final Button b=(Button)findViewById(R.id.button);
        CalendarView calendarView=(CalendarView)findViewById(R.id.calendarView);
        final EditText editText=(EditText)findViewById(R.id.act);
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        final Koushik koushik=new Koushik();
        Calendar c=new Calendar();
        final TextView t=(TextView)findViewById(R.id.textView2);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, final int year, final int month, final int dayOfMonth) {
                str="";
                editText.setText("");
                t.setText("");
                final String time=dayOfMonth+"-"+month+"-"+year;
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot d:dataSnapshot.getChildren())
                        {
                            if(d.getKey().equals(time)) {
                                String activity = d.child("str").getValue().toString();
                                str += activity;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                t.setText(str);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        str+=editText.getText()+"\n";
                        koushik.setStr(str);
                        ref.child(time).setValue(koushik);
                        Toast.makeText(Calendar.this,"activity added",Toast.LENGTH_LONG).show();
                        editText.setText("");
                    }
                });
            }
        });
    }
}
