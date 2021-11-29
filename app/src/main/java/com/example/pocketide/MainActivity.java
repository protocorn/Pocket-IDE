package com.example.pocketide;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ClientProtocolException;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.entity.UrlEncodedFormEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

    private void setText(final TextView text,final String value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

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


        String line2 = null;
        String path = Environment.getExternalStorageDirectory().toString();
        try {
            FileInputStream fileInputStream = new FileInputStream (new File(path +"/PocketIDE/JavaPrograms/"+ filename));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line2 = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line2);
            }
            fileInputStream.close();
            line2 = stringBuilder.toString();
            editText.setText(line2);
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }

        editText.post(new Runnable() {
            @Override
            public void run() {
                int counter=1;
                for(int i=0;i<editText.getLineCount();i++) {
                    countlist.add(String.valueOf(counter));
                    counter++;
                    numberAdapter.notifyDataSetChanged();
                }
            }
        });

        compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try  {
                            String code = editText.getText().toString();
                            String url = "http://45.79.179.111/java-android/compile_android.php";
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost(url);
                            // Add your data
                            List<NameValuePair> nameValuePairs = new ArrayList< NameValuePair >(5);
                            nameValuePairs.add(new BasicNameValuePair("source", code));
                            nameValuePairs.add(new BasicNameValuePair("input", "0"));

                            try {
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                Log.d("myapp", "works till here. 2");
                                try {
                                    HttpResponse response = httpclient.execute(httppost);
                                    String responseBody = EntityUtils.toString(response.getEntity());
                                    TextView txtOutput=findViewById(R.id.output);
                                    setText(txtOutput,responseBody);
                                    Log.d("myapp", "response " + responseBody);
                                } catch (ClientProtocolException e) {
                                    e.printStackTrace();//find output label by id
                                    setText(output,e.toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    setText(output,e.toString());
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                setText(output,e.toString());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            output.setText(e.toString());
                        }
                    }
                });

                thread.start();
                //disable button and modify color
                compile.setClickable(false);
                compile.setBackgroundColor(Color.GRAY);

                //timer for 5s delay and enable button
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        compile.setClickable(true);
                        compile.setBackgroundResource(android.R.drawable.btn_default);
                    }
                }, 5000);
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
}
