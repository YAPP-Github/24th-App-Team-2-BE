package com.xorker.draw.log

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class RequestLoggingWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val encoding: Charset by lazy {
        var characterEncoding = request.characterEncoding
        if (characterEncoding.isBlank()) {
            characterEncoding = StandardCharsets.UTF_8.name()
        }
        Charset.forName(characterEncoding)
    }
    private val rawData: ByteArray by lazy {
        try {
            val inputStream: InputStream = request.inputStream
            inputStream.readAllBytes()
        } catch (e: IOException) {
            throw e
        }
    }

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(this.rawData)
        return object : ServletInputStream() {
            override fun isFinished(): Boolean {
                return false
            }

            override fun isReady(): Boolean {
                return false
            }

            override fun setReadListener(readListener: ReadListener) {}

            override fun read(): Int {
                return byteArrayInputStream.read()
            }
        }
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(this.inputStream, this.encoding))
    }
}
