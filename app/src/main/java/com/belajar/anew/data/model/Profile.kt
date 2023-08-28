package com.belajar.anew.data.model

import android.net.Uri
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

// sebagai class data dari preferences
data class Profile(
    val loginstatus: Boolean,
    val email: String,
    val nama: String,
    val usia: String,
    val alamat: String,
    val pendidikan:String,
    val imgProfile: Uri?

)

