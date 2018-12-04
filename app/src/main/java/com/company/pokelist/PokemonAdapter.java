package com.company.pokelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.nativecode.NativeBlurFilter;
import com.facebook.imagepipeline.postprocessors.BlurPostProcessor;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
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
        String nome = pokemon.getNome();
        nome = nome.substring(0, 1).toUpperCase() + nome.substring(1);
        pokemonView.nome.setText(nome);
        Uri uri = Uri.parse(pokemon.getImagem_url());
        pokemonView.figura.setImageURI(uri);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(new ScalingBlurPostprocessor(10, 5, 20))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(pokemonView.fundo.getController())
                .setImageRequest(imageRequest)
                .build();
        pokemonView.fundo.setController(controller);
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

        public PokemonView(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomePokemon);
            figura = itemView.findViewById(R.id.figuraPokemon);
            fundo = itemView.findViewById(R.id.fundoPokemon);
        }
    }

    public class ScalingBlurPostprocessor extends BasePostprocessor {

        private final Paint mPaint = new Paint();
        private final int mIterations;
        private final int mBlurRadius;
        /**
         * A scale ration of 4 means that we reduce the total number of pixels to process by factor 16.
         */
        private final int mScaleRatio;

        public ScalingBlurPostprocessor(int iterations, int blurRadius, int scaleRatio) {
            Preconditions.checkArgument(scaleRatio > 0);

            mIterations = iterations;
            mBlurRadius = blurRadius;
            mScaleRatio = scaleRatio;
        }

        @Override
        public CloseableReference<Bitmap> process(
                Bitmap sourceBitmap,
                PlatformBitmapFactory bitmapFactory) {
            final CloseableReference<Bitmap> bitmapRef =
                    bitmapFactory.createBitmap(
                            sourceBitmap.getWidth() / mScaleRatio, sourceBitmap.getHeight() / mScaleRatio);

            try {
                final Bitmap destBitmap = bitmapRef.get();
                final Canvas canvas = new Canvas(destBitmap);

                canvas.drawBitmap(
                        sourceBitmap,
                        null,
                        new Rect(-destBitmap.getWidth(), -destBitmap.getHeight(), destBitmap.getWidth()*2, destBitmap.getHeight()*2),
                        mPaint);

                NativeBlurFilter.iterativeBoxBlur(
                        destBitmap, mIterations, Math.max(1, mBlurRadius / mScaleRatio));
                return CloseableReference.cloneOrNull(bitmapRef);
            } finally {
                CloseableReference.closeSafely(bitmapRef);
            }
        }
    }

}
