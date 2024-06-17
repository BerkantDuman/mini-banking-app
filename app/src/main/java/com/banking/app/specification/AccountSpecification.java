package com.banking.app.specification;

import com.banking.app.dto.Filter;
import com.banking.app.model.Account;
import com.banking.app.model.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AccountSpecification {
    public static Specification<Account> columnEqual(List<Filter> accountFilters, User user) {
        return new Specification<Account>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                Join<Account, User> userJoin = root.join("user");

                accountFilters.forEach(filter -> {
                    Predicate predicate = criteriaBuilder.like(
                            root.get(filter.getColumnName()),
                            "%" + filter.getColumnValue() + "%"
                    );
                    predicates.add(predicate);
                });

                Predicate userPredicate = criteriaBuilder.equal(userJoin.get("id"), user.getId());

                // Combine the result with user filter using AND
                if (!accountFilters.isEmpty()) {
                    Predicate accountFiltersCombined = criteriaBuilder.or(predicates.toArray(new Predicate[0]));
                    return criteriaBuilder.and(accountFiltersCombined, userPredicate);

                } else {
                    return userPredicate;
                }
            }

        };
    }

}