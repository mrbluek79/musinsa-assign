package com.musinsa.assign.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum PointTypeEnum {
    SAVE(1, "적립"),
    USE (2, "사용"),
    CANCEL_SAVE (3, "적립취소"),
    CANCEL_USE (4, "사용취소"),
    NONE (99, "")
    ;

    private Integer value;
    private String description;

    public static PointTypeEnum of(Integer value) {
        for (PointTypeEnum e : PointTypeEnum.values()) {
            if (Objects.equals(value, e.getValue())) {
                return e;
            }
        }

        return NONE;
    }
}
