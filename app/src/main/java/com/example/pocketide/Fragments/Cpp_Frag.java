package com.example.pocketide.Fragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketide.R;
import com.example.pocketide.adapter.FileShowAdapter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Cpp_Frag extends Fragment {

    FileShowAdapter fileShowAdapter;
    RecyclerView recyclerView;
    List<String> list;
    List<String> datelist;


    public Cpp_Frag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cpp_, container, false);
        recyclerView=view.findViewById(R.id.cpp_list);

        list=new ArrayList<>();
        datelist=new ArrayList<>();

        fileShowAdapter=new FileShowAdapter(list,datelist,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(fileShowAdapter);

        File path = Environment.getExternalStorageDirectory();
        File yourDir = new File(path, "/PocketIDE/C++Programs");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        for (File f : yourDir.listFiles()) {
            if (f.isFile()) {
                String name = f.getName();
                long date_milli= f.lastModified();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(date_milli);
                String date=formatter.format(calendar.getTime());
                list.add(name);
                datelist.add(date);
                fileShowAdapter.notifyDataSetChanged();
            }
        }
        // Inflate the layout for this fragment
        return view;
    }
}