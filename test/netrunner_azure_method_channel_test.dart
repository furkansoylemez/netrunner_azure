import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:netrunner_azure/netrunner_azure_method_channel.dart';

void main() {
  MethodChannelNetrunnerAzure platform = MethodChannelNetrunnerAzure();
  const MethodChannel channel = MethodChannel('netrunner_azure');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
