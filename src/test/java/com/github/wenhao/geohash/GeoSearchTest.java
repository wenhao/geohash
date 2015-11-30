package com.github.wenhao.geohash;

import com.github.wenhao.geohash.domain.GeoRange;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GeoSearchTest {

    @Test
    public void should_be_able_to_get_search_start_point_and_end_point_if_provider_search_range() {
        // given

        // when
        List<GeoRange> geoRanges = GeoSearch.range(30.5464140000, 104.0748220000, 3000);

        // then
        assertThat(geoRanges.size()).isEqualTo(9);
        assertThat(geoRanges.get(0).getMin()).isEqualTo(4025111615111168L);
        assertThat(geoRanges.get(0).getMax()).isEqualTo(4025111682220032L);
    }
}