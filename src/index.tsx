import React, { Component, ComponentType } from 'react';
import {
  NativeEventEmitter,
  NativeModules,
  AppRegistry,
  StyleSheet,
  Platform,
  Text,
  View,
} from 'react-native';

class NormalSplashView extends Component<{ appid: string }> {
  render() {
    console.log('props', this.props);
    return (
      <View style={styles.container}>
        <Text>启动屏:{this.props.appid}</Text>
      </View>
    );
  }
}

let splashView: ComponentType<{ appid: string }>;

AppRegistry.registerComponent('MpModules.splashView', () => {
  return splashView || NormalSplashView;
});

/**
 * 设置小程序启动屏
 * @param component
 */
export function setSplashView(component: ComponentType<{ appid: string }>) {
  splashView = component;
}

const LINKING_ERROR =
  `The package 'react-native-mp' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const Mp = NativeModules.Mp
  ? NativeModules.Mp
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );
const emitter = new NativeEventEmitter(Mp);

/**
 * 初始化
 * @param o
 * @returns
 */
export function initialize(o?: any): Promise<any> {
  return Mp.initialize(o);
}
/**
 * 释放wgt
 * @param wgtPath
 * @param appId
 * @returns
 */
export function releaseWgtToRunPathFromPath(
  wgtPath: string,
  appId: string
): Promise<any> {
  return Mp.releaseWgtToRunPathFromPath(wgtPath, appId);
}
/**
 * 小程序是否存在
 * @param appid
 * @returns
 */
export function isExistsApp(appid: string): Promise<any> {
  return Mp.isExistsApp(appid);
}
/**
 * 打开小程序
 * @returns
 */
export function launch(o: object): Promise<any> {
  return Mp.launch(o);
}

/**
 * 监听小程序关闭
 * @param cb
 * @returns {EmitterSubscription}
 */
export function onAppClose(cb: (res: any) => void) {
  return emitter.addListener('onCloseMp', cb);
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#ff0',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
