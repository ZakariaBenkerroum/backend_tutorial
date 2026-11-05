package com.ccsw.tutorial.Loan.model;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;

import java.time.LocalDate;

public class LoanDto {

    private Long id;
    private ClientDto client;
    private GameDto game;
    private LocalDate startDate;
    private LocalDate endDate;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientDto getClient() {
        return this.client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public GameDto getGame() {
        return this.game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
