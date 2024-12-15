package com.neoflex.deal.service;

import com.neoflex.deal.dto.CreditDto;
import com.neoflex.deal.dto.LoanOfferDto;
import com.neoflex.deal.dto.LoanStatementRequestDto;
import com.neoflex.deal.dto.ScoringDataDto;
import com.neoflex.deal.model.PostServiceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostServiceProperties postServiceProperties;

    @Autowired
    public PostServiceImpl(PostServiceProperties postServiceProperties) {
        this.postServiceProperties = postServiceProperties;
    }

    public List<LoanOfferDto> postOffers(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("отправление post запроса с LoanStatementRequestDto: {}", loanStatementRequestDto);
        RestClient restClient = RestClient.create();
        List<LoanOfferDto> loanOfferDtoList = restClient.post()
                .uri(postServiceProperties.getCalculatorOffers())
                .contentType(APPLICATION_JSON)
                .body(loanStatementRequestDto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        log.info("ответ на post запрос List<LoanOfferDto>: {}", loanOfferDtoList);
        return loanOfferDtoList;
    }

    public CreditDto postLoanOfferDto(ScoringDataDto scoringDataDto) {
        log.info("отправление post запроса с ScoringDataDto: {}", scoringDataDto);
        RestClient restClient = RestClient.create();
        CreditDto creditDto = restClient.post()
                .uri(postServiceProperties.getCalculatorCalc())
                .contentType(APPLICATION_JSON)
                .body(scoringDataDto)
                .retrieve()
                .body(CreditDto.class);
        log.info("ответ на post запрос CreditDto>: {}", creditDto);
        return creditDto;
    }

}
