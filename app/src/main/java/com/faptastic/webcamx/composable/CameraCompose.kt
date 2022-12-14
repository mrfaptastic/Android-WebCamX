package com.faptastic.webcamx.composable

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.CountDownTimer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.faptastic.webcamx.camerax.CameraX
import com.faptastic.webcamx.utils.Commons
import com.faptastic.webcamx.utils.Commons.REQUIRED_PERMISSIONS
import java.util.*

enum class WEBCAMX_PREVIEW_STATE {
    ACTIVE, PAUSED, UNPAUSED
}

@Composable
fun CameraCompose(
    context: Context,
    cameraX: CameraX,
    onTimerClick: () -> Unit,
    onCaptureClick: () -> Unit
) {

    var previewState by remember { mutableStateOf(WEBCAMX_PREVIEW_STATE.ACTIVE) }

    var flipMode: () -> Unit = {
        /*
        if (inPreviewMode == true)
        {
            inPreviewMode = false
        }
        else
        {
            inPreviewMode = true
        }
        */
    }


    var hasCamPermission by remember {
        mutableStateOf(
            REQUIRED_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) ==
                        PackageManager.PERMISSION_GRANTED
            })
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { granted ->
            hasCamPermission = granted.size == 2
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        if (hasCamPermission) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        cameraX.startCameraPreviewView()
                    }
                )
             //   cameraX.bindCameraPreviewView()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(), Arrangement.Bottom, Alignment.CenterHorizontally
    ) {


        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp))
        {
            var button_text by remember { mutableStateOf("Start Timer") }

            Button(
                onClick = onCaptureClick
            ) {


                Text(text = "Capture")
            }
/*
            Button(
                onClick = {
                    if (cameraX.getPreviewIsActive())
                        cameraX.pauseCameraPreview()
                    else
                        cameraX.resumeCameraPreview()

                }
            ) {


                Text(text = "Toggle Preview")
            }
*/

            Button(
                onClick = {
                    if (button_text.contains("Stop")) {
                        button_text = "Start Timer"
                //        inPreviewMode = false
                    }
                    else{
                        button_text = "Stop Timer"
                //        inPreviewMode = true
                    }
                        onTimerClick()
                }

            ) {
                Text(text = button_text)
            }



        }



    }
}

