package com.company.pokelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PokeService.PokemonListener {
    private Button buttonSincronizar;
    private RecyclerView recyclerView;
    private PokeService pokeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pokeService = new PokeService();
        recyclerView = findViewById(R.id.recyclerViewPokemons);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        buttonSincronizar = findViewById(R.id.buttonSincronizar);
        buttonSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSincronizar.setVisibility(View.GONE);
                pokeService.baixarPokemons(MainActivity.this, MainActivity.this);
            }
        });
    }

    @Override
    public void baixados(List<Pokemon> pokemons) {
        PokemonAdapter pokemonAdapter = new PokemonAdapter(this, pokemons);
        recyclerView.setAdapter(pokemonAdapter);
    }

    @Override
    public void erro(String erro) {
        Toast toast = Toast.makeText(this, erro, Toast.LENGTH_LONG);
        toast.show();
        buttonSincronizar.setVisibility(View.VISIBLE);
    }
}
