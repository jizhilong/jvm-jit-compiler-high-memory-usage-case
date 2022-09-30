## what is this
This little java program shows that a jvm application may cause the jit compiler consume nearyly a gigabyte of memory, while keep the
jvm heap size as small as tens of megabytes.


## how to reproduce the case
just execute command `./run.sh` in a jdk(8+) box.


## test result
Following is the result on openjdk 17 and openjdk 1.8. In both case, high compiler memory usage were observed, especially on openjdk 17.

on openjdk 17:

```
openjdk version "17.0.3" 2022-04-19 LTS
OpenJDK Runtime Environment Corretto-17.0.3.6.1 (build 17.0.3+6-LTS)
OpenJDK 64-Bit Server VM Corretto-17.0.3.6.1 (build 17.0.3+6-LTS, mixed mode, sharing)

running at jit level 4
Darwin macmini-jzl.local 21.2.0 Darwin Kernel Version 21.2.0: Sun Nov 28 20:28:54 PST 2021; root:xnu-8019.61.5~1/RELEASE_X86_64 x86_64
+ /usr/bin/time -l java -server -cp . -XX:+TieredCompilation -XX:TieredStopAtLevel=4 -XX:+UseSerialGC -Xms20m -Xmx20m -XX:NativeMemoryTracking=summary Test
peak total committed happened at 15670 ms, done at 27150 ms
total: 1028 MB
Java Heap: 20 MB
Thread: 14 MB
Code: 7 MB
Compiler: 958 MB
Symbol: 1 MB
Shared class space: 11 MB
Arena Chunk: 11 MB
Metaspace: 2 MB
       27.24 real        16.82 user         1.24 sys
          1561051136  maximum resident set size
                   0  average shared memory size
                   0  average unshared data size
                   0  average unshared stack size
              441979  page reclaims
                   0  page faults
                   0  swaps
                   0  block input operations
                   0  block output operations
                   0  messages sent
                   0  messages received
                   4  signals received
                   0  voluntary context switches
               47480  involuntary context switches
         83353578421  instructions retired
         69082459464  cycles elapsed
          1039642624  peak memory footprint
```

on openjdk 8:

```
openjdk version "1.8.0_222"
OpenJDK Runtime Environment (AdoptOpenJDK)(build 1.8.0_222-b10)
OpenJDK 64-Bit Server VM (AdoptOpenJDK)(build 25.222-b10, mixed mode)

running at jit level 4
Darwin macmini-jzl.local 21.2.0 Darwin Kernel Version 21.2.0: Sun Nov 28 20:28:54 PST 2021; root:xnu-8019.61.5~1/RELEASE_X86_64 x86_64
+ /usr/bin/time -l java -server -cp . -XX:+TieredCompilation -XX:TieredStopAtLevel=4 -XX:+UseSerialGC -Xms20m -Xmx20m -XX:NativeMemoryTracking=summary Test
peak total committed happened at 4891 ms, done at 27951 ms
total: 105 MB
Java Heap: 20 MB
Class: 5 MB
Thread: 12 MB
Code: 3 MB
Compiler: 62 MB
Symbol: 1 MB
       28.07 real         7.32 user         0.81 sys
           115757056  maximum resident set size
                   0  average shared memory size
                   0  average unshared data size
                   0  average unshared stack size
               30727  page reclaims
                   0  page faults
                   0  swaps
                   0  block input operations
                   0  block output operations
                   0  messages sent
                   0  messages received
                  15  signals received
                   1  voluntary context switches
               46908  involuntary context switches
         25203698005  instructions retired
         21190182954  cycles elapsed
            83959808  peak memory footprint
```
