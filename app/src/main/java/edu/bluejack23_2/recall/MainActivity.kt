package edu.bluejack23_2.recall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import edu.bluejack23_2.recall.ui.theme.RecallTheme
import edu.bluejack23_2.recall.ui.util.NavigationGraph
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Typeface
import android.widget.TextView
import edu.bluejack23_2.recall.ui.util.BiometricPromptManager
import java.io.File

//class MainActivity : ComponentActivity() {
class MainActivity : AppCompatActivity() {

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val typeface = resources.getFont(R.font.roboto_black)
        val tv = TextView(this)
        tv.typeface = typeface

        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
        setContent {
            RecallTheme {
//                RecallApp()
                NavigationGraph(promptManager = promptManager);
            }
        }
    }

//    @Composable
//    fun RecallApp() {
//        NavigationGraph();
//    }

}
