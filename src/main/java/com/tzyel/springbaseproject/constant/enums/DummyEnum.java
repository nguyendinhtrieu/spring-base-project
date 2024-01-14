package com.tzyel.springbaseproject.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DummyEnum {
    // @formatter:off
    ONE("ONE", 1),
    TWO("TWO", 2),
    THREE("THREE", 3),
    FOUR("FOUR", 4);
    // @formatter:on

    private final String valueAsString;
    private final Integer valueAsNumber;
}
