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
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketide.adapter.NumberAdapter;
import com.example.pocketide.backgroundTasks.ActivityTask;
import com.example.pocketide.backgroundTasks.GccTask;
import com.example.pocketide.compileAndRun.GccCompiler;
import com.example.pocketide.project.Project;
import com.google.android.material.navigation.NavigationView;

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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> countlist;
    NumberAdapter numberAdapter;
    int initial_line_count = 1, final_line_count;
    String filename;

    Button compile;
    EditText editText;
    TextView output;


    private Project selectedProject;

    private boolean isLoadingDialog = false;
    // private FileBrowserDialog fileBrowserDialog;
    private GccCompiler gccCompiler;
    private ActivityTask activityStartTask;
    private GccTask compileTask;
    private boolean compiled = false;
    private SharedPreferences sharedPreferences;
    private String compileParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        filename = getIntent().getStringExtra("filename");

        compile = findViewById(R.id.compile);
        output = findViewById(R.id.output);

        countlist = new ArrayList<>();

        RecyclerView recyclerView;
        editText = findViewById(R.id.editTextTextMultiLine);
        editText.setLineSpacing(0, 1.17f); //spacing to align with recyclerview

        recyclerView = findViewById(R.id.recyclerView);




        String line2;
        String path = Environment.getExternalStorageDirectory().toString();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path + "/PocketIDE/CPrograms/" + filename));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((line2 = bufferedReader.readLine()) != null) {
                stringBuilder.append(line2);
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
                    FileWriter fWriter = new FileWriter(path + "/PocketIDE/CPrograms/" + filename);
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
                pd.setMessage("Compile");
                compileTask = new GccTask(pd);
                compileTask.setOnTaskStarted((o) -> compileTask());
                compileTask.setOnPostTask(this::compileTaskResult);
                compileTask.setOnUpdateTask(this::compileProgress);
                compileTask.execute();

                //API request to compile the code
               /* Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            try {
                                String clientId = "af52e5b3d644630c2d81bade4206c055"; //Replace with your client ID
                                String clientSecret = "e868414f5565d10183267d398d578a448c6a1a70bf8be2896ebc208388b013bc"; //Replace with your client Secret
                                String script = editText.getText().toString();
                                String language = "java";
                                String versionIndex = "0";
                                URL url = new URL("https://api.jdoodle.com/v1/execute");
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setDoOutput(true);
                                connection.setRequestMethod("POST");
                                connection.setRequestProperty("Content-Type", "application/json");
                                String input = "{\"clientId\": \"" + clientId + "\",\"clientSecret\":\"" + clientSecret + "\",\"script\":\"" + script +
                                        "\",\"language\":\"" + language + "\",\"versionIndex\":\"" + versionIndex + "\"} ";
                                System.out.println(input);
                                OutputStream outputStream = connection.getOutputStream();
                                outputStream.write(input.getBytes());
                                outputStream.flush();
                                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                    throw new RuntimeException("Please check your inputs : HTTP error code : " + connection.getResponseCode());
                                }
                                BufferedReader bufferedReader;
                                bufferedReader = new BufferedReader(new InputStreamReader(
                                        (connection.getInputStream())));
                                StringBuilder results = new StringBuilder();
                                String output1;
                                System.out.println("Output from JDoodle .... \n");
                                while ((output1 = bufferedReader.readLine()) != null) {
                                    results.append(output1);
                                }
                                connection.disconnect();
                                output.setText(results);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();*/
            }

            private void compileProgress(Object result) {
                Object[] objs = (Object[]) result;
                double endTime = (double) objs[1];
                String compileMsg = (String) objs[0];
                String msg = output.getText().toString() + compileMsg + "Task ends in " + Double.toString(endTime) + "s";
                output.setText(msg);
            }

            private void compileTaskResult(Object... o) {
                String msg = output.getText().toString() + o[0];
                output.setText(msg);
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
    }

    private Object compileTask() {
        gccCompiler.setCompileParams(compileParams);
        compileTask.updateProgress("Linking source...\n");
        long startTime = 0;
        StringBuilder builder = new StringBuilder();
        boolean success = false;
        try {
            Thread.sleep(500);
            compileTask.updateProgress("Starting compile...\n");
            Thread.sleep(500);
            startTime = System.currentTimeMillis();
            Object[] objs = gccCompiler.compile(selectedProject);
            String stdout = (String) objs[1];
            String stderr = (String) objs[2];
            if ((boolean) objs[0]) {
                success = true;
                if (!stdout.isEmpty()) {
                    builder.append("Compiled with warnings: ").append(stdout).append("\n");
                } else {
                    builder.append("Compiled successfully.\n");
                }
            } else {
                builder.append("Compile error: ").append(stderr).append("\n");
            }
        } catch (Exception e) {
            String err = e.getMessage();
            builder.append("Unknown error: ").append(err).append("\n");
        }
        long endTime = System.currentTimeMillis();
        return new Object[]{builder.toString(), (endTime - startTime) / 1000.0, success};
    }

    private void checkCompiler() {
        try {
            gccCompiler = new GccCompiler(this);
        } catch (IOException e) {
            // Toast(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT, true);
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Object onStartTask() {
        checkCompiler();
        return null;
    }
    //  private void compileTaskResult(Object result)

    /*
     * Show GCC output while compiling in UI thread
     */
    // private void compileProgress(Object... o)



}