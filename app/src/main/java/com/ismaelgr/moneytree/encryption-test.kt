package com.ismaelgr.moneytree

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator


private val keyStoreType: String = "AndroidKeyStore"

////////////////////////////////////////////////////////////////////

private fun getKey(userID: String): Key? {
    val keyStore: KeyStore = KeyStore.getInstance(keyStoreType)
    keyStore.load(null)
    // El segundo parámetro de getKey() es null porque Android KeyStore maneja la autenticación de forma interna.
    return keyStore.getKey(userID, null)
}

private fun generateKey(
    userID: String,
    blockMode: String = KeyProperties.BLOCK_MODE_GCM,
    encryptionPadding: String = KeyProperties.ENCRYPTION_PADDING_NONE,
    algorithm: String = KeyProperties.KEY_ALGORITHM_AES
): Key {
    val keyGenerator: KeyGenerator = KeyGenerator.getInstance(
        algorithm,
        keyStoreType
    )

    /*
    * GCM (Galois/Counter Mode): Ensures data confidentiality and integrity
    * No Padding: Optimizes the encryption process and enhances security.
    * */
    val keyParamSpec: KeyGenParameterSpec = KeyGenParameterSpec
        .Builder(userID, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        .setBlockModes(blockMode)
        .setEncryptionPaddings(encryptionPadding)
        .setUserAuthenticationRequired(true)
        .build()

    keyGenerator.init(keyParamSpec)

    return keyGenerator.generateKey()
}

fun doCipher(key: Key, content: ByteArray, mode: Int): String {
    val cipher: Cipher = Cipher.getInstance(key.algorithm, keyStoreType)
    cipher.init(mode, key)

    val processedContent: ByteArray = cipher.doFinal(content)

    return String(processedContent)
}
//////////////////////////////////////////////////////////////////// With Cipher

fun encrypt(userID: String, token: String): String {
    val key = getKey(userID = userID) ?: generateKey(userID = userID)

    return doCipher(
        key = key,
        content = token.toByteArray(),
        mode = Cipher.ENCRYPT_MODE
    )
}

fun decrypt(userID: String, encryptedToken: ByteArray): String? {
    val key = getKey(userID = userID) ?: return null

    return doCipher(
        key = key,
        content = encryptedToken,
        mode = Cipher.DECRYPT_MODE
    )
}

//////////////////////////////////////////////////////////////////// With EncryptedSharedPreferences

private fun getTokenFolder(context: Context, userID: String): SharedPreferences {
    val mainKeyAlias: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val fileName: String = "$userID-token"

    return EncryptedSharedPreferences.create(
        context,
        fileName,
        mainKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

fun saveEncrypted(context: Context, userID: String, token: String) {
    val sharedPrefs = getTokenFolder(context, userID)

    with(sharedPrefs.edit()) {
        putString(userID, token)
        apply()
    }
}

fun getEncryptedToken(context: Context, userID: String): String? {
    val sharedPrefs = getTokenFolder(context, userID)

    return sharedPrefs.getString(userID, null)
}