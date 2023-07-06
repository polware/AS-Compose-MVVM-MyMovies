package com.polware.mymoviescompose.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.polware.mymoviescompose.data.model.Genre
import com.polware.mymoviescompose.ui.theme.SMALL_PADDING

@Composable
fun GenreItem(genre: Genre){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = genre.name,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(start = SMALL_PADDING)
        )
    }
}

@Composable
@Preview
fun GenreItemPreview(){
    GenreItem(genre = Genre.ACTION)
}