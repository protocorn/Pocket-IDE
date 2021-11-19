package com.example.pocketide;

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

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        compile=findViewById(R.id.compile);
        output=findViewById(R.id.output);

        count = new ArrayList<>();

        RecyclerView recyclerView;
        editText = findViewById(R.id.editTextTextMultiLine);
        editText.setLineSpacing(0, 1.17f); //spacing to align with recyclerview

        recyclerView = findViewById(R.id.recyclerView);

        compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execute(new String[]{editText.getText().toString()}, new String[]{""});
                try {
                    File dir = Environment.getExternalStorageDirectory();
                    String path = dir.getAbsolutePath();
                    Process su = Runtime.getRuntime().exec(path+"/MyClass.java");
                    Toast.makeText(MainActivity.this, "h", Toast.LENGTH_SHORT).show();
                    output.setText(su.toString());
                    DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                    outputStream.writeBytes("screenrecord --time-limit 10 /sdcard/MyVideo.mp4\n");
                    outputStream.flush();

                    outputStream.writeBytes("exit\n");
                    outputStream.flush();
                    su.waitFor();
                }
                catch(IOException | InterruptedException e){
                    try {
                        throw new Exception(e);
                    } catch (Exception exception) {
                        output.setText(e.toString());
                        exception.printStackTrace();
                    }
                }

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

               /* if (editText.getText().toString().contains("int") == true) {
                    String str = editText.getText().toString().replaceAll("int", "<font color='red'>int</font>");

                    editText.setText(Html.fromHtml(str), TextView.BufferType.SPANNABLE);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {
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

    public static void execute(String[] commands, String[] headers) {

        try {
            FileWriter fstream = new FileWriter("Example.java");
            BufferedWriter out = new BufferedWriter(fstream);

            out.write("");

            for (String header : headers) out.append(header);

            out.append("class Example { public static void main(String args[]) { ");

            for (String cmd : commands) out.append(cmd);

            out.append(" } }");

            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // set path, compile, & run
        try {
            Process tr = Runtime.getRuntime().exec(
                    new String[]{"java -cp .",
                            "javac Example.java",
                            "java Example"});
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
}
