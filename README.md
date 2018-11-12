[![Build Status](https://travis-ci.org/wenhao/geohash.svg?branch=master)](https://travis-ci.org/wenhao/geohash)

## [GeoHash](https://github.com/wenhao/geohash)

[52位GeoHash算法]，精度约0.6米，查找附近点，广泛用于需要基于LBS搜索的场景，例如滴滴、微信、陌陌、美团等.

### 功能

* 二维坐标转一维长整形
* 根据距离搜索目标附近9个(8个相邻区域和1个中心区域)候选区域
* 计算坐标之间的距离(跟实际的百度导航距离有偏差)

### 精度

| GeoHash长度     | 正方形边长(米)     |
| :------------- | :------------- |
| 52       | 0.597       |
| 50       | 1.193       |
| ...      | ...         |
| 32       | 610.984     |
| 30       | 1221.969    |
| ...      | ...         |

#### Gradle

```groovy
repositories {
  jcenter()
}

dependencies {
  compile 'com.github.wenhao:geohash:1.0.2'
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


### 例子

[结合Redis的例子]

### Copyright and license

Copyright 2016~2018 Wen Hao

Licensed under [Apache License]

[52位GeoHash算法]: ./docs/algorithm.md
[32位GeoHash算法]: https://en.wikipedia.org/wiki/Geohash
[结合Redis的例子]: ./docs/example.md
[Apache License]: ./LICENSE