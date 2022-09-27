import 'package:flutter_test/flutter_test.dart';
import 'package:netrunner_azure/netrunner_azure.dart';
import 'package:netrunner_azure/netrunner_azure_platform_interface.dart';
import 'package:netrunner_azure/netrunner_azure_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockNetrunnerAzurePlatform 
    with MockPlatformInterfaceMixin
    implements NetrunnerAzurePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final NetrunnerAzurePlatform initialPlatform = NetrunnerAzurePlatform.instance;

  test('$MethodChannelNetrunnerAzure is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelNetrunnerAzure>());
  });

  test('getPlatformVersion', () async {
    NetrunnerAzure netrunnerAzurePlugin = NetrunnerAzure();
    MockNetrunnerAzurePlatform fakePlatform = MockNetrunnerAzurePlatform();
    NetrunnerAzurePlatform.instance = fakePlatform;
  
    expect(await netrunnerAzurePlugin.getPlatformVersion(), '42');
  });
}
