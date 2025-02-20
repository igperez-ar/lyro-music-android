package com.lyro.music.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lyro.music.core.capabilities.songs.domain.dtos.Song

@Composable
fun SongList(songs: List<Song>, onItemClick: (Int) -> Unit = {}) {
    LazyColumn {
        itemsIndexed(songs) { index, song ->
            SongItem(song, modifier = Modifier.clickable { onItemClick(index) })
            if (index < songs.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SongItem(
    song: Song,
    modifier: Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        SongThumbnail(song.imagePath, contentDescription = null)
        Column(
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(text = song.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = song.artist)
        }
    }
}

@Composable
fun SongThumbnail(albumArtUri: Uri, contentDescription: String?) {
    AsyncImage(
        model = albumArtUri,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Gray),
        contentScale = ContentScale.Crop
    )
}



