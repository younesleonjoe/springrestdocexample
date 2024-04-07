package com.younesleonjoe.springrestdocexample.web.controller;

import com.younesleonjoe.springrestdocexample.repository.BeerRepository;
import com.younesleonjoe.springrestdocexample.web.mapper.BeerMapper;
import com.younesleonjoe.springrestdocexample.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/beers")
@RestController
public class BeerController {

  private final BeerMapper beerMapper;
  private final BeerRepository beerRepository;

  @GetMapping("/{beerId}")
  public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId) {

    return new ResponseEntity<>(
        beerMapper.BeerToBeerDto(beerRepository.findById(beerId).get()), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity saveNewBeer(@RequestBody @Validated BeerDto beerDto) {

    beerRepository.save(beerMapper.BeerDtoToBeer(beerDto));

    return new ResponseEntity(HttpStatus.CREATED);
  }

  @PutMapping("/{beerId}")
  public ResponseEntity updateBeerById(
      @PathVariable("beerId") UUID beerId, @RequestBody @Validated BeerDto beerDto) {
    beerRepository
        .findById(beerId)
        .ifPresent(
            beer -> {
              beer.setBeerName(beerDto.getBeerName());
              beer.setBeerStyle(beerDto.getBeerStyle().name());
              beer.setPrice(beerDto.getPrice());
              beer.setUpc(beerDto.getUpc());

              beerRepository.save(beer);
            });

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
