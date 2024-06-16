package com.banking.app.specification;

import com.banking.app.dto.Filter;
import com.banking.app.model.Account;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AccountSpecification {
    public static Specification<Account> columnEqual(List<Filter> accountFilters) {
        return new Specification<Account>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                accountFilters.forEach(filter -> {
                    Predicate predicate = criteriaBuilder.like(
                            root.get(filter.getColumnName()),
                            "%" + filter.getColumnValue() + "%"
                    );
                    predicates.add(predicate);
                });

                return predicates.isEmpty()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }

        };
    }

}