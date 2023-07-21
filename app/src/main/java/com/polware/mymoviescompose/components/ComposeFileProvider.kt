package com.polware.mymoviescompose.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.polware.mymoviescompose.R
import com.polware.mymoviescompose.ui.theme.Orange
import java.io.File

class ComposeFileProvider: FileProvider(R.xml.filepaths) {

    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "movie_img_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }

}

@Composable
fun ImageLoader(
    image: String,
    imagePath: (Uri?) -> Unit
) {
    val context = LocalContext.current

    var hasPhoto by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = {
            uri ->
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION  // or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            val resolver = context.contentResolver
            resolver.takePersistableUriPermission(uri!!, flags)
            hasPhoto = uri != null
            imageUri = uri
            imagePath(uri)
            Log.d("ImageLauncher", "Selected Image: $imageUri")
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
                success ->
            hasPhoto = success
            imagePath(imageUri)
        }
    )

    Column(
        modifier = Modifier
            .width(280.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasPhoto && imageUri != null) {
            AsyncImage(
                model = Uri.parse(image),
                modifier = Modifier
                        .size(200.dp)
                        .padding(2.dp),
                contentDescription = "Movie image",
            )
        }
        else {
            AsyncImage(
                model = if (image.isEmpty()) R.drawable.empty_image else Uri.parse(image),
                contentDescription = "Movie Image",
                modifier = Modifier
                    .size(150.dp)
                    .padding(2.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            Modifier
                .padding(all = 2.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = {
                    hasPhoto = false
                    imageUri = null
                    val uri = ComposeFileProvider.getImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                }
            ) {
                Icon(
                    modifier = Modifier.size(size = 40.dp),
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = "Camera Icon",
                    tint = Orange
                )
            }
            IconButton(
                onClick = {
                    selectImageLauncher.launch(arrayOf("image/*"))
                }
            ) {
                Icon(
                    modifier = Modifier.size(size = 36.dp),
                    imageVector = Icons.Outlined.ImageSearch,
                    contentDescription = "Search Image",
                    tint = Orange
                )
            }
        }
    }
}
