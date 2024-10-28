#ifndef FLUTTER_PLUGIN_EASY_FILE_SAVER_PLUGIN_H_
#define FLUTTER_PLUGIN_EASY_FILE_SAVER_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace easy_file_saver {

class EasyFileSaverPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  EasyFileSaverPlugin();

  virtual ~EasyFileSaverPlugin();

  // Disallow copy and assign.
  EasyFileSaverPlugin(const EasyFileSaverPlugin&) = delete;
  EasyFileSaverPlugin& operator=(const EasyFileSaverPlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace easy_file_saver

#endif  // FLUTTER_PLUGIN_EASY_FILE_SAVER_PLUGIN_H_
