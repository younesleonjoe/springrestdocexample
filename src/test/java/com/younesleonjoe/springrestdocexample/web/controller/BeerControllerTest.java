package com.younesleonjoe.springrestdocexample.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.younesleonjoe.springrestdocexample.domain.Beer;
import com.younesleonjoe.springrestdocexample.repository.BeerRepository;
import com.younesleonjoe.springrestdocexample.web.model.BeerDto;
import com.younesleonjoe.springrestdocexample.web.model.BeerStyleEnum;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "com.younesleonjoe.springrestdocexample.web.mapper")
class BeerControllerTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @MockBean BeerRepository beerRepository;

  @Test
  void getBeerById() throws Exception {
    given(beerRepository.findById(any())).willReturn(Optional.of(Beer.builder().build()));

    mockMvc
        .perform(get("/api/v1/beers/" + UUID.randomUUID()).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void saveNewBeer() throws Exception {
    BeerDto beerDto = getValidBeerDto();
    String beerDtoJson = objectMapper.writeValueAsString(beerDto);

    mockMvc
        .perform(post("/api/v1/beers").contentType(MediaType.APPLICATION_JSON).content(beerDtoJson))
        .andExpect(status().isCreated());
  }

  @Test
  void updateBeerById() throws Exception {
    BeerDto beerDto = getValidBeerDto();
    String beerDtoJson = objectMapper.writeValueAsString(beerDto);

    mockMvc
        .perform(
            put("/api/v1/beers/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
        .andExpect(status().isNoContent());
  }

  private BeerDto getValidBeerDto() {
    return BeerDto.builder()
        .beerName("Nice Ale")
        .beerStyle(BeerStyleEnum.ALE)
        .price(new BigDecimal("9.99"))
        .upc(123123123123L)
        .build();
  }
}
