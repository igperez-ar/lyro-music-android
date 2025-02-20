package com.lyro.music.core.capabilities.songs.infrastructure.datasource

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.lyro.music.core.capabilities.songs.domain.dtos.Song
import com.lyro.music.core.capabilities.songs.repository.SongsRepository
import javax.inject.Inject

class SongsDataSource @Inject constructor(
    private val contentResolver: ContentResolver
) : SongsRepository {

    companion object {
        private const val TAG = "SongsDataSource"
    }

    override fun getAllSongs(): List<Song> {
        val musicList = mutableListOf<Song>()
        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.IS_MUSIC
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        var cursor: Cursor? = null

        try {
            cursor = contentResolver.query(
                collection,
                projection,
                selection,
                null,
                sortOrder
            )

            if (cursor == null) {
                Log.e(TAG, "Cursor is null")
                return emptyList()
            }

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            if (cursor.count == 0) {
                Log.w(TAG, "No music files found")
                return emptyList()
            }

            while (cursor.moveToNext()) {
                val id = cursor.getString(idColumn)
                val title = cursor.getString(titleColumn) ?: "Unknown"
                val artist = cursor.getString(artistColumn) ?: "Unknown"
                val album = cursor.getString(albumColumn) ?: "Unknown"
                val albumId = cursor.getLong(albumIdColumn)
                val duration = cursor.getLong(durationColumn)
                val data = Uri.parse(cursor.getString(dataColumn))

                val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
                val albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId)

                val music = Song(id, title, artist, album, albumArtUri, duration, data)
                musicList.add(music)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error querying MediaStore", e)
        } finally {
            cursor?.close()
        }

        return musicList
    }
}
