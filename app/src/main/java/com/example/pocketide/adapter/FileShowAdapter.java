package com.example.pocketide.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketide.MainActivity;
import com.example.pocketide.R;

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
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("filename", list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView filename, date1;
        LinearLayout linearLayout;

        public viewholder(@NonNull View itemView) {
            super(itemView);
           filename = itemView.findViewById(R.id.file_name);
           date1 = itemView.findViewById(R.id.date);
           linearLayout = itemView.findViewById(R.id.file_on_click);


        }
    }
}

