package com.company.pokelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PokeService.PokemonListener {
    private Button buttonSincronizar;
    private RecyclerView recyclerView;
    private PokeService pokeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        pokeService = new PokeService(this);
        recyclerView = findViewById(R.id.recyclerViewPokemons);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        buttonSincronizar = findViewById(R.id.buttonSincronizar);
        buttonSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSincronizar.setVisibility(View.GONE);
                pokeService.baixarPokemons( MainActivity.this);
            }
        });
    }

    @Override
    public void baixados(List<Pokemon> pokemons) {
        PokemonAdapter pokemonAdapter = new PokemonAdapter(this, pokemons, pokeService);
        recyclerView.setAdapter(pokemonAdapter);
    }

    @Override
    public void erro(String erro) {
        Toast toast = Toast.makeText(this, erro, Toast.LENGTH_LONG);
        toast.show();
        buttonSincronizar.setVisibility(View.VISIBLE);
    }
}
