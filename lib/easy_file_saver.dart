
import 'easy_file_saver_platform_interface.dart';

class EasyFileSaver {
  
  Future<String?> getPlatformVersion() {
    return EasyFileSaverPlatform.instance.getPlatformVersion();
  }

  Future<String> saveImageToGalleryFromBase64(String base64Image, String fileName, { String androidFolderName = "" }) {
    return EasyFileSaverPlatform.instance.saveImageToGalleryFromBase64(
        base64Image, fileName, androidFolderName: androidFolderName);
  }

}
