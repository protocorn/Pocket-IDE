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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.pocketide.Models.FileModel;
import com.example.pocketide.adapter.FileShowAdapter;
import com.example.pocketide.adapter.FragmentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class file_saver extends AppCompatActivity {
    FloatingActionButton add;
    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_saver);
        add=findViewById(R.id.add_file);
        viewPager=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);


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
                               File folder = new File(Environment.getExternalStorageDirectory().toString() + "/PocketIDE/CPrograms");
                               folder.mkdirs();

                               String extStorageDirectory = folder.toString();

                               File file = new File(extStorageDirectory, filename + ".c");
                               try {
                                   file.createNewFile();
                                   Toast.makeText(file_saver.this, "New C File was Created Successfully", Toast.LENGTH_SHORT).show();
                                   dialog.dismiss();
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           } else if (java.isChecked()) {
                               String path=Environment.getExternalStorageDirectory().toString();
                               File folder = new File(path + "/PocketIDE/JavaPrograms");
                               folder.mkdirs();

                               String extStorageDirectory = folder.toString();

                               File file = new File(extStorageDirectory, filename + ".java");

                               try {
                                   file.createNewFile();
                                   Toast.makeText(file_saver.this, "New Java File was Created Successfully", Toast.LENGTH_SHORT).show();
                                   dialog.dismiss();
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           } else if (cpp.isChecked()) {
                               File folder = new File(Environment.getExternalStorageDirectory().toString() + "/PocketIDE/C++Programs");
                               folder.mkdirs();

                               String extStorageDirectory = folder.toString();

                               File file = new File(extStorageDirectory, filename + ".cpp");
                               try {
                                   file.createNewFile();
                                   Toast.makeText(file_saver.this, "New Cpp File was Created Successfully", Toast.LENGTH_SHORT).show();
                                   dialog.dismiss();
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