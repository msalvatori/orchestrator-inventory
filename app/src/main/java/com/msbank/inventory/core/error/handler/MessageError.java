package com.msbank.inventory.core.error.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageError {
    private String code;
    private String description;
}
