#!/usr/bin/env bash

javac Test.java

jit_level=${1:-4}
echo "running at jit level $jit_level"

if /usr/bin/time -l ls 2>/dev/null > /dev/null; then
  TIME_PREFIX='/usr/bin/time -l'   # on mac os
elif /usr/bin/time -v ls 2>/dev/null > /dev/null; then
  TIME_PREFIX='/usr/bin/time -v'   # on linux
else
  echo 'no full-metric time command found'
  TIME_PREFIX=''
fi

uname -a
set -x

$TIME_PREFIX java -server -cp . \
  -XX:+TieredCompilation -XX:TieredStopAtLevel=$jit_level \
  -XX:+UseSerialGC \
  -Xms20m -Xmx20m \
  -XX:NativeMemoryTracking=summary \
  Test
