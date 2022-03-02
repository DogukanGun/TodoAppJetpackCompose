package com.dag.todoappjetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dag.todoappjetpack.navigation.SetupNavigation
import com.dag.todoappjetpack.ui.screens.list.ListScreen
import com.dag.todoappjetpack.ui.theme.TodoAppJetpackTheme
import com.dag.todoappjetpack.ui.viewmodels.TodoVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var  navigationController: NavHostController
    private val viewModel: TodoVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppJetpackTheme {
                navigationController = rememberNavController()
                SetupNavigation(
                    navHostController = navigationController,
                    viewModel = viewModel
                )
            }
        }
    }
}
