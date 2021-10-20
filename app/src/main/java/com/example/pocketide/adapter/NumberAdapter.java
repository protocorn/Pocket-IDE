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

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.viewholder> {
    Context context;
    List<String> list;

    public NumberAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public NumberAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.number, parent, false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberAdapter.viewholder holder, int position) {
        holder.numbers.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView numbers;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            numbers = itemView.findViewById(R.id.num);
        }
    }
}