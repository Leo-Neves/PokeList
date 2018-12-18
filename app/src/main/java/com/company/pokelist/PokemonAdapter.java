package com.company.pokelist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.pokelist.utils.ScalingBlurPostprocessor;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonView>{
    private Context context;
    private List<Pokemon> pokemons;
    private PokeService pokeService;

    public PokemonAdapter(Context context, List<Pokemon> pokemons, PokeService pokeService){
        this.context = context;
        this.pokemons = pokemons;
        this.pokeService = pokeService;
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
    public void onBindViewHolder(@NonNull final PokemonView pokemonView, int i) {
        //Neste método vamos trabalhar com os objetos Pokemon e exibir seu conteudo
        // em cada elemento visual da lista
        final Pokemon pokemon = pokemons.get(i);

        //Exibir nome dos Pokemons:
        String nome = pokemon.getNome();
        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
        pokemonView.nome.setText(nome);

        //Exibir imagem do Pokemon
        Uri uri = Uri.parse(pokemon.getImagem_url());
        pokemonView.figura.setImageURI(uri);

        //Exibir imagem de Pokemon no fundo e aplicar o filtro de BlurEffect
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(new ScalingBlurPostprocessor(10, 5, 20))
                .build();
        final DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(pokemonView.fundo.getController())
                .setImageRequest(imageRequest)
                .build();
        pokemonView.fundo.setController(controller);

        pokemonView.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pokeService.baixarDetalhesDePokemon(pokemon, new PokeService.PokemonDetalhesListener() {
                    @Override
                    public void baixado(Pokemon pokemon) {
                        LayoutInflater layoutInflater = ((MainActivity)context).getLayoutInflater();
                        View dialogView = layoutInflater.inflate(R.layout.dialog_pokemon, null, false);
                        TextView nome = dialogView.findViewById(R.id.dialogNome);
                        String nomePokemon = pokemon.getNome().substring(0, 1).toUpperCase() + pokemon.getNome().substring(1);
                        nome.setText(nomePokemon);
                        TextView ordem = dialogView.findViewById(R.id.dialogOrdem);
                        ordem.setText("#"+pokemon.getOrdem());
                        TextView altura = dialogView.findViewById(R.id.dialogAltura);
                        altura.setText("Altura: "+pokemon.getAltura()+ " cm");
                        TextView peso = dialogView.findViewById(R.id.dialogPeso);
                        peso.setText("Peso: "+pokemon.getPeso()+" kg");
                        TextView baseXp = dialogView.findViewById(R.id.dialogBaseXp);
                        baseXp.setText("Base Exp.: "+pokemon.getBase_exp());
                        String tipos = TextUtils.join(", ",pokemon.getTipos());
                        TextView tiposPokemon = dialogView.findViewById(R.id.dialogTipos);
                        tiposPokemon.setText("Tipos: "+tipos);
                        LinearLayout containerImagens = dialogView.findViewById(R.id.dialogImagens);
                        for (String imagemUrl : pokemon.getOutras_imagens_url()){
                            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
                            simpleDraweeView.setScaleType(ImageView.ScaleType.FIT_XY);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120,120);
                            layoutParams.setMargins(3,0,3,0);
                            simpleDraweeView.setLayoutParams(layoutParams);
                            Uri uri = Uri.parse(imagemUrl);
                            containerImagens.addView(simpleDraweeView);
                            simpleDraweeView.setImageURI(uri);
                            simpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
                        }

                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setView(dialogView);
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "FECHAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();
                    }

                    @Override
                    public void erro(String erro) {
                        Toast.makeText(context, erro, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        //Neste método iremos informar o total de Pokemons a serem exibidos
        return pokemons.size();
    }

    public class PokemonView extends RecyclerView.ViewHolder{
        public TextView nome;
        public SimpleDraweeView figura;
        public SimpleDraweeView fundo;
        public CardView cardView;

        public PokemonView(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomePokemon);
            figura = itemView.findViewById(R.id.figuraPokemon);
            fundo = itemView.findViewById(R.id.fundoPokemon);
            cardView = itemView.findViewById(R.id.cardPokemon);
        }
    }



}
