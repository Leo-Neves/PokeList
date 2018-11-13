package com.company.pokelist;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PokeService {

    public void baixarPokemons(Context context, final PokemonListener pokemonListener){
        //O código de conexão com a PokeAPI começa aqui
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://pokeapi.co/api/v2/pokemon/";

        //Preparar uma requisição para a URL da PokeAPI
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray pokemonsArray = json.getJSONArray("results");
                            List<String> nomesPokemons = new ArrayList<>();
                            for (int i=0; i<pokemonsArray.length(); i++){
                                JSONObject pokemonJson = pokemonsArray.getJSONObject(i);
                                nomesPokemons.add(pokemonJson.getString("name"));
                            }
                            pokemonListener.baixados(nomesPokemons);
                        }catch (JSONException e){
                            e.printStackTrace();
                            pokemonListener.erro("Não foi possível verificar a resposta do servidor");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pokemonListener.erro("Não foi possível estabelecer conexão");
            }
        });

        //Executar a requicição HTTP
        queue.add(stringRequest);
    }

    public interface PokemonListener{
        void baixados(List<String> nomesPokemons);
        void erro(String erro);
    }

}
