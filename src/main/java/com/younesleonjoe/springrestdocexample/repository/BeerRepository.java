package com.younesleonjoe.springrestdocexample.repository;

import com.younesleonjoe.springrestdocexample.domain.Beer;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BeerRepository
    extends PagingAndSortingRepository<Beer, UUID>, CrudRepository<Beer, UUID> {}
