package com.hyunju.deliveryapp.screen.review.gallery

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.hyunju.deliveryapp.model.restaurant.review.GalleryPhotoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class GalleryPhotoRepository(
    private val context: Context
) {

    suspend fun getAllPhotos(): MutableList<GalleryPhotoModel> = withContext(Dispatchers.IO) {
        val galleryPhotoModelList = mutableListOf<GalleryPhotoModel>()
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val query: Cursor?
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.SIZE,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns._ID
        )
        val resolver = context.contentResolver
        query = resolver?.query(
            uriExternal,
            projection,
            null,
            null,
            "${MediaStore.Images.ImageColumns.DATE_ADDED} DESC"
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)
                val date = cursor.getString(dateColumn)

                val contentUri = ContentUris.withAppendedId(uriExternal, id)

                galleryPhotoModelList.add(
                    GalleryPhotoModel(
                        id = id,
                        uri = contentUri,
                        name = name,
                        size = size,
                        date = date ?: ""
                    )
                )
            }
        }

        galleryPhotoModelList
    }
}