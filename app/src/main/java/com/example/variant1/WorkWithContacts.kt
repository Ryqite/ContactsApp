package com.example.variant1

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

class WorkWithContacts() {
    fun loadContacts(context: Context): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(ID,NAME,NUMBER,PHOTO_URI)

        context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndex(ID)
            val nameIndex = cursor.getColumnIndex(NAME)
            val phoneIndex = cursor.getColumnIndex(NUMBER)
            val photoIndex = cursor.getColumnIndex(PHOTO_URI)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val name = cursor.getString(nameIndex)
                val phone = cursor.getString(phoneIndex)
                val photoUri = cursor.getString(photoIndex)?.let { Uri.parse(it) }

                contacts.add(Contact(id, name, phone, photoUri))
            }
        }

        return contacts
    }
    private companion object {
        const val ID=ContactsContract.CommonDataKinds.Phone._ID
        const val NAME=ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        const val NUMBER=ContactsContract.CommonDataKinds.Phone.NUMBER
        const val PHOTO_URI=ContactsContract.CommonDataKinds.Phone.PHOTO_URI
    }
}
