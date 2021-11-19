# react-native-mp

为有基础能力(比如交易,通信,AI等)APP提供开放方案;用React Native实现基础业务;以小程序,H5扩展生态;

![Demo](https://pay-xinxiaomeng.oss-cn-beijing.aliyuncs.com/web/yhh/theme/20215/w8ufi-kea4f.gif "小程序")

## 安装

> 使用React Native 0.60以上

- npm package

```sh
npm install react-native-mp
```

- 在iOS工程的AppDelegate.m

```objectivec
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
#ifdef FB_SONARKIT_ENABLED
  InitializeFlipper(application);
#endif

  RCTBridge *bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:launchOptions];
  RCTRootView *rootView = [[RCTRootView alloc] initWithBridge:bridge
                                                   moduleName:@"io"
                                            initialProperties:nil];

  if (@available(iOS 13.0, *)) {
    rootView.backgroundColor = [UIColor systemBackgroundColor];
  } else {
    rootView.backgroundColor = [UIColor whiteColor];
  }

  self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
  UIViewController *rootViewController = [UIViewController new];
  rootViewController.view = rootView;
  self.window.rootViewController = rootViewController;
  [self.window makeKeyAndVisible];
  // 配置参数
  NSMutableDictionary *options = [NSMutableDictionary dictionaryWithDictionary:launchOptions];
  // 设置 debug YES 会在控制台输出 js log，默认不输出 log，注：需要引入 liblibLog.a 库
  [options setObject:[NSNumber numberWithBool:NO] forKey:@"debug"];
  // 初始化引擎
  [DCUniMPSDKEngine initSDKEnvironmentWithLaunchOptions:options];
  return YES;
}
```

## 入门

```js
import Mp from 'react-native-mp';

// 初始化
initialize({
  menus: [{title: '标题', key: 't1'}],
  capsule: true,
  fontSize: '16px',
  fontColor: '#000',
  fontWeight: 'normal',
  isFromRecents: true,
  enableBackground: true,
});
isExistsApp(appid);// 是否存在
releaseWgtToRunPathFromPath(path, appid);// 部署资源
launch({
  appid,
  params: {time: 1},
});// 打开小程序;
```

## 目标

- 解决APP臃肿,轻量化APP,快速响应用户;
- 解耦大而全的业务,高扩展;
- 扩展生态共享;

## 欢迎交流

![20211119181000](https://i.loli.net/2021/11/19/8DbdEz9Jjf4AG5i.png)

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
