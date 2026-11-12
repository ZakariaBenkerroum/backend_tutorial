package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.Loan.model.LoanDto;
import com.ccsw.tutorial.Loan.model.LoanSearchDto;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.game.model.GameDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    private static final int TOTAL_LOANS = 6;
    private static final Long MODIFY_LOAN_ID = 1L;
    private static int PAGE_SIZE = 5;
    private static Long DELETE_LOAN_ID = 1L;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<ResponsePage<LoanDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<LoanDto>>() {
    };
    ParameterizedTypeReference<List<LoanDto>> responseTypeList = new ParameterizedTypeReference<List<LoanDto>>() {
    };

    @Test
    public void findAllSouldReturnAllLoan() {
        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseTypeList);

        assertNotNull(response);
        assertEquals(6, response.getBody().size());
    }

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResults() {

        int elementsCount = TOTAL_LOANS - PAGE_SIZE;

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void findWithExistsClientIdShouldReturnLoansOfClient() {

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));
        searchDto.setIdClient(1L);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    public void findWithDateBetweenLoanDatesShouldReturnLoan() {
        int LOANS_WITH_FILTER = 1;

        LoanSearchDto dto = new LoanSearchDto();
        dto.setPageable(new PageableRequest(0, PAGE_SIZE));

        LocalDate searchDate = LocalDate.parse("2026-11-03");
        dto.setDate(searchDate);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(dto), responseTypePage);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getContent().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewLoan() {
        long newLoanId = TOTAL_LOANS + 1;
        long newLoanSize = TOTAL_LOANS + 1;

        LoanDto dto = new LoanDto();
        ClientDto clientDto = new ClientDto();
        GameDto gameDto = new GameDto();

        clientDto.setId(1L);
        gameDto.setId(3L);

        dto.setClient(clientDto);
        dto.setGame(gameDto);
        dto.setStartDate(LocalDate.parse("2026-11-05"));
        dto.setEndDate(LocalDate.of(2026, 11, 12));

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, (int) newLoanSize));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newLoanSize, response.getBody().getTotalElements());

    }

    @Test
    public void modifyWithExistIdShouldModifyLoan() {

        LoanDto dto = new LoanDto();
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);

        dto.setId(MODIFY_LOAN_ID);
        dto.setClient(clientDto);
        dto.setGame(gameDto);
        dto.setStartDate(LocalDate.of(2026, 10, 10));
        dto.setEndDate(LocalDate.of(2026, 10, 13));

        ResponseEntity<Void> saveResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_LOAN_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
        assertEquals(HttpStatus.OK, saveResponse.getStatusCode());

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());

        LoanDto loan = response.getBody().getContent().stream().filter(item -> item.getId().equals(MODIFY_LOAN_ID)).findFirst().orElse(null);
        assertNotNull(loan);
        assertEquals(LocalDate.of(2026, 10, 10), loan.getStartDate());
        assertEquals(LocalDate.of(2026, 10, 13), loan.getEndDate());
    }

    @Test
    public void modifyShouldNotCountCurrentLoanWhenCheckingClientOverlap() {
        LoanDto dto = createLoanDto(1L, 1L, "2020-10-02", "2020-10-04");

        ResponseEntity<Void> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_LOAN_ID,
                HttpMethod.PUT,
                new HttpEntity<>(dto),
                Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void saveShouldRejectSameGameOnAnOverlappingDate() {
        LoanDto dto = createLoanDto(3L, 2L, "2026-11-06", "2026-11-10");

        ResponseEntity<Void> response = saveLoan(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void saveShouldRejectThirdConcurrentLoanForClient() {
        assertEquals(HttpStatus.OK, saveLoan(createLoanDto(3L, 1L, "2026-12-01", "2026-12-05")).getStatusCode());
        assertEquals(HttpStatus.OK, saveLoan(createLoanDto(3L, 2L, "2026-12-01", "2026-12-05")).getStatusCode());

        ResponseEntity<Void> response = saveLoan(createLoanDto(3L, 3L, "2026-12-05", "2026-12-08"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void saveShouldAllowTwoLoansThatDoNotOverlapOnAnyDay() {
        assertEquals(HttpStatus.OK, saveLoan(createLoanDto(3L, 1L, "2026-12-01", "2026-12-02")).getStatusCode());
        assertEquals(HttpStatus.OK, saveLoan(createLoanDto(3L, 2L, "2026-12-05", "2026-12-06")).getStatusCode());

        ResponseEntity<Void> response = saveLoan(createLoanDto(3L, 3L, "2026-12-01", "2026-12-06"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void modifyWithNotExistIdShouldThrowException() {

        Long NOT_EXISTS_GAME_ID = 0L;
        LoanDto dto = new LoanDto();
        dto.setId(NOT_EXISTS_GAME_ID);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_GAME_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteWithExistsIdSHouldDeleteLoan() {
        long newLoanSize = TOTAL_LOANS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_LOAN_ID, HttpMethod.DELETE, null, Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newLoanSize, response.getBody().getTotalElements());
    }

    @Test
    public void deleteWithNoExistsIdShouldThrowException() {
        long deleteLoanId = TOTAL_LOANS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deleteLoanId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private LoanDto createLoanDto(Long clientId, Long gameId, String startDate, String endDate) {
        LoanDto dto = new LoanDto();
        ClientDto clientDto = new ClientDto();
        clientDto.setId(clientId);
        GameDto gameDto = new GameDto();
        gameDto.setId(gameId);

        dto.setClient(clientDto);
        dto.setGame(gameDto);
        dto.setStartDate(LocalDate.parse(startDate));
        dto.setEndDate(LocalDate.parse(endDate));
        return dto;
    }

    private ResponseEntity<Void> saveLoan(LoanDto dto) {
        return restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);
    }
}
