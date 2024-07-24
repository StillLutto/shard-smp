package me.lutto.shardsmp.extension

fun String.wrap(length: Int): List<String> {
    val result = mutableListOf<String>()
    var startIndex = 0

    while (startIndex < this.length) {
        var endIndex = (startIndex + length).coerceAtMost(this.length)
        val lastSpaceIndex = this.lastIndexOf(' ', endIndex)

        if (lastSpaceIndex in (startIndex + 1)..<endIndex) {
            endIndex = lastSpaceIndex
        }

        val line = this.substring(startIndex, endIndex).trim()
        result.add(line)

        startIndex = if (endIndex == this.length) endIndex else endIndex + 1
    }

    return result
}