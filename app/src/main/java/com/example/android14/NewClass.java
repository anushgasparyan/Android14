package com.example.android14;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewClass extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView desc;
    public LinearLayout root;
    public NewClass(@NonNull View itemView) {
        super(itemView);
        this.root = itemView.findViewById(R.id.liners);
        this.title = itemView.findViewById(R.id.text1);
        this.desc = itemView.findViewById(R.id.text2);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setDesc(String desc) {
        this.desc.setText(desc);
    }
}
