package com.example.variant1

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        ContactsScreen()
        }
    }
}

@Composable
fun ContactsScreen() {
    val context = LocalContext.current
    val contactsState = remember { mutableStateOf<List<Contact>>(emptyList()) }
    val work = remember { WorkWithContacts() }
    RequestContactPermission {
        contactsState.value = work.loadContacts(context)
    }
    Contacts(contactsState.value)
}

@Composable
fun Contacts(contacts: List<Contact>, modifier: Modifier = Modifier) {
    LazyColumn {
        itemsIndexed(contacts) { index, contact ->
            ContactItem(contact, modifier)
        }
    }
}


@Composable
fun RequestContactPermission(permissionGranted: () -> Unit) {
    val permissions = remember {
        arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE
        )
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { permission -> permission == true }) {
            permissionGranted()
        }
    }
    LaunchedEffect(Unit) {
        launcher.launch(permissions)
    }
}

@Composable
fun ContactItem(contact: Contact, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .clickable {},
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = contact.photoUri,
            contentDescription = "Contact photo",
            placeholder = painterResource(R.mipmap.ic_launcher_round),
            modifier = modifier
                .size(48.dp)
                .clip(CircleShape)
        )
    }
    Spacer(modifier = modifier.width(16.dp))

    Column {
        Text(text = contact.name)
        contact.number?.let { Text(text = it) }
    }
}
