package com.company.pokelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PokeService.PokemonListener {
    private Button buttonSincronizar;
    private TextView textoPokemon;
    private PokeService pokeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pokeService = new PokeService();
        textoPokemon = findViewById(R.id.textoPokemon);
        buttonSincronizar = findViewById(R.id.buttonSincronizar);
        buttonSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pokeService.baixarPokemons(MainActivity.this, MainActivity.this);
            }
        });
    }

    @Override
    public void baixados(List<String> nomesPokemons) {
        String nomes = "";
        for (String nome : nomesPokemons){
            nomes += nome + "\n";
        }
        textoPokemon.setText(nomes);
    }

    @Override
    public void erro(String erro) {
        textoPokemon.setText(erro);
    }
}
