package com.younesleonjoe.springrestdocexample.web.mapper;

import com.younesleonjoe.springrestdocexample.domain.Beer;
import com.younesleonjoe.springrestdocexample.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

  BeerDto BeerToBeerDto(Beer beer);

  Beer BeerDtoToBeer(BeerDto dto);
}
