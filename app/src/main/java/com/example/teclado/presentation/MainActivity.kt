
package com.example.teclado.presentation

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            textScreen()
        }
    }
}


@Composable
fun  textScreen(){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextInput()
    }
}


@Composable
fun TextInput() {
    val label = remember { mutableStateOf("Start") }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                val results: Bundle? = RemoteInput.getResultsFromIntent(data)
                val ipAddress: CharSequence? = results?.getCharSequence("ip_address")
                label.value = ipAddress.toString() //as String
            }
        }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier =Modifier.height(20.dp))
        Chip(
            label = { Text(label.value) },
            onClick = {},
        )
        Spacer(modifier =Modifier.height(20.dp))
        Chip(label={Text("Insert Data")},
            onClick = {
                val intent : Intent = RemoteInputIntentHelper.createActionRemoteInputIntent();
                val remoteInputs:List<RemoteInput> = listOf(
                    RemoteInput.Builder("ip_address")
                        .setLabel("Manual IP Entry").wearableExtender{
                            setEmojisAllowed(false)
                            setInputActionType(EditorInfo.IME_ACTION_DONE)
                        }.build()
                )
                RemoteInputIntentHelper.putRemoteInputsExtra(intent,remoteInputs)
                launcher.launch(intent)
            } )

    }
}
