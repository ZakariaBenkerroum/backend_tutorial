package com.ccsw.tutorial.Loan;

import com.ccsw.tutorial.Loan.model.Loan;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.time.LocalDate;

public class LoanSpecification implements Specification<Loan> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public LoanSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // We will use the operator ":" to search with ID for Client or Game
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        }

        //Opertor "<=", used for comparing the date chosed bigger than startDate
        else if (criteria.getOperation().equalsIgnoreCase("<=") && criteria.getValue() != null) {
            Path<LocalDate> path = root.get(criteria.getKey());
            return builder.lessThanOrEqualTo(path, (LocalDate) criteria.getValue());
        }

        // Operator ">=", used to compare between the date chosed and the endDate
        else if (criteria.getOperation().equalsIgnoreCase(">=") && criteria.getValue() != null) {
            Path<LocalDate> path = root.get(criteria.getKey());
            return builder.greaterThanOrEqualTo(path, (LocalDate) criteria.getValue());
        }
        return null;
    }

    // We can note that this function is duplicated, in this tutorial i will not resolve it
    // because I wanna focus on the rest, but we can resolve it with creating a new class
    // Creating a class that has getPath as a public function, and re call it in the both of classes that use this function
    private Path<String> getPath(Root<Loan> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }
        return expression;
    }

}
