package com.msbank.inventory.core.output.service.inventory.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.msbank.inventory.core.domain.Sale;



public record DataResponseDto(
        @JsonProperty("data")
        Sale sale
) {

}
