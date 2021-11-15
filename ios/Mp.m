#import "Mp.h"
#import <Foundation/Foundation.h>
#import <React/RCTRootView.h>
#import <React/RCTBundleURLProvider.h>

@implementation Mp{
    bool hasListeners;
}
RCT_EXPORT_MODULE()
- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_METHOD(initialize:(NSDictionary *)params findEventsWithResolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSArray *menus = params[@"menus"];
    NSMutableArray *sheetItems = [NSMutableArray array];

    for (int i=0;i<menus.count;i++) {
        NSLog(@"-> %@",menus[i]);
        [sheetItems addObject:[[DCUniMPMenuActionSheetItem alloc] initWithTitle:menus[i][@"title"] identifier:menus[i][@"key"]]];
    }
    [DCUniMPSDKEngine setDefaultMenuItems:sheetItems];

    [DCUniMPSDKEngine setMenuButtonHidden:!params[@"capsule"]];
    [DCUniMPSDKEngine setDelegate:self];
    resolve([NSNumber numberWithBool:true]);
}
RCT_EXPORT_METHOD(launch:(NSDictionary *)arg  findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    NSString *appid = arg[@"appid"];
    NSString *path = arg[@"path"];
    NSDictionary *params = arg[@"params"];

    [DCUniMPSDKEngine openApp:appid
                    arguments:params redirectPath:path];
    resolve([NSNumber numberWithBool:true]);
}
RCT_EXPORT_METHOD(isExistsApp:(NSString *)appid  findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    BOOL isExistsApp = [DCUniMPSDKEngine isExistsApp:appid];
    resolve([NSNumber numberWithBool:isExistsApp]);
}

RCT_EXPORT_METHOD(releaseWgtToRunPathFromPath:(NSString *)path appid:(NSString *)appid findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
//    NSString *appid = [[path lastPathComponent] stringByDeletingPathExtension];
    BOOL success = [DCUniMPSDKEngine releaseAppResourceToRunPathWithAppid:appid resourceFilePath:path];
    resolve([NSNumber numberWithBool:success]);
}

- (NSArray<NSString *> *)supportedEvents
{
    return @[@"MenuItemClick",@"UniMPEventReceive",@"onCloseMp"];
}

#pragma mark - DCUniMPSDKEngineDelegate
/// DCUniMPMenuActionSheetItem 点击触发回调方法
- (void)defaultMenuItemClicked:(NSString *)identifier {
    NSLog(@"标识为 %@ 的 item 被点击了", identifier);
}

- (void)onUniMPEventReceive:(NSString *)event data:(id)data callback:(DCUniMPKeepAliveCallback)callback {

    NSLog(@"Receive UniMP event: %@ data: %@",event,data);
    if(hasListeners){
        [self sendEventWithName:@"UniMPEventReceive" body:data];
    }
    // 回传数据给小程序
    // DCUniMPKeepAliveCallback 用法请查看定义说明
    if (callback) {
        callback(@"native callback message",NO);
    }
}

- (UIView *)splashViewForApp:(NSString *)appid{
    return [[RCTRootView alloc] initWithBridge: self.bridge moduleName:@"MpModules.splashView" initialProperties:@{@"appid":appid}];
}
- (void)uniMPOnClose:(NSString *)appid{
    if(hasListeners){
        [self sendEventWithName:@"onCloseMp" body:appid];
    }
}

// 在添加第一个监听函数时触发
-(void)startObserving {
    hasListeners = YES;
    // Set up any upstream listeners or background tasks as necessary
}

// Will be called when this module's last listener is removed, or on dealloc.
-(void)stopObserving {
    hasListeners = NO;
    // Remove upstream listeners, stop unnecessary background tasks
}

@end
