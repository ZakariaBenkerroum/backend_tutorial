package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.Loan.LoanRepository;
import com.ccsw.tutorial.Loan.LoanServiceImpl;
import com.ccsw.tutorial.Loan.model.Loan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    public static final Long EXISTS_LOAN_ID = 1L;
    public static final Long NOT_EXISTS_AUTHOR_ID = 0L;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    public void getExistsLoanIdShouldReturnLoan() {

        Loan loan = mock(Loan.class);
        when(loan.getId()).thenReturn(EXISTS_LOAN_ID);
        when(loanRepository.findById(EXISTS_LOAN_ID)).thenReturn(Optional.of(loan));

        Loan loanResponse = loanService.get(EXISTS_LOAN_ID);
    }

    @Test
    public void getNotExistsLoanIdShouldReturnNull() {

        when(loanRepository.findById(NOT_EXISTS_AUTHOR_ID)).thenReturn(Optional.empty());

        Loan loan = loanService.get(NOT_EXISTS_AUTHOR_ID);

        assertNull(loan);
    }
}
