
import 'netrunner_azure_platform_interface.dart';

class NetrunnerAzure {
  Future<String?> getPlatformVersion() {
    return NetrunnerAzurePlatform.instance.getPlatformVersion();
  }
}
