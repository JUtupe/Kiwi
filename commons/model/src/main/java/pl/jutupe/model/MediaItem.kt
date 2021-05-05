package pl.jutupe.model

sealed class MediaItem (
    val isPlayable: Boolean,
    val type: ItemType,
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
    ) : MediaItem(isPlayable = true, ItemType.TYPE_SONG) {

        override val subtitle: String
            get() = artist
    }

    data class Root(
        override val id: String,
        override val title: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false, ItemType.TYPE_ROOT) {

        override val subtitle: String
            get() = ""
    }

    data class Album(
        override val id: String,
        override val title: String,
        val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false, ItemType.TYPE_ALBUM) {

        override val subtitle: String
            get() = artist
    }

    data class Artist(
        override val id: String,
        override val title: String,
        val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false, ItemType.TYPE_ARTIST) {

        override val subtitle: String
            get() = ""
    }

    data class Playlist(
        override val id: String,
        override val title: String,
        val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false, ItemType.TYPE_PLAYLIST) {

        override val subtitle: String
            get() = artist
    }

    data class PlaylistMember(
        override val id: String,
        override val title: String,
        val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = true, ItemType.TYPE_PLAYLIST_MEMBER) {

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
            .firstOrNull { it.value == value }
    }
}