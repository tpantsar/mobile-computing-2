package com.codemave.mobicomp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.codemave.mobicomp.navigation.MainNavigation
import com.codemave.mobicomp.ui.login.SharedPreferences
import com.codemave.mobicomp.ui.theme.MobicompTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Correct username and password are stored in SharedPreferences class
        val credentials = SharedPreferences(this.applicationContext)
        credentials.saveCredentials("matti", "123")

        setContent {
            MobicompTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainNavigation(viewModel = hiltViewModel(), context = applicationContext)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MobicompTheme {
        Greeting("Android")
    }
}