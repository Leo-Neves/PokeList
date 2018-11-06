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

        //O código de conexão com a PokeAPI começa aqui
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://pokeapi.co/api/v2/pokemon/";

        //Preparar uma requisição para a URL da PokeAPI
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Exibindo a resposta da PokeAPI na tela
                        textoPokemon.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Caso haja algum problema, exibir mensagem de erro
                textoPokemon.setText("Ocorreu um erro!");
            }
        });

        //Executar a requicição HTTP
        queue.add(stringRequest);
    }

}
