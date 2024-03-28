package com.msbank.inventory.core.output.service.inventory.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.msbank.inventory.core.domain.Sale;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DataResponseDto {

    @JsonProperty("data")
    private Sale sale;

}
