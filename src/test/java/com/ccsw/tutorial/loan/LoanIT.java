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
    private static int PAGE_SIZE = 5;

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
}
