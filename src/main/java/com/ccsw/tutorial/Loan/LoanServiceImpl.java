package com.ccsw.tutorial.Loan;

import com.ccsw.tutorial.Loan.model.Loan;
import com.ccsw.tutorial.Loan.model.LoanDto;
import com.ccsw.tutorial.Loan.model.LoanSearchDto;
import com.ccsw.tutorial.author.LoanService;
import com.ccsw.tutorial.client.ClientRepository;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.GameRepository;
import com.ccsw.tutorial.game.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
        return this.loanRepository.findAll(dto.getPageable().getPageable());
    }

    @Override
    public void save(Long id, LoanDto dto) {
        Loan loan;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.loanRepository.findById(id).orElse(null);
        }

        loan.setStartDate(dto.getStartDate());
        loan.setEndDate(dto.getEndDate());

        // getting the client and the game related with this Loan.
        Client client = this.clientRepository.findById(dto.getClient().getId()).orElse(null);
        Game game = this.gameRepository.findById(dto.getGame().getId()).orElse(null);

        loan.setClient(client);
        loan.setGame(game);

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
