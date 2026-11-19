package com.ioffeivan.core.datastore_app_settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.ioffeivan.core.datastore_app_settings.model.AppSettings
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object AppSettingsSerializer : Serializer<AppSettings> {
    override val defaultValue: AppSettings = AppSettings()

    private val json =
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            json.decodeFromString<AppSettings>(input.readBytes().decodeToString())
        } catch (exception: SerializationException) {
            throw CorruptionException("Unable to deserialize the AppSettings", exception)
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        output.write(json.encodeToString(t).encodeToByteArray())
    }
}
