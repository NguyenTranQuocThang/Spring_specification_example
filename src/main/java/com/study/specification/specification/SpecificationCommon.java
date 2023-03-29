package com.study.specification.specification;

import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Path;

import static org.springframework.data.jpa.domain.Specification.where;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

@Service
public class SpecificationCommon<T> {
    public Specification<T> createSpecification(Filter filter) {
        switch (filter.getOperator()) {
            case EQUALS:
                return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(filter.getField()),
                        castToRequiredType(root.get(filter.getField()), filter.getValue())));
            case NOT_EQUALS:
                return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(filter.getField()),
                        castToRequiredType(root.get(filter.getField()),
                                filter.getValue()));

            case GREATER_THAN:
                return (root, query, criteriaBuilder) -> {
                    if (root.get(filter.getField()).getJavaType() == Timestamp.class) {
                        return criteriaBuilder.greaterThan(root.get(filter.getField()), (Timestamp) castToRequiredType(
                                root.get(filter.getField()),
                                filter.getValue()));
                    } else if (root.get(filter.getField()).getJavaType() == LocalDateTime.class) {
                        return criteriaBuilder.greaterThan(root.get(filter.getField()),
                                (LocalDateTime) castToRequiredType(
                                        root.get(filter.getField()),
                                        filter.getValue()));
                    } else {
                        return criteriaBuilder.gt(root.get(filter.getField()),
                                (Number) castToRequiredType(
                                        root.get(filter.getField()),
                                        filter.getValue()));
                    }
                };

            case LESS_THAN:
                return (root, query, criteriaBuilder) -> {
                    if (root.get(filter.getField()).getJavaType() == Timestamp.class) {
                        return criteriaBuilder.lessThan(root.get(filter.getField()), (Timestamp) castToRequiredType(
                                root.get(filter.getField()),
                                filter.getValue()));
                    } else if (root.get(filter.getField()).getJavaType() == LocalDateTime.class) {
                        return criteriaBuilder.lessThan(root.get(filter.getField()), (LocalDateTime) castToRequiredType(
                                root.get(filter.getField()),
                                filter.getValue()));
                    } else {
                        return criteriaBuilder.lt(root.get(filter.getField()),
                                (Number) castToRequiredType(
                                        root.get(filter.getField()),
                                        filter.getValue()));
                    }
                };

            case LIKE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(filter.getField()),
                        "%" + filter.getValue() + "%");

            case IN:
                return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(filter.getField()))
                        .value(castToRequiredType(root.get(filter.getField()), filter.getValues()));

            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

    private Object castToRequiredType(Path<Object> field, String value) {
        if (field.getJavaType().isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (field.getJavaType().isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (field.getJavaType().isAssignableFrom(java.sql.Timestamp.class)) {
            return java.sql.Timestamp.valueOf(value);
        } else if (field.getJavaType().isAssignableFrom(LocalDateTime.class)) {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        }
        return value;
    }

    private Object castToRequiredType(Path<Object> field, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(field, s));
        }
        return lists;
    }

    public Specification<T> getSpecificationFromFilters(List<Filter> filters) {
        Specification<T> specification = where(createSpecification(filters.remove(0)));
        for (Filter filter : filters) {
            specification = specification.and(createSpecification(filter));
        }
        return specification;
    }
}
