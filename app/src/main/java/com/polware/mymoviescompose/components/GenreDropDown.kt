package com.polware.mymoviescompose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.polware.mymoviescompose.data.model.Genre

@Composable
fun GenreDropDown(
    genre: Genre,
    onGenreSelected: (Genre) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val angle:Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )
    var parentSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    Row(
        modifier = Modifier
            .width(120.dp)
            .onGloballyPositioned {
                parentSize = it.size
            }
            .background(MaterialTheme.colors.background)
            .height(40.dp)
            .clickable(onClick = { expanded = true })
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = ContentAlpha.disabled
                ),
                shape = MaterialTheme.shapes.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(weight = 8f)
                .padding(start = 10.dp),
            text = genre.name,
            style = MaterialTheme.typography.subtitle2
        )
        IconButton(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .rotate(degrees = angle)
                .weight(weight = 1.5f),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop-Down Arrow Icon"
            )
        }

        DropdownMenu(
            modifier = Modifier
                .width(with(LocalDensity.current) {
                    parentSize.width.toDp()
                }),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Genre.values().slice(0..11).forEach {
                genreName ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onGenreSelected(genreName)
                    }
                ) {
                    GenreItem(genre = genreName)
                }
            }
        }
    }
}

@Composable
@Preview
private fun PriorityDropDownPreview() {
    GenreDropDown(
        genre = Genre.ACTION,
        onGenreSelected = {}
    )
}