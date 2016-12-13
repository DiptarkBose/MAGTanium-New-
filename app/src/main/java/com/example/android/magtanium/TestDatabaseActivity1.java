package com.example.android.magtanium;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class TestDatabaseActivity1 extends ListActivity {
    private CommentsDataSource1 datasource1;

    String mov = null;

    Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database1);
        Intent i = getIntent();
        mov = i.getStringExtra("movie_name");
        btn = (Button) findViewById(R.id.add);
        datasource1 = new CommentsDataSource1(this);
        datasource1.open();

        List<Comment> values = datasource1.getAllComments();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
        Comment comment = null;

        switch (view.getId()) {
            case R.id.add:
                Toast.makeText(this, "Movie added to this list!!", Toast.LENGTH_SHORT).show();
                comment = datasource1.createComment(mov);
                adapter.add(comment);
                finish();
                break;

        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource1.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource1.close();
        super.onPause();
    }
}

