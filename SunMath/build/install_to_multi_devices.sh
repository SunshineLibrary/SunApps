#!/bin/bash
if [ $# -lt 3 ] ;then
    echo "usage $0 packagename activityName"
    exit 1
fi

APK=$1
PACKAGE=$2
ACTIVITYNAME=$3


installOnSingleDevice(){	
    echo "adb -s $1 uninstall $2"
	adb -s $1 uninstall $2
	
    echo "adb -s $1 install -r $APK"
    adb -s $1 install -r $APK 

    echo "adb -s $1 shell am start -n $2/$3"
    adb -s $1 shell am start -n $2/$3
    echo "--------------------------"
}

if [ $? -eq 0 ] ;then
    adb devices | grep device | grep -v attached | cut -f 1 > tmpfile
    while read line; 
    do 
        installOnSingleDevice $line $PACKAGE $ACTIVITYNAME
    done < tmpfile
    rm -f tmpfile
else
    echo "No apk at this path (${FILEPATH})"
fi
