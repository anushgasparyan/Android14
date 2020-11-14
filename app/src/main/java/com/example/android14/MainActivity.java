package com.example.android14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    EditText editText1, editText2;
    Button button;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    AlertDialog.Builder builder;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        recyclerView = findViewById(R.id.recycleView);
        builder = new AlertDialog.Builder(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        button = findViewById(R.id.button);
        Picasso.get()
                .load("https://static.toiimg.com/photo/72975551.cms")
                .into(imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = String.valueOf(editText1.getText());
                String description = String.valueOf(editText2.getText());
                editText1.setText("");
                editText2.setText("");
                databaseReference = FirebaseDatabase.getInstance().getReference("books").push();
                Model model = new Model();
                model.setId(databaseReference.getKey());
                model.setTitle(title);
                model.setDesc(description);
                databaseReference.setValue(model);
            }
        });
        show();
    }

    private void show() {
        Query query = FirebaseDatabase.getInstance().getReference().child("books");
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, new SnapshotParser<Model>() {
                            @NonNull
                            @Override
                            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Model(snapshot.child("id").getValue().toString(),
                                        snapshot.child("title").getValue().toString(),
                                        snapshot.child("desc").getValue().toString());
                            }
                        })
                        .build();
        adapter = new FirebaseRecyclerAdapter<Model, BaseAdapter>(options) {
            @Override
            public BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter, parent, false);
                return new BaseAdapter(view);
            }

            @Override
            protected void onBindViewHolder(final BaseAdapter holder, final int position, final Model model) {
                holder.setDesc(model.getDesc());
                holder.setTitle(model.getTitle());

                holder.clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference().child("books").child(model.getId()).removeValue();
                    }
                });
                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getLayoutInflater().inflate(R.layout.alert, null);
                        final EditText newTitle = view.findViewById(R.id.newtitle);
                        final EditText newDesc = view.findViewById(R.id.newdesc);
                        newTitle.setText(model.getTitle());
                        newDesc.setText(model.getDesc());
                        builder.setTitle("Edit the title and the description")
                                .setView(view)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String s1 = String.valueOf(newTitle.getText());
                                        String s2 = String.valueOf(newDesc.getText());
                                        FirebaseDatabase.getInstance().getReference().child("books").child(model.getId()).setValue(new Model(model.getId(), s1, s2));
                                        dialog.cancel();
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

//https://medium.com/android-grid/how-to-use-firebaserecycleradpater-with-latest-firebase-dependencies-in-android-aff7a33adb8b