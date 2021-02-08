package com.genedev.sqlitesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btn_save, btn_retrieve;
    private ListView lv_names;
    private EditText ed_name;

    private SQLiteDatabaseHandler db;

    private Names names;

    final int min = 100;
    final int max = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_retrieve = findViewById(R.id.btn_retrieve);
        btn_save = findViewById(R.id.btn_save);
        ed_name = findViewById(R.id.ed_name);
        lv_names = findViewById(R.id.lv_names);

        db = new SQLiteDatabaseHandler(this);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int random = new Random().nextInt((max - min) + 1) + min;
                String name = ed_name.getText().toString();

                names = new Names(random, name);
                db.addPlayer(names);

                ed_name.setText("");
            }
        });

        btn_retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Names> names = db.allNames();

                if (names != null) {
                    String[] itemsNames = new String[names.size()];

                    for (int i = 0; i < names.size(); i++) {
                        itemsNames[i] = names.get(i).toString();
                    }

                    // display like string instances
                    lv_names.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, itemsNames));

                }
            }
        });
    }
}
