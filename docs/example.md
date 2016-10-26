###Example

####需求

   搜索2千米内所有的嘀嘀司机
    
####实现

1. 乘客坐标30.5464140000, 104.0748220000

2. 嘀嘀司机不断地向Redis更新自己的坐标
   假如司机坐标为30.5388942218,104.0555758833
   
   ```java
    GeoHash geoHash = GeoHash.fromCoordinate(30.5388942218,104.0555758833)
    //4024744861876082L
    long longValue = geoHash.toLong();
   ```

   ```
   ZADD didiDriver 4024744861876082 driverId
   ```
3. 乘客在搜索司机时先录入自己坐标到Redis
   
   ```
   ZADD passenger 4025111557750656 passengerId
   ```

4. 2千米内计算出9个搜索范围

   ```java
   List<GeoRange> geoRanges = GeoSearch.range(30.5464140000, 104.0748220000, 2000);
   ```
5. 针对每一个搜索GeoRange范围调用Redis

   ```java
   double min = geoRange.min();
   double max = geoRange.max();
   ```
   
   ```  
   ZRANGEBYSCORE didiDriver min max WITHSCORES
   ```
6. 最后使用距离公式刷选结果
   
    ```java
    GeoHash geoHash = GeoHash.fromLong(4024744861876082L);
    double distance = geoHash.distance(30.5464140000, 104.0748220000);
    if(distance < 2000){
        //....
    }
    ```
    
此算法也可以使用其他的数据库如MySQL等.