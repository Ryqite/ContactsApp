package com.example.variant1

import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.variant1.ui.theme.Variant1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text(text = "gregege")
            RequestContactPermission()
            }
        }
    }

@Composable
fun Contacts(contacts: List<Contact>, painter: Painter,modifier: Modifier) {
    LazyColumn {
        itemsIndexed(contacts) { index, contact ->
            Box(modifier = modifier.fillMaxWidth()) {
                Row(modifier = modifier
                    .background(Color.LightGray)
                    .clickable {}) {
                    Image(
                        painter=painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop)
                    Column(){
                        Text(text = contact.name)
                        Text(text = contact.number)
                    }
                }
            }
        }
    }
}


@Composable
fun RequestContactPermission() {
    val permissions = remember {arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE)}
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { permission-> permission==true }
        if (allGranted) {

        } else {
        }
    }
    LaunchedEffect(Unit) {
            launcher.launch(permissions)
    }
}

