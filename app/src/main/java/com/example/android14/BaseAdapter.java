package com.example.android14;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseAdapter extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView desc;
    public LinearLayout root;
    public Button clear, edit;
    public BaseAdapter(@NonNull View itemView) {
        super(itemView);
        this.root = itemView.findViewById(R.id.liners);
        this.title = itemView.findViewById(R.id.text1);
        this.desc = itemView.findViewById(R.id.text2);
        this.clear = itemView.findViewById(R.id.clear);
        this.edit = itemView.findViewById(R.id.edit);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setDesc(String desc) {
        this.desc.setText(desc);
    }
}
