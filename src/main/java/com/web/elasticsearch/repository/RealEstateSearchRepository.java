package com.web.elasticsearch.repository;

import com.web.elasticsearch.model.RealEstateSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealEstateSearchRepository extends ElasticsearchRepository<RealEstateSearch, Long> {
}
