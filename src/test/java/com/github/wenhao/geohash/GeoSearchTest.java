package com.github.wenhao.geohash;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class GeoSearchTest {

    @Test
    public void should_be_able_to_get_search_start_point_and_end_point_if_provider_search_range() {
        // given

        // when
        long[] range = GeoSearch.range(30.5451620000, 104.0620180000, 3000, 5000);

        // then
        assertThat(range[0]).isEqualTo(4024745032941568L);
        assertThat(range[1]).isEqualTo(4024745133604864L);
    }
}