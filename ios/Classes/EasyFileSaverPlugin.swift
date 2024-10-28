import Flutter
import PhotosUI
import UIKit

public class EasyFileSaverPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "easy_file_saver", binaryMessenger: registrar.messenger())
    let instance = EasyFileSaverPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getPlatformVersion":
      result("iOS " + UIDevice.current.systemVersion)
    case "saveImageToGalleryFromBase64":
      let arguments = call.arguments as! [String: Any]
      let base64Image = arguments["base64Image"] as! String
      let fileName = arguments["fileName"] as! String
      saveImageToGalleryFromBase64(base64Image, fileName, result)
    default:
      result(FlutterMethodNotImplemented)
    }
  }

  private func saveImageToGalleryFromBase64(_ base64Image: String, _ fileName: String, _ result: @escaping FlutterResult) {
    PHPhotoLibrary.shared().performChanges({
        let options = PHAssetResourceCreationOptions()
        options.originalFilename = fileName
        let request = PHAssetCreationRequest.forAsset()
        request.addResource(with: PHAssetResourceType.photo, data: Data(base64Encoded: base64Image)!, options: options)
    }) { res, error in
      DispatchQueue.main.async {
          result(res ? "OK" : "Error")
      }
    }
  }
}
