package com.msbank.inventory.core.output.service.inventory.db;

import reactor.core.publisher.Mono;

public interface FindById <E>{
    Mono<E> find(final Integer id);
}
