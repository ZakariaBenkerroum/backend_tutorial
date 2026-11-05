package com.ccsw.tutorial.Loan;

import com.ccsw.tutorial.Loan.model.Loan;
import com.ccsw.tutorial.Loan.model.LoanDto;
import com.ccsw.tutorial.Loan.model.LoanSearchDto;
import com.ccsw.tutorial.author.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(path = "/loan")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    ModelMapper mapper;

    @Operation(summary = "Find", description = "Method that find all Loans")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<LoanDto> findAll() {

        List<Loan> loans = this.loanService.findAll();

        return loans.stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Find Page", description = "Method that return a page of Loans")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<LoanDto> findPage(@RequestBody LoanSearchDto dto) {
        Page<Loan> page = this.loanService.findPage(dto);

        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }
}
