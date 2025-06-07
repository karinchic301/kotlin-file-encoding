import java.io.File 
import java.nio.charset.Charset 
import java.nio.charset.StandardCharsets
import java.nio.file.Files

fun detectEncoding(file: File): Charset {
    val bom = ByteArray(4)
    file.inputStream().use { it.read(bom) }

    return when {
        bom[0] == 0xFF.toByte() && bom[1] == 0xFE.toByte() -> StandardCharsets.UTF_16LE
        bom[0] == 0xFE.toByte() && bom[1] == 0xFF.toByte() -> StandardCharsets.UTF_16BE
        bom[0] == 0xEF.toByte() && bom[1] == 0xBB.toByte() && bom[2] == 0xBF.toByte() -> StandardCharsets.UTF_8
        else -> StandardCharsets.UTF_8 
    }
}

fun main() {
    val path = "test.txt"
    val file = File(path)

    if (!file.exists()) {
        println("Файл $path не найден.")
        return
    }

    val charset = detectEncoding(file)
    println("Определена кодировка: $charset")

    val content = Files.readString(file.toPath(), charset)
    println("Содержимое файла:\n$content")
}
