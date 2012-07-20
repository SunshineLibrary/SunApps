#!/bin/sh
cd `dirname $0`
rm metadb
sqlite3 metadb < metadata.sql
adb push metadb /data/data/com.sunshine.support/databases/metadb

