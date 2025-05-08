package com.example.kopringcacheexample.application.service

import org.springframework.stereotype.Service
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest

/**
 * 암호화/복호화 서비스
 */
@Service
class EncryptionService {

    companion object {
        private const val ALGORITHM = "AES/ECB/PKCS5Padding"
        private const val SECRET_KEY_ALGORITHM = "AES"
    }

    /**
     * 암호화
     */
    fun encrypt(plainText: String, secretKey: String): String {
        val key = createSecretKey(secretKey)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    /**
     * 복호화
     */
    fun decrypt(encryptedText: String, secretKey: String): String {
        val key = createSecretKey(secretKey)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        
        val decodedBytes = Base64.getDecoder().decode(encryptedText)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }
    
    /**
     * 암호화 키 생성 - 표준 AES 키 길이(128/192/256 비트)를 맞추기 위해 해시 사용
     */
    private fun createSecretKey(secretKey: String): SecretKeySpec {
        // SHA-256을 사용하여 지정된 키를 해시며 256비트(32바이트) 키 생성
        val digest = MessageDigest.getInstance("SHA-256")
        val keyBytes = digest.digest(secretKey.toByteArray())
        // AES-128 사용 (첫 16바이트만 사용)
        val truncatedKey = keyBytes.copyOf(16) // 128비트 AES 키
        return SecretKeySpec(truncatedKey, SECRET_KEY_ALGORITHM)
    }
}
