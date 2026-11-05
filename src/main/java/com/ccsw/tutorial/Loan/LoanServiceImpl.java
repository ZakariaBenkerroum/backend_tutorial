package com.ccsw.tutorial.Loan;

import com.ccsw.tutorial.Loan.model.Loan;
import com.ccsw.tutorial.Loan.model.LoanSearchDto;
import com.ccsw.tutorial.author.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Override
    public Loan get(Long id) {
        return this.loanRepository.findById(id).orElse(null);
    }

    @Override
    public List<Loan> findAll() {
        return (List<Loan>) this.loanRepository.findAll();
    }

    @Override
    public Page<Loan> findPage(LoanSearchDto dto) {
        return this.loanRepository.findAll(dto.getPageable().getPageable());
    }
}
