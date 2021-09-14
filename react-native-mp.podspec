require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-mp"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "10.0" }
  s.source       = { :git => "https://github.com/quany/react-native-mp.git", :tag => "#{s.version}" }


  s.source_files = "ios/**/*.{h,m,mm,swift}"

  s.resources = 'ios/UniMPSDK/Core/Resources/*.{js,ttf,bundle}'

  s.libraries = 'c++','iconv'

  s.frameworks = 'JavaScriptCore','CoreMedia','MediaPlayer','AVFoundation','AVKit','GLKit','OpenGLES','CoreText','QuartzCore','CoreGraphics','QuickLook','CoreTelephony','AssetsLibrary','CoreLocation','AddressBook'

  s.vendored_libraries = 'ios/UniMPSDK/Core/Libs/*.a'

  s.vendored_frameworks = 'ios/UniMPSDK/Core/Libs/*.framework'

  s.dependency "React-Core"
end
