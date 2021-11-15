# react-native-mp

React Native + MiniProgram
![Demo](https://pay-xinxiaomeng.oss-cn-beijing.aliyuncs.com/web/yhh/theme/20215/w8ufi-kea4f.gif "小程序")

## 安装

```sh
npm install react-native-mp
```

## 使用

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
            params: {cc: 1},
          });// 打开小程序;
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
