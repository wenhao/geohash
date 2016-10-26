[![Build Status](https://travis-ci.org/wenhao/geohash.svg?branch=master)](https://travis-ci.org/wenhao/geohash)

## [GeoHash](https://github.com/wenhao/geohash)
   
### 功能

* 52位Geohash([32位GeoHash算法]), 精度0.6m
* 经纬度转52位值
* 根据距离搜索附近目标(8个相邻区域和1个中心区域)

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

[52位GeoHash算法]

### 例子

[结合Redis的例子]

### Copyright and license

Copyright 2016 Wen Hao

Licensed under [Apache License]

[52位GeoHash算法]: ./docs/algorithm.md
[32位GeoHash算法]: https://en.wikipedia.org/wiki/Geohash
[结合Redis的例子]: ./docs/example.md