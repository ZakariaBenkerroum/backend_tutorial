package com.ccsw.tutorial.Loan;

import com.ccsw.tutorial.Loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long> {
    Page<Loan> findAll(Pageable pageable);
}
