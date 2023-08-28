package com.belajar.anew.data.preferences

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.belajar.anew.data.model.Profile
import com.belajar.anew.data.preferences.PreferencesKeys.ALAMAT
import com.belajar.anew.data.preferences.PreferencesKeys.EMAIL
import com.belajar.anew.data.preferences.PreferencesKeys.IMG_PROFILE
import com.belajar.anew.data.preferences.PreferencesKeys.LOGINSTATUS
import com.belajar.anew.data.preferences.PreferencesKeys.NAMA
import com.belajar.anew.data.preferences.PreferencesKeys.PENDIDIKAN
import com.belajar.anew.data.preferences.PreferencesKeys.USIA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

//kelas dari data khusus untuk objek itu dapat menggunakan objek atau instance kelas secara bergantian.
//key untuk mengabdate data dari datastrore
object PreferencesKeys {
    val LOGINSTATUS = booleanPreferencesKey("loginStatus")
    val EMAIL = stringPreferencesKey("Email")
    val NAMA = stringPreferencesKey("Nama")
    val USIA = stringPreferencesKey("Usia")
    val ALAMAT = stringPreferencesKey("Alamat")
    val PENDIDIKAN = stringPreferencesKey("Pendidikan")
    val IMG_PROFILE = stringPreferencesKey("IMG_PROFILE")


}

// private val agar tidak dapat diubah oleh class lain
class UserPreferences(
    private val userPreferencesStore: DataStore<Preferences>, context: Context
) {
    //untuk menampilkan error
    //untuk mengambil nilai dari profil
    val userPreferencesFlow: Flow<Profile> = userPreferencesStore.data
        .catch { exception->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val loginStatus = preferences[LOGINSTATUS] ?: false
            val email = preferences[EMAIL] ?: ""
            val nama = preferences[NAMA] ?: ""
            val usia = preferences[USIA] ?: ""
            val alamat = preferences[ALAMAT] ?: ""
            val pendidikan = preferences[PENDIDIKAN] ?: ""
            val imgProfile = preferences[IMG_PROFILE]?.let {
                Log.d("TEST", it)
                Uri.parse(it)
            }
            Profile(loginStatus, email, nama, usia, alamat, pendidikan, imgProfile)
        }

    // suspend sebagai fungsi dan bersifat publik karena untuk di panggil di class lain
    // untuk memperbarui properti pada userlogin

    suspend fun userLogin(loginStatus: Boolean, email: String){
        userPreferencesStore.edit { preferences ->
            preferences[LOGINSTATUS] = loginStatus
            preferences[EMAIL] = email
        }
    }
    // untuk memperbarui properti pada profile
    suspend fun profile(nama: String, usia: String, alamat: String, pendidikan: String){
        userPreferencesStore.edit { preferences ->
            preferences[NAMA] = nama
            preferences[USIA] = usia
            preferences[ALAMAT] = alamat
            preferences[PENDIDIKAN] = pendidikan
        }
    }

    suspend fun setImage(img: Uri?){
        //untuk menulis data
        userPreferencesStore.edit { preferences ->
            preferences[IMG_PROFILE] = img.toString()
        }
    }
}