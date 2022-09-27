import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'netrunner_azure_method_channel.dart';

abstract class NetrunnerAzurePlatform extends PlatformInterface {
  /// Constructs a NetrunnerAzurePlatform.
  NetrunnerAzurePlatform() : super(token: _token);

  static final Object _token = Object();

  static NetrunnerAzurePlatform _instance = MethodChannelNetrunnerAzure();

  /// The default instance of [NetrunnerAzurePlatform] to use.
  ///
  /// Defaults to [MethodChannelNetrunnerAzure].
  static NetrunnerAzurePlatform get instance => _instance;
  
  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [NetrunnerAzurePlatform] when
  /// they register themselves.
  static set instance(NetrunnerAzurePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
