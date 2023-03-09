package com.mobile.encrypthttpinterceptor

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Encryptor {
    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(key: String, initVector: String, value: String): String? {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            val iv = IvParameterSpec(initVector.toByteArray(charset("UTF-8")))

            val secretKeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv)

            val encrypted = cipher.doFinal(value.toByteArray())

            return String(Base64.getEncoder().encode(encrypted))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(key: String, initVector: String, encrypted: String?): String? {
        try {
            val iv = IvParameterSpec(initVector.toByteArray(charset("UTF-8")))
            val secretKeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv)

            val original = cipher.doFinal(Base64.getDecoder().decode(encrypted))

            return String(original)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

}