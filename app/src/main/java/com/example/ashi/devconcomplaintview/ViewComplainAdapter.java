package com.example.ashi.devconcomplaintview;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ViewComplainAdapter extends RecyclerView.Adapter {
    List<Complaint> dataModels;
    public ViewComplainAdapter(List<Complaint> dataModels)
    {
        this.dataModels=dataModels;

    }
    class ComplainViewHolder extends RecyclerView.ViewHolder{
        TextView status,category,description;
        ImageView image;
        Button navigate,change_status;
        ProgressBar progressBar;
        public ComplainViewHolder(@NonNull View itemView) {
            super(itemView);
            status=itemView.findViewById(R.id.status);
            category=itemView.findViewById(R.id.category);
            description=itemView.findViewById(R.id.description);
            image=itemView.findViewById(R.id.image);
            navigate=itemView.findViewById(R.id.navigate);
            change_status=itemView.findViewById(R.id.change_status);
            progressBar=itemView.findViewById(R.id.progressBar);


        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_complain_item, viewGroup, false);
                return new ComplainViewHolder(view) ;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
                final ComplainViewHolder complainViewHolder=(ComplainViewHolder) viewHolder;
                Glide.with(complainViewHolder.itemView.getContext())
                        .load(dataModels.get(position).getImage())
                        .into(complainViewHolder.image);
                complainViewHolder.status.setText(dataModels.get(position).getStatus());
                complainViewHolder.description.setText(dataModels.get(position).getDescription());
                complainViewHolder.category.setText(dataModels.get(position).getCategory());
        complainViewHolder.navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location=new Location();
                location.setLat(Double.parseDouble(dataModels.get(position).getLati()));
                location.setLng(Double.parseDouble(dataModels.get(position).getLongi()));
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+location.getLat()+","+location.getLng());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(complainViewHolder.itemView.getContext().getPackageManager()) != null) {
                    complainViewHolder.itemView.getContext().startActivity(mapIntent);
                }
            }
        });
        complainViewHolder.change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(complainViewHolder.itemView.getContext());
                alertDialog.setTitle("Status");
                alertDialog.setMessage("Enter Status");
                final EditText input = new EditText(complainViewHolder.itemView.getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Change Status", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        complainViewHolder.progressBar.setVisibility(View.VISIBLE);
                        if(input.getText().toString().equals(""))
                        {
                            Toast.makeText(complainViewHolder.itemView.getContext(), "Enter Status", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dataModels.get(position).setStatus(input.getText().toString());
                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Complaint");
                            databaseReference.child(dataModels.get(position).getKey()).setValue(dataModels.get(position)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    complainViewHolder.progressBar.setVisibility(View.GONE);
                                    notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
        }


    @Override
    public int getItemCount() {
        return dataModels.size();
    }
}
