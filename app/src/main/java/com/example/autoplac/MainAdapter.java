package com.example.autoplac;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter <MainModel,MainAdapter.myViewHolder> {

    private boolean activate;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView")  int position, @NonNull MainModel model) {

        holder.description.setText(model.getDescription());
        holder.model.setText(model.getModel());
        holder.year.setText(model.getYear());
        holder.phone.setText(model.getPhone());
        Glide.with(holder.img.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);


        //pozovi metodu za prikazivanje i brisanje

        if (activate){
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }else
        {
            holder.btnEdit.setVisibility(View.INVISIBLE);
            holder.btnDelete.setVisibility(View.INVISIBLE);
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1450)
                        .create();


               // dialogPlus.show();

                View view=dialogPlus.getHolderView();

                EditText textModel =view.findViewById(R.id.txtModel);
                EditText year=view.findViewById(R.id.txtYear);
                EditText phone=view.findViewById(R.id.txtPhone);
                EditText description=view.findViewById(R.id.txtDescription);
                EditText image=view.findViewById(R.id.txtImage);
              Button btnUpdate=view.findViewById(R.id.btnUpdate);

                textModel.setText(model.getModel());
                year.setText(model.getYear());
                phone.setText(model.getPhone());
                description.setText(model.getDescription());
                image.setText(model.getImage());


                dialogPlus.show();


                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("model",textModel.getText().toString());
                        map.put("year",year.getText().toString());
                        map.put("phone",phone.getText().toString());
                        map.put("description",description.getText().toString());
                        map.put("image",image.getText().toString());


                        FirebaseDatabase.getInstance().getReference().child("vehicles")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.model.getContext(), "Data Updated Successfully.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.model.getContext(), "Error While Updating.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });





            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(holder.model .getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted data can't be undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("vehicles")
                                .child(getRef(position).getKey()).removeValue();

                    }
                });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(holder.model.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.show();
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView description,model,year,phone;
        Button btnEdit,btnDelete;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img=(CircleImageView)itemView.findViewById(R.id.img1);
            model=(TextView) itemView.findViewById(R.id.modeltext);
            description=(TextView) itemView.findViewById(R.id.descriptiontext);
            year=(TextView) itemView.findViewById(R.id.yeartext);
            phone=(TextView) itemView.findViewById(R.id.phonetext);
            btnEdit=(Button) itemView.findViewById(R.id.btnEdit);
            btnDelete=(Button) itemView.findViewById(R.id.btnDelete);
        }
    }
    public void activateButtons(boolean activate){
        this.activate = activate;
        notifyDataSetChanged();
    }

}
