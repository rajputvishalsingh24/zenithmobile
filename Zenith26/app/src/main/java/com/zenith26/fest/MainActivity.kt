package com.zenith26.fest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.zenith26.fest.navigation.ZenithNavHost
import com.zenith26.fest.ui.theme.Slate950
import com.zenith26.fest.ui.theme.Zenith26Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Zenith26Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Slate950
                ) {
                    ZenithNavHost()
                }
            }
        }
    }
}
