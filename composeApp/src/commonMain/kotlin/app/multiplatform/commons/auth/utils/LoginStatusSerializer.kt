package app.multiplatform.commons.auth.utils

import app.multiplatform.commons.auth.domain.models.LoginStatus
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

object LoginStatusSerializer : KSerializer<LoginStatus> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LoginStatus", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LoginStatus {
        return when (val value = decoder.decodeString()) {
            "PASS" -> LoginStatus.PASS
            "FAIL" -> LoginStatus.FAIL
            "UI" -> LoginStatus.UI
            "REDIRECT" -> LoginStatus.REDIRECT
            "RESTART" -> LoginStatus.RESTART
            else -> LoginStatus.UNKNOWN
        }
    }

    override fun serialize(encoder: Encoder, value: LoginStatus) {
        encoder.encodeString(value.name)
    }
}