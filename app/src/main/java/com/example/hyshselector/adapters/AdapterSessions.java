package com.example.hyshselector.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hyshselector.MainActivity;
import com.example.hyshselector.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterSessions extends RecyclerView.Adapter<AdapterSessions.MyViewHolder> {


    private Context context;
    private List<String> listString;

    public AdapterSessions(Context context, List<String> listString) {
        this.context = context;
        this.listString = listString;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sessions, viewGroup, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.textSelection.setText(listString.get(position));

        holder.textSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("session_name", listString.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listString.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_selection)
        TextView textSelection;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }


}
