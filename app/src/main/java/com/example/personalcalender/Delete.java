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

import java.util.ArrayList;
import java.util.List;

public class Delete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        CalendarView calendarView=(CalendarView)findViewById(R.id.calendarView2);
        final EditText n=(EditText)findViewById(R.id.editText2);
        n.setEnabled(false);
        final TextView act=(TextView)findViewById(R.id.act);
        final Button button=(Button)findViewById(R.id.button5);
        final List<String> list=new ArrayList<>();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                final DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
                final String date=dayOfMonth+"-"+month+"-"+year;
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean flag=false;
                        for(DataSnapshot d:dataSnapshot.getChildren())
                        {
                            if(d.getKey().equals(date))
                            {
                                List<String> activities=(List)d.child("str").getValue();
                                Koushik k=new Koushik();
                                String j="";int count=1;
                                for(String i:activities)
                                {
                                    list.add(i);
                                    j+=(count++)+")"+i+"\n";
                                }
                                act.setText(j);
                                n.setEnabled(true);
                                flag=true;
                            }
                        }
                        if(!flag)act.setText("No Activities");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num=Integer.parseInt(n.getText().toString());
                        list.remove(num-1);
                        Koushik koushik=new Koushik();
                        koushik.setStr(list);
                        reference.child(date).setValue(koushik);
                        Toast.makeText(Delete.this,"Deleted Successfully",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
