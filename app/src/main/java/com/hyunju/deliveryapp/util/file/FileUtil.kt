package com.hyunju.deliveryapp.util.file

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import com.hyunju.deliveryapp.DeliveryApplication.Companion.appContext
import com.hyunju.deliveryapp.model.restaurant.review.UriModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.util.*

object FileUtil {

    private const val MAX_WIDTH = 1280
    private const val MAX_HEIGHT = 960

    private const val TAG = "FileUtil"

    suspend fun bitmapResize(uriList: ArrayList<UriModel>): MutableList<Uri> {
        val photoHashMap = hashMapOf<Int, Uri>()

        val job = CoroutineScope(Dispatchers.IO).launch {
            uriList.forEachIndexed { index, uriModel ->
                launch {
                    optimizeBitmap(uriModel).apply {
                        photoHashMap[index] = Uri.fromFile(this?.let { File(it) })
                    }
                }
            }
        }

        job.join()

        return photoHashMap.values.toMutableList()
    }

    // 비트맵 최적화
    private fun optimizeBitmap(uriModel: UriModel): String? {
        val uri = uriModel.uri

        try {
            val storage = appContext!!.cacheDir
            val fileName = String.format("%s.%s", UUID.randomUUID(), "jpg")
            val tempFile = File(storage, fileName)
            tempFile.createNewFile()
            val fos = FileOutputStream(tempFile)

            val bitmap = resizeBitmapFormUri(uri).apply {
                this?.let {
                    rotateImageIfRequired(it, uri)
                    compress(Bitmap.CompressFormat.JPEG, 100, fos)
                }
            }
            bitmap?.recycle()

            fos.flush()
            fos.close()

            return tempFile.absolutePath
        } catch (e: IOException) {
            Log.e(TAG, "FileUtil - IOException: ${e.message}")
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "FileUtil - FileNotFoundException: ${e.message}")
        } catch (e: OutOfMemoryError) {
            Log.e(TAG, "FileUtil - OutOfMemoryError: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "FileUtil - ${e.message}")
        }

        return null
    }

    private fun resizeBitmapFormUri(uri: Uri): Bitmap? {
        val input = BufferedInputStream(appContext!!.contentResolver.openInputStream(uri))

        input.mark(input.available())

        return BitmapFactory.Options().run {

            inJustDecodeBounds = true
            BitmapFactory.decodeStream(input, null, this)

            input.reset()

            inSampleSize = calculateInSampleSize(this)

            inJustDecodeBounds = false

            BitmapFactory.decodeStream(input, null, this)
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > MAX_HEIGHT || width > MAX_WIDTH) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= MAX_HEIGHT && halfWidth / inSampleSize >= MAX_WIDTH) {
                inSampleSize *= 2

                Log.d(TAG, "calculateInSampleSize: $inSampleSize")
            }
        }

        return inSampleSize
    }

    private fun rotateImageIfRequired(bitmap: Bitmap, uri: Uri): Bitmap? {
        val input = appContext!!.contentResolver.openInputStream(uri) ?: return null

        val exif = if (Build.VERSION.SDK_INT > 23) {
            ExifInterface(input)
        } else {
            ExifInterface(uri.path!!)
        }

        return when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}