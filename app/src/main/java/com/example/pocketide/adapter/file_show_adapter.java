package com.example.pocketide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketide.R;

import java.util.List;

public class file_show_adapter extends RecyclerView.Adapter<file_show_adapter.viewholder> {
    Context context;
    List<String> list;

    public file_show_adapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public file_show_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_files, parent, false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull file_show_adapter.viewholder holder, int position) {

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

