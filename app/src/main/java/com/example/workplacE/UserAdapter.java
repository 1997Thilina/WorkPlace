package com.example.workplacE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    ArrayList<User> mList;
    Context context;

    public UserAdapter(Context context , ArrayList<User> mList){

        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_display , parent ,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = mList.get(position);

        holder.jobTitle.setText(user.getEmployment());
        holder.proName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView jobTitle, proName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            proName = itemView.findViewById(R.id.yourName);
            jobTitle = itemView.findViewById(R.id.jobTitle);
        }

    }
}