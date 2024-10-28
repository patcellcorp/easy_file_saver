import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'easy_file_saver_platform_interface.dart';

/// An implementation of [EasyFileSaverPlatform] that uses method channels.
class MethodChannelEasyFileSaver extends EasyFileSaverPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('easy_file_saver');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<String> saveImageToGalleryFromBase64(String base64Image, String fileName, { String androidFolderName = "" }) async {
    final result = await methodChannel.invokeMethod<String>(
      'saveImageToGalleryFromBase64',
      { 'base64Image': base64Image, 'fileName': fileName, 'androidFolderName': androidFolderName, }
    );
    return result!;
  }
}
