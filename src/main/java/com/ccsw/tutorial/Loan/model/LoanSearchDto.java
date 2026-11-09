package com.ccsw.tutorial.Loan.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class LoanSearchDto {

    private PageableRequest pageable;
    private Long idGame;
    private Long idClient;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }

    public Long getIdGame() {
        return idGame;
    }

    public void setIdGame(Long idGame) {
        this.idGame = idGame;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
