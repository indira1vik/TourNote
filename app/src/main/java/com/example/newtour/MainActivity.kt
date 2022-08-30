package com.example.newtour

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.newtour.models.Place
import com.example.newtour.sealed.DataState
import com.example.newtour.ui.theme.NewTourTheme
import com.example.newtour.ui.theme.Purple500
import com.example.newtour.viewmodels.MainView
import kotlinx.coroutines.delay
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    val viewModel: MainView by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewTourTheme {
                Navigation(viewModel)
            }
        }
    }
}

@Composable
fun Navigation(viewModel: MainView) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }
        composable("main_screen") {
            Column {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    backgroundColor = Purple500,
                    contentColor = Color.White

                )
                Spacer(modifier = Modifier.padding(10.dp))
                SetData(viewModel, navController)
            }
        }

        composable("detail_screen"){
            val result_next_page = navController.previousBackStackEntry?.savedStateHandle?.get<Place>("place")
            Log.d("Detail_Screen","${result_next_page?.Name}")
            DetailsScreen(
                result_next_page?.Name.toString(),
                result_next_page?.Image.toString(),
                result_next_page?.PlaceDescription.toString(),
                result_next_page?.NearestFlight.toString(),
                result_next_page?.ToVisit.toString(),
                result_next_page?.Package.toString(),
                
            )
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center){
        Image(
            painter = painterResource(R.drawable.ic_flights),
            contentDescription = null
        )
        Text(
            text = "Welcome to",
            style = TextStyle(fontSize = 32.sp),
            modifier = Modifier.padding(40.dp,400.dp,40.dp,0.dp)
        )
        Text(
            text = "TourNote",
            style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(40.dp,500.dp,40.dp,0.dp)
        )
    }
    LaunchedEffect(key1 = true) {
        delay(2400L)
        navController.navigate("main_screen")
    }
}

@Composable
fun DetailsScreen(
    CityName: String,
    CityImage: String,
    CityDesciption:String,
    CityFlight:String,
    CityVisit: String,
    CityPackage:String
) {
    Column {
        TopAppBar(
            title = {
                Text(text = CityName)
            },
            backgroundColor = Purple500,
            contentColor = Color.White
        )
        Image(
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            painter = rememberImagePainter(CityImage),
            contentDescription = "Image")
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(30.dp)
        ) {
            Text(text = "Place Description", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(text = CityDesciption)
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "Nearest Airport", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(text = CityFlight)
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "Place to Visit in ${CityName}", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(text = CityVisit)
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "Minimum Package Amount", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(text = CityPackage)
        }
    }
}

@Composable
fun CardItem(place: Place,navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .padding(15.dp, 0.dp, 15.dp, 0.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp
    ) {
        Box(modifier = Modifier.height(270.dp)){
            Image(
                painter = rememberImagePainter(place.Image),
                contentDescription = "My Description",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black, Color.Transparent
                        )
                    )
                ),
                contentAlignment = Alignment.BottomStart
            ){
                Text(
                    text = place.Name!!,
                    style = TextStyle(color = Color.White, fontSize = 20.sp),
                    modifier = Modifier.padding(20.dp))
            }
            Button(
                onClick = {
                    val place_take = place
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "place",
                        value = place_take
                    )
                    navController.navigate("detail_screen")
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Show More")
            }
        }
    }
    Spacer(modifier = Modifier.padding(10.dp))
}

@Composable
fun SetData(viewModel: MainView,navController: NavController) {
    when (val result = viewModel.response.value){
        is DataState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), Alignment.Center){
                CircularProgressIndicator()
            }
        }
        is DataState.Failure -> {
            Box(modifier = Modifier.fillMaxSize(), Alignment.Center){
                Text(text = result.message, style = TextStyle(fontSize = 24.sp))
            }
        }
        is DataState.Success -> {
            DisplayData(result.data, navController)
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize(), Alignment.Center){
                Text(text = "Error in Fetching Data", style = TextStyle(fontSize = 24.sp))
            }
        }
    }
}

@Composable
fun DisplayData(places: MutableList<Place>,navController: NavController) {
    LazyColumn{
        items(places){place->
            CardItem(place = place,navController)
        }
    }
}