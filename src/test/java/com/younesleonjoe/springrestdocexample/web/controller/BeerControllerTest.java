package com.younesleonjoe.springrestdocexample.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme="https", uriHost="younesleonjoe.com", uriPort=443)
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
        .perform(
            get("/api/v1/beers/{id}", UUID.randomUUID())
                .param("isCold", "true")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(
            document(
                "v1/beers-get",
                pathParameters(
                    parameterWithName("id").description("UUID of the desired beer to get.")),
                responseFields(
                    fieldWithPath("id").description("Id of Beer"),
                    fieldWithPath("version").description("Version number"),
                    fieldWithPath("createdAt").description("date Created"),
                    fieldWithPath("updatedAt").description("Date Updated"),
                    fieldWithPath("beerName").description("Beer Name"),
                    fieldWithPath("beerStyle").description("Beer Style"),
                    fieldWithPath("upc").description("UPC of Beer"),
                    fieldWithPath("price").description("Price"),
                    fieldWithPath("quantityOnHand").description("Quantity On Hand"))));
  }

  @Test
  void saveNewBeer() throws Exception {
    BeerDto beerDto = getValidBeerDto();
    String beerDtoJson = objectMapper.writeValueAsString(beerDto);

    ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

    mockMvc
        .perform(post("/api/v1/beers").contentType(MediaType.APPLICATION_JSON).content(beerDtoJson))
        .andExpect(status().isCreated())
        .andDo(
            document(
                "v1/beers-new",
                requestFields(
                    fields.withPath("id").ignored(),
                    fields.withPath("beerName").description("Beer Name"),
                    fields.withPath("version").ignored(),
                    fields.withPath("createdAt").ignored(),
                    fields.withPath("updatedAt").ignored(),
                    fields.withPath("beerStyle").description("Beer Style"),
                    fields.withPath("upc").description("UPC of Beer").attributes(),
                    fields.withPath("price").description("Price"),
                    fields.withPath("quantityOnHand").ignored())));
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

  private static class ConstrainedFields {

    private final ConstraintDescriptions constraintDescriptions;

    ConstrainedFields(Class<?> input) {
      this.constraintDescriptions = new ConstraintDescriptions(input);
    }

    private FieldDescriptor withPath(String path) {
      return fieldWithPath(path)
          .attributes(
              key("constraints")
                  .value(
                      StringUtils.collectionToDelimitedString(
                          this.constraintDescriptions.descriptionsForProperty(path), ". ")));
    }
  }
}
