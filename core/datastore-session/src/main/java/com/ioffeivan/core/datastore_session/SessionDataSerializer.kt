package com.ioffeivan.core.datastore_session

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.ioffeivan.core.datastore_session.model.SessionData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object SessionDataSerializer : Serializer<SessionData> {
    override val defaultValue: SessionData = SessionData()

    override suspend fun readFrom(input: InputStream): SessionData {
        return try {
            Json.decodeFromString<SessionData>(input.readBytes().decodeToString())
        } catch (exception: SerializationException) {
            throw CorruptionException("Unable to deserialize the SessionData", exception)
        }
    }

    override suspend fun writeTo(t: SessionData, output: OutputStream) {
        output.write(Json.encodeToString(t).encodeToByteArray())
    }
}
