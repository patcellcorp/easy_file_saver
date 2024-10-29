# easy_file_saver

Easy File Saver.

## Getting Started

**1. Add this package to your pubspec.yaml**

```
dependencies:
    flutter:
        sdk: flutter
    easy_file_saver:
        git:
        url: https://github.com/patcellcorp/easy_file_saver.git
        ref: main
```

**2. How to use it**

As of now, this package only has one method that saves a base64 image to Gallery on Android and Photos on iOS/macOS. Other methods and platforms will be added later.

```
import 'package:easy_file_saver/easy_file_saver.dart';

Future<void> saveImage() async {
    final base64Image = 'BASE64_IMAGE_STRING'
    final res = await EasyFileSaver().saveImageToGalleryFromBase64(base64Image, "file_name.png", androidFolderName: "Whatever");
    print(res);
}
```