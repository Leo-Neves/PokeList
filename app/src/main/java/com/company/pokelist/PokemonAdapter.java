package com.company.pokelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonView>{
    private Context context;
    private List<Pokemon> pokemons;

    public PokemonAdapter(Context context, List<Pokemon> pokemons){
        this.context = context;
        this.pokemons = pokemons;
    }

    @NonNull
    @Override
    public PokemonView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Aqui vamos definir qual arquivo de layout será utilizado para gerar
        // os elementos da lista
        LayoutInflater layoutInflater = ((MainActivity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.lista_pokemon, viewGroup,false);
        PokemonView pokemonView = new PokemonView(view);
        return pokemonView;
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonView pokemonView, int i) {
        //Neste método vamos trabalhar com os objetos Pokemon e exibir seu conteudo
        // em cada elemento visual da lista
        Pokemon pokemon = pokemons.get(i);
        pokemonView.nome.setText(pokemon.getNome());
        Picasso.get().load(pokemon.getImagem_url()).into(pokemonView.figura);
    }

    @Override
    public int getItemCount() {
        //Neste método iremos informar o total de Pokemons a serem exibidos
        return pokemons.size();
    }

    public class PokemonView extends RecyclerView.ViewHolder{
        public TextView nome;
        public ImageView figura;
        public ImageView fundo;

        public PokemonView(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomePokemon);
            figura = itemView.findViewById(R.id.figuraPokemon);
            fundo = itemView.findViewById(R.id.fundoPokemon);
        }
    }

}
