package codepath.kaughlinpractice.flixsterapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.kaughlinpractice.flixsterapp.models.Config;
import codepath.kaughlinpractice.flixsterapp.models.Movie;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    // list of movies
    ArrayList<Movie> movies;
    // config needed for image urls
    Config config;
    //context for rendering
    Context context;

    // intialize with list

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }
    public void setConfig(Config config) {
        this.config = config;
    }

    public static final String imageUrl_front = "imageUrl_front";



    // creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the contyext and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie,parent, false);
        return new ViewHolder(movieView);


    }

    // binds a inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the movie data at the specified position
        Movie movie = movies.get(position);
        // populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // build url for poster image
        String imageUrl = null;

        // if in portrait mode, load the poster image
        if(isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        }
        else{
            // load the backdrop image
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }
        // get the correct placeholder and imageview for the current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        // load image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(30,0 ))
                .placeholder(placeholderId)
                .error(R.drawable.flicks_movie_placeholder) //TODO change error place holder
                .into(imageView);


    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the viewholder as a static inner clas
    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        // track view objects
        @Nullable@BindView(R.id.ivPosterImage) ImageView ivPosterImage;
        @Nullable@BindView(R.id.ivBackdropImage) ImageView ivBackdropImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;
        //ImageView ivPosterImage;
        //ImageView ivBackdropImage;
        //TextView tvTitle;
        //TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             //lookup view ibjects by id
            //ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            //ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            //tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            //tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);// TODO change to movieDetails activity
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));

                intent.putExtra(imageUrl_front,config.getImageUrl(config.getPosterSize(),movie.getPosterPath()));
                //intent.putExtra(imageUrl_front,config.getImageBaseUrl()config.getPosterSize(),movie.getPosterPath());

                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
