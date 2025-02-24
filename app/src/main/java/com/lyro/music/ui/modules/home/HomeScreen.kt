package com.lyro.music.ui.modules.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.lyro.music.core.capabilities.songs.domain.dtos.Song
import com.lyro.music.core.capabilities.songs.infrastructure.datasource.MockSongsDataSource
import com.lyro.music.ui.components.SongList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(songs: List<Song>, onPlayItem: (index: Int) -> Unit) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Lyro") })
        },
        content = { paddingValues ->
            ConstraintLayout(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(20.dp)
            ) {
                SongList(songs = songs, onItemClick = onPlayItem)
            }
        }
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        songs = MockSongsDataSource().getAllSongs(),
        onPlayItem = {}
    )
}