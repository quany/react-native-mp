import { NativeModules } from 'react-native';

type MpType = {
  multiply(a: number, b: number): Promise<number>;
};

const { Mp } = NativeModules;

export default Mp as MpType;
