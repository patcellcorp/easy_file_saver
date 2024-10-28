package com.patcell.easy_file_saver

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

/** EasyFileSaverPlugin */
class EasyFileSaverPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context : Context

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "easy_file_saver")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "saveImageToGalleryFromBase64") {
      val ret = saveImageToGalleryFromBase64(call.argument<String>("base64Image")!!, call.argument<String>("fileName")!!, call.argument<String>("androidFolderName")!!)
      result.success(ret)
    } else {
      result.notImplemented()
    }
  }

  fun saveImageToGalleryFromBase64(base64Image: String, fileName: String, folderName: String): String {
    val decodedBytes = Base64.decode(base64Image, 0)
    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    return saveImage(bitmap, context, folderName, fileName)
  }

  private fun saveImage(bitmap: Bitmap, context: Context, folderName: String, fileName: String): String {
      if (Build.VERSION.SDK_INT >= 29) {
          val values = ContentValues()
          values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
          values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
          values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
          values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
          values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
          values.put(MediaStore.Images.Media.IS_PENDING, true)
          // RELATIVE_PATH and IS_PENDING are introduced in API 29.
          val uri: Uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
              ?: return "Error: Invalid URI"
          saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
          values.put(MediaStore.Images.Media.IS_PENDING, false)
          context.contentResolver.update(uri, values, null, null)
      } else {
          val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), folderName)
          if (!dir.exists() && !dir.mkdirs()) {
            return "Error: Cannot create nor access folder ${folderName}"
          }
          val date = Date()
          val fullFileName = fileName
          val fileName = fullFileName.substring(0, fullFileName.lastIndexOf("."))
          val extension = fullFileName.substring(fullFileName.lastIndexOf("."))
          val imageFile = File(dir.absolutePath.toString() + File.separator + fileName + "_" + System.currentTimeMillis() + ".jpg")
          saveImageToStream(bitmap, FileOutputStream(imageFile))
          val values = ContentValues()
          values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
          values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
          values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
          values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
          context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
      }
      return "OK"
  }

  private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
      if (outputStream != null) {
          try {
              bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
              outputStream.close()
          } catch (e: Exception) {
              e.printStackTrace()
          }
      }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
