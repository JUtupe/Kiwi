package pl.jutupe.model

sealed class MediaItem (
    val isPlayable: Boolean
) {
    abstract val id: String
    abstract val title: String
    abstract val artist: String
    abstract val art: String?

    data class Song(
        override val id: String,
        override val title: String,
        override val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = true)

    data class Root(
        override val id: String,
        override val title: String,
        override val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false)

    data class Album(
        override val id: String,
        override val title: String,
        override val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false)

    data class Artist(
        override val id: String,
        override val title: String,
        override val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false)

    data class Playlist(
        override val id: String,
        override val title: String,
        override val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = false)

    data class PlaylistMember(
        override val id: String,
        override val title: String,
        override val artist: String,
        override val art: String?,
    ) : MediaItem(isPlayable = true)

    companion object {
        fun create(
            id: String,
            title: String,
            artist: String,
            art: String?,
            type: ItemType
        ): MediaItem = when (type) {
            ItemType.TYPE_ROOT -> Root(id, title, artist, art)
            ItemType.TYPE_SONG -> Song(id, title, artist, art)
            ItemType.TYPE_PLAYLIST -> Playlist(id, title, artist, art)
            ItemType.TYPE_PLAYLIST_MEMBER -> PlaylistMember(id, title, artist, art)
            ItemType.TYPE_ALBUM -> Album(id, title, artist, art)
            ItemType.TYPE_ARTIST -> Artist(id, title, artist, art)
        }
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