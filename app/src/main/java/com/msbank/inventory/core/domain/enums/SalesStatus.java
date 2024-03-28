package com.msbank.inventory.core.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SalesStatus {

    PENDING(1),

    FINALIZED(2),

    CANCELED(3);

   private final Integer statusId;

    SalesStatus(Integer statusId) {
        this.statusId = statusId;
    }


    public static SalesStatus toEnum(Integer id) {
       if (id == null) return null;
       return Arrays.stream(values())
                .filter(status -> id.equals(status.statusId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Id dp Status Inválido:"+ id));
    }
}
