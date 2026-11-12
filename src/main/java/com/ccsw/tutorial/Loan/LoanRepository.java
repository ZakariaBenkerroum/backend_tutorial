package com.ccsw.tutorial.Loan;

import com.ccsw.tutorial.Loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {
    @Override
    @EntityGraph(attributePaths = { "game", "client" })
    Page<Loan> findAll(Specification<Loan> spec, Pageable pageable);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.game.id = :gameId AND l.startDate <= :endDate AND l.endDate >= :startDate AND (:loanId IS NULL OR l.id <> :loanId)")
    long countOverlappingGameLoans(@Param("gameId") Long gameId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("loanId") Long loanId);

    @Query("""
            SELECT COUNT(l1)
            FROM Loan l1, Loan l2
            WHERE l1.client.id = :clientId
              AND l2.client.id = :clientId
              AND l1.id < l2.id
              AND (:loanId IS NULL OR l1.id <> :loanId)
              AND (:loanId IS NULL OR l2.id <> :loanId)
              AND l1.startDate <= :endDate AND l1.endDate >= :startDate
              AND l2.startDate <= :endDate AND l2.endDate >= :startDate
              AND l1.startDate <= l2.endDate AND l1.endDate >= l2.startDate
            """)
    long countOverlappingClientLoanPairs(@Param("clientId") Long clientId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("loanId") Long loanId);
}
