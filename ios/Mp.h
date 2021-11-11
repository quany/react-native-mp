#import <React/RCTBridgeModule.h>
#import "DCUniMP.h"
#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeDelegate.h>

@interface Mp : RCTEventEmitter <RCTBridgeModule,DCUniMPSDKEngineDelegate,RCTBridgeDelegate>

@end
