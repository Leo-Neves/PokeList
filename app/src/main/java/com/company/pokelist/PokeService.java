package com.company.pokelist;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                            List<Pokemon> pokemons = new ArrayList<>();
                            for (int i=0; i<pokemonsArray.length(); i++){
                                JSONObject pokemonJson = pokemonsArray.getJSONObject(i);
                                Pokemon pokemon = converterJsonParaPokemon(pokemonJson);
                                pokemons.add(pokemon);
                            }
                            pokemonListener.baixados(pokemons);
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

    private Pokemon converterJsonParaPokemon(JSONObject pokemonJson) throws JSONException{
        Pokemon pokemon = new Pokemon();
        String url = pokemonJson.getString("url");
        String nome = pokemonJson.getString("name");
        int id = getIdPokemon(url);
        String urlImagem = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+id+".png";
        pokemon.setNome(nome);
        pokemon.setUrl(url);
        pokemon.setId(id);
        pokemon.setImagem_url(urlImagem);
        return pokemon;
    }

    private int getIdPokemon(String url){
        Uri uri = Uri.parse(url);
        String id = uri.getLastPathSegment();
        return Integer.parseInt(id);
    }

    public interface PokemonListener{
        void baixados(List<Pokemon> pokemons);
        void erro(String erro);
    }

}
