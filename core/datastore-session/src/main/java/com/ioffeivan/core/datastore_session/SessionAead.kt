package com.ioffeivan.core.datastore_session

import com.google.crypto.tink.Aead
import com.google.crypto.tink.integration.android.AndroidKeystoreKmsClient
import java.io.IOException
import java.security.GeneralSecurityException

private const val SESSION_KEY_URI = "android-keystore://smart_greenhouse_session"

internal class SessionAead : Aead {
    private val delegate by lazy {
        try {
            AndroidKeystoreKmsClient.getOrGenerateNewAeadKey(SESSION_KEY_URI)
        } catch (exception: GeneralSecurityException) {
            throw IOException("Unable to initialize the session key", exception)
        }
    }

    override fun encrypt(plaintext: ByteArray, associatedData: ByteArray): ByteArray =
        delegate.encrypt(plaintext, associatedData)

    override fun decrypt(ciphertext: ByteArray, associatedData: ByteArray): ByteArray =
        delegate.decrypt(ciphertext, associatedData)
}
