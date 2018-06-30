package codepath.kaughlinpractice.flixsterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.kaughlinpractice.flixsterapp.models.Movie;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static codepath.kaughlinpractice.flixsterapp.MovieAdapter.imageUrl_front;

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;
    ImageView poster;


    // the view objects
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        // resolve the view objects
        //tvTitle = (TextView) findViewById(R.id.tvTitle);
        //tvOverview = (TextView) findViewById(R.id.tvOverview);
        //tvOverview.setMovementMethod(new ScrollingMovementMethod());
        //rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        tvOverview.setMovementMethod(new ScrollingMovementMethod());

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());


        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        poster = (ImageView) findViewById(R.id.ivfrontposter);
        poster.setAlpha(50);


        // load image using glide
        GlideApp.with(this)
                .load(getIntent().getStringExtra(imageUrl_front))
                .transform(new RoundedCornersTransformation(30,0 ))
                .placeholder(R.drawable.flicks_movie_placeholder) //flicks backdrop for backdrop
                .error(R.drawable.flicks_movie_placeholder) //TODO change error place holder
                .into(poster);

    }


    public void goToTrailer(View view) {

        Toast.makeText(getApplicationContext(), "Welcome to the trailer", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, MovieTrailerActivity.class);
        // serialize the movie using parceler, use its short name as a key
        i.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
        startActivity(i);
    }








}

