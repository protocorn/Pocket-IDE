package com.example.pocketide;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketide.adapter.NumberAdapter;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> countlist;
    NumberAdapter numberAdapter;
    int initial_line_count = 1, final_line_count;
    String filename;
    Button compile,ClearScreen;
    EditText editText;
    TextView output, FileName;
    String lang="",path_name="",version="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        filename = getIntent().getStringExtra("filename");

        compile = findViewById(R.id.compile);
        output = findViewById(R.id.output);
        FileName = findViewById(R.id.file_nm);
        ClearScreen = findViewById(R.id.clr_scr);

        FileName.setText(filename);

        if (filename.contains(".java")) {
            lang = "java";
            path_name="JavaPrograms";
            version="1";
        } else if (filename.contains(".cpp")) {
            lang = "cpp";
            path_name="C++Programs";
            version="2";
        } else {
            lang = "c";
            path_name="CPrograms";
            version="2";
        }

        countlist = new ArrayList<>();

        RecyclerView recyclerView;
        editText = findViewById(R.id.editTextTextMultiLine);
        editText.setLineSpacing(0, 1.17f); //spacing to align with recyclerview

        recyclerView = findViewById(R.id.recyclerView);


        String line2;
        String path = Environment.getExternalStorageDirectory().toString();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path + "/PocketIDE/"+path_name+"/" + filename));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            int c_line=0;

            while ((line2 = bufferedReader.readLine()) != null) {
                if(c_line==0){
                    stringBuilder.append(line2);
                    c_line=1;
                }
                else {
                    stringBuilder.append("\n" + line2);
                }
            }
            fileInputStream.close();
            line2 = stringBuilder.toString();
            editText.setText(line2);
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }

        editText.post(new Runnable() {
            @Override
            public void run() {
                int counter = 1;
                for (int i = 0; i < editText.getLineCount(); i++) {
                    countlist.add(String.valueOf(counter));
                    counter++;
                    numberAdapter.notifyDataSetChanged();
                }
            }
        });


        compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileWriter fWriter = new FileWriter(path + "/PocketIDE/"+path_name+"/" + filename);
                    fWriter.append(editText.getText().toString());

                    // Close the file writer object
                    fWriter.close();

                    // Display message to be printed on the console
                    Toast.makeText(MainActivity.this, "File Saved", Toast.LENGTH_SHORT).show();
                }

                // Catch block to handle if exception occurs
                catch (IOException e) {
                    // Print the exception
                    System.out.print(e.getMessage());
                }
                ProgressDialog pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("Compiling...");
                pd.show();

                //API request to compile the code
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            try {
                                String initial_script = editText.getText().toString();
                                String clientId = "1d9915a673520f1acdc941be6d0fbf38"; //Replace with your client ID
                                String clientSecret = "59e56a3021e82d94a8667712b3e597ab4d141fde197df2073e52b27286de32fb"; //Replace with your client Secret
                                String final_script = initial_script.replaceAll("\n", " ");
                                String final_script1 =final_script.replaceAll("\"","\\\\"+"\"");
                                String script = final_script1;
                                String language = lang;
                                String versionIndex = version;
                                URL url = new URL("https://api.jdoodle.com/v1/execute/");
                                URLConnection connection = url.openConnection();
                                HttpURLConnection http = (HttpURLConnection) connection;
                                http.setDoOutput(true);
                                http.setRequestMethod("POST");
                                connection.setRequestProperty("Content-Type", "application/json");
                                String input = "{\"clientId\": \"" + clientId + "\",\"clientSecret\":\"" + clientSecret + "\",\"script\":\"" + script +
                                        "\",\"language\":\"" + language + "\",\"versionIndex\":\"" + versionIndex + "\"} ";
                                System.out.println(input);
                                OutputStream outputStream = connection.getOutputStream();
                                outputStream.write(input.getBytes());
                                outputStream.flush();
                                if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                    throw new RuntimeException("Please check your inputs : HTTP error code : " + http.getResponseCode());
                                }
                                BufferedReader bufferedReader;
                                bufferedReader = new BufferedReader(new InputStreamReader(
                                        (http.getInputStream())));
                                String output1;
                                System.out.println("Output from JDoodle .... \n");
                                while ((output1 = bufferedReader.readLine()) != null) {
                                    output.setText(output1);
                                    pd.dismiss();
                                }
                                http.disconnect();
                            } catch (IOException e) {
                                e.printStackTrace();
                                pd.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            pd.dismiss();
                        }
                    }
                });
                thread.start();
            }
        });

        //attaching adapter to RecyclerView
        numberAdapter = new NumberAdapter(countlist, MainActivity.this);
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
                    countlist.add(String.valueOf(final_line_count));
                    //notifying the updates to the adapter
                    numberAdapter.notifyDataSetChanged();
                } else if (initial_line_count != final_line_count && initial_line_count > final_line_count) {
                    countlist.remove(String.valueOf(initial_line_count));
                    numberAdapter.notifyDataSetChanged();
                }
            }
        });

        ClearScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output.setText("");
            }
        });
    }
}