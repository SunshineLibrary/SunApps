adb uninstall com.ssl.support.daemon
adb shell rm -r /sdcard/.contents
adb install ./bin/SunDaemon-release.apk
