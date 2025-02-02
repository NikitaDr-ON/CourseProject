package com.neoflex.statement.service;

import com.neoflex.statement.dto.LoanOfferDto;
import com.neoflex.statement.dto.LoanStatementRequestDto;
import com.neoflex.statement.model.PostServiceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
public class PostServiceImpl implements  PostService{

    private final PostServiceProperties postServiceProperties;

    @Autowired
    public PostServiceImpl(PostServiceProperties postServiceProperties){
        this.postServiceProperties = postServiceProperties;
    }

    @Override
    public  List<LoanOfferDto> postDealStatement(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("отправление post запроса с LoanStatementRequestDto: {}", loanStatementRequestDto);
        RestClient restClient = RestClient.create();
        List<LoanOfferDto> loanOfferDtoList = restClient.post()
                .uri(postServiceProperties.getDealStatementUrl())
                .contentType(APPLICATION_JSON)
                .body(loanStatementRequestDto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        log.info("ответ на post запрос List<LoanOfferDto>: {}", loanOfferDtoList);
        return loanOfferDtoList;
    }

    @Override
    public void postDealOfferSelect(LoanOfferDto loanOfferDto) {
        log.info("отправление post запроса с loanOfferDto: {}", loanOfferDto);
        RestClient restClient = RestClient.create();
        restClient.post()
                .uri(postServiceProperties.getDealOfferSelectUrl())
                .contentType(APPLICATION_JSON)
                .body(loanOfferDto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

}
