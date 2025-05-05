package com.example.variant1

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
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
import androidx.core.content.ContextCompat.startActivity
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
        val contacts= work.loadContacts(context)
        contactsState.value=contacts.sortedBy { it.name }
    }
    GroupedContacts(contactsState.value,work,context)
}
@Composable
fun LetterHeader(letter: String) {
    Surface(modifier = Modifier.padding(vertical = 16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.Blue)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = letter,
                color = Color.Black
            )
        }
    }
}

@Composable
fun GroupedContacts(contacts: List<Contact>,workWithContacts: WorkWithContacts,context: Context) {
    val groupedContacts = remember(contacts) {
        workWithContacts.groupContacts(contacts)
    }
    val sections = remember(groupedContacts) {
        groupedContacts.keys.sorted()
    }
    Surface(modifier = Modifier.padding(all = 2.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 1.dp, color = Color.Blue)
                .background(Color.White)
        ) {
            sections.forEach { letter ->
                val contactsInSection = groupedContacts[letter] ?: emptyList()
                item {
                    LetterHeader(letter = letter)
                }
                itemsIndexed(contactsInSection) { index, contact ->
                    ContactItem(contact,context)
                }
            }
        }
    }
}

@Composable
fun RequestContactPermission(permissionGranted: () -> Unit) {
    val permissions = remember {
        arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE
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
fun ContactItem(contact: Contact,context: Context) {
    Surface(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.Blue)
                .padding(all = 8.dp)
                .clickable {
                    val callIntent = Intent(Intent.ACTION_CALL).apply {
                        data=Uri.parse("tel:${contact.number}")
                    }
                    context.startActivity(callIntent)
                           },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = contact.photoUri,
                contentDescription = "Contact photo",
                placeholder = painterResource(R.drawable.ic_correct),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = contact.name,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                contact.number?.let {
                    Text(
                        text = it,
                        color = Color.Gray
                    )
                }
            }
        }

    }
}
