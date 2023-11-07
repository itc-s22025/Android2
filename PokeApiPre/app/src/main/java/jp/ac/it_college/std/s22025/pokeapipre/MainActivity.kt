package jp.ac.it_college.std.s22025.pokeapipre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jp.ac.it_college.std.s22025.pokeapipre.ui.theme.PokeApiPreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeApiPreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("android")
                }
            }
        }
    }
}

//@Composable
//fun MainScene(modifier: Modifier = Modifier){
////    var resulTest by remember {
////        mutableStateOf("")
////    }
//    Surface (Modifier){
//        Column {
//            ElevatedButton(onClick = { /*TODO*/ }) {
//                Text(text = "PokeApi test")
//            }
//        }
//
//    }
//}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokeApiPreTheme {
        Greeting("android")
    }
}