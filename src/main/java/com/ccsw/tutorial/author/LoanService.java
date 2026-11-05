package com.ccsw.tutorial.author;

import com.ccsw.tutorial.Loan.model.Loan;
import com.ccsw.tutorial.Loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LoanService {

    Loan get(Long id);

    List<Loan> findAll();

    Page<Loan> findPage(LoanSearchDto dto);
}
