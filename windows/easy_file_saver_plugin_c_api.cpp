#include "include/easy_file_saver/easy_file_saver_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "easy_file_saver_plugin.h"

void EasyFileSaverPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  easy_file_saver::EasyFileSaverPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
