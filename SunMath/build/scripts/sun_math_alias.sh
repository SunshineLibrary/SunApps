#!/bin/bash

MATH_APP_PACKAGE="com.ssl.curriculum.math"
MATH_APP_ACTIVITY="com.ssl.curriculum.math.activity.MainActivity"


MATH_PROJECT_DIR=$1

alias install_math_app_to_multi_devices="sh ./build/scripts/install_to_multi_devices.sh ./bin/SunMathApp-release.apk"
alias clean_math_app_app='cd $MATH_PROJECT_DIR&&ant clean'

alias math_release='clean_math_app_app&&cd $MATH_PROJECT_DIR&&ant release&&install_math_app_to_multi_devices $MATH_APP_PACKAGE $MATH_APP_ACTIVITY'
