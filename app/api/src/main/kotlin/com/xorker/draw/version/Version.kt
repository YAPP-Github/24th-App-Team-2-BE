package com.xorker.draw.version

class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
) : Comparable<Version> {
    override fun compareTo(other: Version): Int {
        if (major > other.major) return 1
        if (major < other.major) return -1

        if (minor > other.minor) return 1
        if (minor < other.minor) return -1

        return patch.compareTo(patch)
    }

    companion object {
        fun of(version: String): Version {
            val split = version.split(".")
            return Version(split[0].toInt(), split[1].toInt(), split.getOrElse(2) { "0" }.toInt())
        }
    }
}
