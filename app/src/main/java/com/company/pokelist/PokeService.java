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
import com.company.pokelist.utils.AssetsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PokeService {
    private Context context;
    private RequestQueue queue;

    public PokeService(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void baixarDetalhesDePokemon(final Pokemon pokemon, final PokemonDetalhesListener pokemonDetalhesListener){
        String url = pokemon.getUrl();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject json = new JSONObject(response);
                    pokemon.setOrdem(json.optInt("order"));
                    pokemon.setPeso(json.optInt("weight") / 10.0);
                    pokemon.setAltura(json.optInt("height") * 10);
                    pokemon.setBase_exp(json.optInt("base_experience"));
                    pokemon.setTipos(obterTiposDoPokemon(json));
                    pokemon.setOutras_imagens_url(obterImagensDoPokemon(json));
                    pokemonDetalhesListener.baixado(pokemon);
                }catch (JSONException e){
                    e.printStackTrace();
                    pokemonDetalhesListener.erro("Não foi possível obter dados do "+pokemon.getNome());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pokemonDetalhesListener.erro("Não foi possível obter dados do "+pokemon.getNome());
            }
        });
        queue.add(stringRequest);
    }

    public void baixarPokemons(final PokemonListener pokemonListener){
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

    private List<String> obterTiposDoPokemon(JSONObject json) throws JSONException{
        JSONObject tiposPortugues = AssetsUtils.getJSONObjectFromAssets(context, "types.json");
        List<String> tipos = new ArrayList<>();
        JSONArray tiposJson = json.getJSONArray("types");
        for (int i=0; i<tiposJson.length();i++){
            JSONObject tipoJson = tiposJson.getJSONObject(i);
            String tipoIngles = tipoJson.getJSONObject("type").optString("name");
            String tipoPortugues = tiposPortugues.optString(tipoIngles, tipoIngles);
            tipos.add(tipoPortugues);
        }
        return tipos;
    }

    private List<String> obterImagensDoPokemon(JSONObject json) throws JSONException{
        List<String> imagensUrl = new ArrayList<>();
        JSONObject sprites = json.getJSONObject("sprites");
        if (sprites.optString("back_default", null) != null)
            imagensUrl.add(sprites.getString("back_default"));
        if (sprites.optString("back_female", null) != null)
            imagensUrl.add(sprites.getString("back_female"));
        if (sprites.optString("back_shiny", null) != null)
            imagensUrl.add(sprites.getString("back_shiny"));
        if (sprites.optString("back_shiny_female", null) != null)
            imagensUrl.add(sprites.getString("back_shiny_female"));
        if (sprites.optString("front_default", null) != null)
            imagensUrl.add(sprites.getString("front_default"));
        if (sprites.optString("front_female", null) != null)
            imagensUrl.add(sprites.getString("front_female"));
        if (sprites.optString("front_shiny", null) != null)
            imagensUrl.add(sprites.getString("front_shiny"));
        if (sprites.optString("front_shiny_female", null) != null)
            imagensUrl.add(sprites.getString("front_shiny_female"));
        return imagensUrl;
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

    public interface PokemonDetalhesListener{
        void baixado(Pokemon pokemon);
        void erro(String erro);
    }

}
