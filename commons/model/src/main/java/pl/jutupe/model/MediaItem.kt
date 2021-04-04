package pl.jutupe.model

sealed class MediaItem (
    val isPlayable: Boolean
) {
    abstract val id: String

    abstract val title: String
    abstract val subtitle: String

    abstract val art: String?

    data class Song(
        override val id: String,
        override val title: String,
        val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = true) {

        override val subtitle: String
            get() = artist
    }

    data class Root(
        override val id: String,
        override val title: String,
        val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false) {

        override val subtitle: String
            get() = artist
    }

    data class Album(
        override val id: String,
        override val title: String,
        val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false) {

        override val subtitle: String
            get() = artist
    }

    data class Artist(
        override val id: String,
        override val title: String,
        val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false) {

        override val subtitle: String
            get() = ""
    }

    data class Playlist(
        override val id: String,
        override val title: String,
        val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false) {

        override val subtitle: String
            get() = artist
    }

    data class PlaylistMember(
        override val id: String,
        override val title: String,
        val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = true) {

        override val subtitle: String
            get() = artist
    }
}

enum class ItemType(
    val value: Int
) {
    TYPE_ROOT(0),
    TYPE_SONG(1),
    TYPE_PLAYLIST(2),
    TYPE_PLAYLIST_MEMBER(3),
    TYPE_ALBUM(4),
    TYPE_ARTIST(5);

    companion object {
        fun getByValue(value: Int) = values()
            .first { it.value == value }
    }
}