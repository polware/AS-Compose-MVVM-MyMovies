package com.polware.mymoviescompose.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.polware.mymoviescompose.data.model.Genre
import com.polware.mymoviescompose.ui.theme.MEDIUM_PADDING

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MovieDetails(
    image: String,
    onImageChange: (String) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    genre: Genre,
    onGenreSelected: (Genre) -> Unit,
    year: String,
    onYearChange: (String) -> Unit,
    score: Int,
    onScoreChange: (Int) -> Unit
) {
    val pattern = remember { Regex("^\\d+\$") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Log.d("MovieImage", "ImageDB: $image")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(all = MEDIUM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        ImageLoader(image = image) {
            uri ->
            onImageChange(uri.toString())
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            modifier = Modifier.width(280.dp),
            value = title,
            onValueChange = { newTitle -> onTitleChange(newTitle) },
            label = { Text(text = "Title") },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(8.dp))
        GenreDropDown(
            genre = genre,
            onGenreSelected = onGenreSelected
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            modifier = Modifier.width(280.dp),
            value = year,
            onValueChange = {
                    newYear ->
                if (newYear.isEmpty() || newYear.matches(pattern)) {
                    onYearChange(newYear)
                }
            },
            label = { Text("Year") },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .width(280.dp)
                .height(140.dp)
                .padding(3.dp),
            value = description,
            onValueChange = { newDescription -> onDescriptionChange(newDescription) },
            label = { Text(text = "Description") },
            singleLine = false,
            textStyle = MaterialTheme.typography.body1,
            keyboardActions = KeyboardActions {
                onDescriptionChange(description.trim())
                keyboardController?.hide()
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Score",
            fontWeight = FontWeight.SemiBold
        )
        // Rating bar
        MovieRatingBar(score = score) {
            newScore ->
            onScoreChange(newScore)
        }
    }
}
