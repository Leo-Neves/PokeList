package com.company.pokelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button buttonSincronizar;
    private TextView textoPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textoPokemon = findViewById(R.id.textoPokemon);
        textoPokemon.setVisibility(View.GONE);
        buttonSincronizar = findViewById(R.id.buttonSincronizar);
        buttonSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textoPokemon.setVisibility(View.VISIBLE);
            }
        });
    }
}
