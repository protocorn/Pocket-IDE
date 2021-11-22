package com.example.pocketide;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketide.adapter.NumberAdapter;

import org.stringtemplate.v4.Interpreter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> count;
    NumberAdapter numberAdapter;
    int initial_line_count = 1, final_line_count;

    Button compile;
    EditText editText;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        compile = findViewById(R.id.compile);
        output = findViewById(R.id.output);

        count = new ArrayList<>();

        RecyclerView recyclerView;
        editText = findViewById(R.id.editTextTextMultiLine);
        editText.setLineSpacing(0, 1.17f); //spacing to align with recyclerview

        recyclerView = findViewById(R.id.recyclerView);

        compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Process p;
                StringBuffer output2 = new StringBuffer();
                String path = Environment.getExternalStorageDirectory().toString();
                File file = new File(path + "/PocketIDE/JavaPrograms/prog.java");
                /*file.canExecute();
                file.canWrite();
                file.canRead();*/
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    do{
                        String str= br.readLine();
                        editText.setText(str);
                    }while(br.readLine()!=null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileWriter writer = new FileWriter(file);
                    writer.append(editText.getText().toString());
                    writer.flush();
                    writer.close();
                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    p = Runtime.getRuntime().exec(path + "/PocketIDE/JavaPrograms/prog.java");
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(p.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        output2.append(line).append("\n");
                        p.waitFor();
                    }
                } catch (IOException | InterruptedException e) {
                    output.setText(e.toString());
                    e.printStackTrace();
                }
                String response = output2.toString();
                //output.setText(response);
            }
        });

        //attaching adapter to RecyclerView
        numberAdapter = new NumberAdapter(count, MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(numberAdapter);

        //listener to count number of lines in EditText
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                initial_line_count = final_line_count;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final_line_count = editText.getLineCount();
            }

            @Override
            public void afterTextChanged(Editable s) {
                    /*if (s.toString().contains("int")) {
                        String str = editText.getText().toString().replaceAll("int","<font color='red'>int</font>");
                        editText.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
                    }*/
                if (initial_line_count != final_line_count && initial_line_count < final_line_count) {
                    //adding number of lines to the list
                    count.add(String.valueOf(final_line_count));
                    //notifying the updates to the adapter
                    numberAdapter.notifyDataSetChanged();
                } else if (initial_line_count != final_line_count && initial_line_count > final_line_count) {
                    count.remove(String.valueOf(initial_line_count));
                    numberAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
