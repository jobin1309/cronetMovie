package com.example.movieflixcronet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.movieflixcronet.data.Movie
import com.example.movieflixcronet.ui.theme.MovieFlixCronetTheme
import com.example.movieflixcronet.viewModel.MovieViewModel
import com.quintetsolutions.qalert.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<MovieViewModel>()


        super.onCreate(savedInstanceState)
        setContent {
            MovieFlixCronetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MoviePage(viewModel)
                }
            }
        }
    }
}


@Composable
fun MovieCard(movie: Movie) {
    val image = rememberAsyncImagePainter(Constants.POSTER_BASE_URL + movie.posterPath)

    Card(
        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column() {
            Image(
                painter = image,
                contentDescription = "poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Column(Modifier.padding(10.dp)) {
                movie.title?.let {
                    Text(
                        text = it,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                }
                Text(
                    text = movie.releaseDate.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}

@Composable
fun MoviePage(viewModel: MovieViewModel) {
    // Observe the LiveData using observeAsState
    val moviesState by viewModel.moviesLiveData.observeAsState()

    // Call fetchMovies when the screen is created
    LaunchedEffect(Unit) {
    }

    // Render the UI based on the movies data
    LazyColumn {
        items(moviesState ?: emptyList()) { movie ->
            // Call your MovieCard composable with the movie data
            MovieCard(movie)
        }
    }
}



