package com.ccsw.tutorial.Loan;

import com.ccsw.tutorial.Loan.model.Loan;
import com.ccsw.tutorial.Loan.model.LoanDto;
import com.ccsw.tutorial.Loan.model.LoanSearchDto;
import com.ccsw.tutorial.author.LoanService;
import com.ccsw.tutorial.client.ClientRepository;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameRepository;
import com.ccsw.tutorial.game.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    GameRepository gameRepository;

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

        Specification<Loan> spec = Specification.where(null);

        if (dto.getIdGame() != null) {
            LoanSpecification gameSpec = new LoanSpecification(new SearchCriteria("game.id", ":", dto.getIdGame()));
            spec = spec.and(gameSpec);
        }

        if (dto.getIdClient() != null) {
            LoanSpecification clientSpec = new LoanSpecification(new SearchCriteria("client.id", ":", dto.getIdClient()));
            spec = spec.and(clientSpec);
        }

        if (dto.getDate() != null) {
            LoanSpecification startDateSpec = new LoanSpecification(new SearchCriteria("startDate", "<=", dto.getDate()));
            LoanSpecification endDateSpec = new LoanSpecification(new SearchCriteria("endDate", ">=", dto.getDate()));
            spec = spec.and(startDateSpec).and(endDateSpec);
        }

        return this.loanRepository.findAll(spec, dto.getPageable().getPageable());
    }

    @Override
    public void save(Long id, LoanDto dto) {
        Loan loan;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.loanRepository.findById(id).orElse(null);
        }

        // CHecking if the range of dates:
        if (dto.getStartDate() == null || dto.getEndDate() == null || dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rango de fechas del préstamo no es válido.");
        }

        loan.setStartDate(dto.getStartDate());
        loan.setEndDate(dto.getEndDate());

        // getting the client and the game related with this Loan.
        Client client = this.clientRepository.findById(dto.getClient().getId()).orElse(null);
        Game game = this.gameRepository.findById(dto.getGame().getId()).orElse(null);

        loan.setClient(client);
        loan.setGame(game);

        //Checking the game, if at least one loan overlap with the range of dates, it rejects the oparation
        // when we exclude the current ID, an update can save its changes without overlap
        if (loanRepository.countOverlappingGameLoans(loan.getGame().getId(), loan.getStartDate(), loan.getEndDate(), loan.getId()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El juego ya está prestado a otro cliente en esas fechas.");
        }

        long overlappingLoanPairs = loanRepository.countOverlappingClientLoanPairs(
                loan.getClient().getId(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getId());
        if (overlappingLoanPairs > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente ya tiene 2 préstamos activos en alguna fecha del rango.");
        }

        this.loanRepository.save(loan);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.loanRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exits");
        }

        this.loanRepository.deleteById(id);
    }
}
