package com.example.variant1.domain

import android.net.Uri

data class Contact(
    val id:Long,
    val name: String,
    val number: String?,
    val photoUri:Uri?
)