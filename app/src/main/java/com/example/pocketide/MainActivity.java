package com.example.pocketide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pocketide.adapter.NumberAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> count;
    NumberAdapter numberAdapter;
    int initial_line_count=1,final_line_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        count = new ArrayList<>();

        EditText editText;
        RecyclerView recyclerView;
        editText = findViewById(R.id.editTextTextMultiLine);
        editText.setLineSpacing(0,1.17f); //spacing to align with recyclerview

        recyclerView = findViewById(R.id.recyclerView);

        //attaching adapter to RecyclerView
        numberAdapter = new NumberAdapter(count, MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(numberAdapter);

        //listener to count number of lines in EditText
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                initial_line_count=final_line_count;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final_line_count=editText.getLineCount();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(initial_line_count!=final_line_count && initial_line_count<final_line_count) {
                    //adding number of lines to the list
                    count.add(String.valueOf(final_line_count));
                    //notifying the updates to the adapter
                    numberAdapter.notifyDataSetChanged();
                }
                else if(initial_line_count!=final_line_count && initial_line_count>final_line_count){
                    count.remove(String.valueOf(initial_line_count));
                    numberAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
