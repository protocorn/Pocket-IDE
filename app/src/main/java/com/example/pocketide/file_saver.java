package com.example.pocketide;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;

public class file_saver extends AppCompatActivity {
    FloatingActionButton add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_saver);
        add=findViewById(R.id.add_file);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(file_saver.this);
                dialog.setContentView(R.layout.save_file);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                EditText file_name;
                Button cancel, save;
                RadioButton c,cpp,java;
                file_name = dialog.findViewById(R.id.txt_input);
                cancel = dialog.findViewById(R.id.btn_cancel);
                save = dialog.findViewById(R.id.btn_okay);
                c = dialog.findViewById(R.id.cprogram);
                cpp = dialog.findViewById(R.id.cpp);
                java = dialog.findViewById(R.id.java_p);
               save.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       String filename = file_name.getText().toString();
                       if (filename.isEmpty()) {
                           Toast.makeText(file_saver.this, "Filename cannot be empty", Toast.LENGTH_SHORT).show();
                       } else {
                           if (c.isChecked()) {
                               File folder = new File(Environment.getExternalStorageDirectory().toString() + "/PocketIDE/C programs");
                               folder.mkdirs();

                               //Save the path as a string value
                               String extStorageDirectory = folder.toString();

                               //Create New file and name it Image2.PNG
                               File file = new File(extStorageDirectory, filename + ".c");
                               try {
                                   file.createNewFile();
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           } else if (java.isChecked()) {
                               File folder = new File(Environment.getExternalStorageDirectory().toString() + "/PocketIDE/Java programs");
                               folder.mkdirs();

                               //Save the path as a string value
                               String extStorageDirectory = folder.toString();

                               //Create New file and name it Image2.PNG
                               File file = new File(extStorageDirectory, filename + ".java");
                               try {
                                   file.createNewFile();
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           } else if (cpp.isChecked()) {
                               File folder = new File(Environment.getExternalStorageDirectory().toString() + "/PocketIDE/C++ programs");
                               folder.mkdirs();

                               //Save the path as a string value
                               String extStorageDirectory = folder.toString();

                               //Create New file and name it Image2.PNG
                               File file = new File(extStorageDirectory, filename + ".cpp");
                               try {
                                   file.createNewFile();
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           } else {
                               Toast.makeText(file_saver.this, "please select anyone language", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }
               });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

}