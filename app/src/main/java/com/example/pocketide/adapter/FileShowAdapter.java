package com.example.pocketide.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketide.Models.FileModel;
import com.example.pocketide.R;

import java.io.File;
import java.util.List;

public class FileShowAdapter extends RecyclerView.Adapter<FileShowAdapter.viewholder> {
    Context context;
    List<String> list;
    List<String> date;

    public FileShowAdapter(List<String> list,List<String> date, Context context) {
        this.list = list;
        this.context = context;
        this.date=date;
    }

    @NonNull
    @Override
    public FileShowAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_files, parent, false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileShowAdapter.viewholder holder, int position) {
        holder.filename.setText(list.get(position));
        holder.date1.setText(date.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView filename, date1;

        public viewholder(@NonNull View itemView) {
            super(itemView);
           filename = itemView.findViewById(R.id.file_name);
           date1 = itemView.findViewById(R.id.date);
        }
    }
}

