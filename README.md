[![Build Status](https://travis-ci.org/wenhao/geohash.svg?branch=master)](https://travis-ci.org/wenhao/geohash)

## [GeoHash](https://github.com/wenhao/geohash)
   
### 功能

* 52位Geohash, 精度0.6m
* 经纬度转52位integer值
* 根据距离搜索附近目标

#### Gradle

```groovy
repositories {
  jcenter()
}

dependencies {
  compile 'com.github.wenhao:geohash:1.0.0'
}
```

### Maven

```xml
<dependency>
  <groupId>com.github.wenhao</groupId>
  <artifactId>geohash</artifactId>
  <version>1.0.0</version>
</dependency>
```

### 算法

[52为GeoHash算法]

### 例子

[结合Redis的例子]

### Copyright and license

Copyright 2016 Wen Hao

Licensed under [Apache License]

[52为GeoHash算法]: ./docs/algorithm.md
[结合Redis的例子]: ./docs/example.md