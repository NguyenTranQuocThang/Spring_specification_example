package com.study.specification.specification;

import java.util.List;

import com.study.specification.enums.QueryOperator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Filter {
    private String field;
    private QueryOperator operator;
    private String value;
    private List<String> values;
}
