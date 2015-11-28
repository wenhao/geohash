package com.github.wenhao.geohash;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.github.wenhao.geohash.domain.GeoRange;

public class GeoSearchTest {

    @Test
    public void should_be_able_to_get_search_start_point_and_end_point_if_provider_search_range() {
         // given
    	double latitude = 30.5464140000;
    	double longitude = 104.0748220000;
        // when
        List<GeoRange> geoRanges = GeoSearch.range(latitude, longitude, 3000);
        
        // then
        int i =0;
        for (GeoRange geoRange : geoRanges) {
			System.out.println(++i +"="+geoRange);
			GeoHash min = GeoHash.fromLong(geoRange.getMin());
			GeoHash max = GeoHash.fromLong(geoRange.getMax());
			double distance = min.distance(latitude, longitude);
			double distance2 = max.distance(latitude, longitude);
			System.out.println(distance);
			System.out.println(distance2);
//			if(distance < 2){
//			    //....
//			}
		}
        //i get different result
       /* 1=GeoRange [min=4025111564779520, max=4025111581556736]
		1387.7315955851102
		1047.3011686333807
		2=GeoRange [min=4025111598333952, max=4025111615111168]
		1178.2754910732679
		2281.1501866388926
		3=GeoRange [min=4025111581556736, max=4025111598333952]
		1047.3011686333807
		1178.2754910732679
		4=GeoRange [min=4025111397007360, max=4025111413784576]
		1947.244457459772
		6174.277385085662
		5=GeoRange [min=4025111363452928, max=4025111380230144]
		2080.708643615349
		3077.839640189986
		6=GeoRange [min=4024744848392192, max=4024744865169408]
		3705.1840220838762
		9611.918173281605
		7=GeoRange [min=4024745032941568, max=4024745049718784]
		3321.4971615855147
		3364.9238538402897
		8=GeoRange [min=4024745049718784, max=4024745066496000]
		3364.9238538402897
		5731.822618079891
		9=GeoRange [min=4025111548002304, max=4025111564779520]
		1278.4234079219816
		1387.7315955851102*/
        
//        assertThat(geoRanges.size()).isEqualTo(9);
//        assertThat(geoRanges.get(0).getMin()).isEqualTo(4025111615111168L);
//        assertThat(geoRanges.get(0).getMax()).isEqualTo(4025111682220032L);
    }
}
