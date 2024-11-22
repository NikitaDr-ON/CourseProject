package com.neoflex.CourseProject.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class ImplPreImplScoringServiceTest {

    @InjectMocks
    private ImplPreScoringService implPreScoringService;

    @Test
    void resultsOfPreScoring_shouldReturnArrayOf4(){
        ReflectionTestUtils.setField(implPreScoringService, "defaultRate", 20);
        Assertions.assertEquals(4, implPreScoringService.resultsOfPreScoring(BigDecimal.valueOf(200000)).size());
    }
    @Test
    void resultsOfPreScoring_shouldReturnTrue(){
        ReflectionTestUtils.setField(implPreScoringService, "defaultRate", 20);
        Assertions.assertEquals(BigDecimal.valueOf(16), implPreScoringService.resultsOfPreScoring(BigDecimal.valueOf(200000)).get(0).getRate());
        Assertions.assertEquals(BigDecimal.valueOf(18), implPreScoringService.resultsOfPreScoring(BigDecimal.valueOf(200000)).get(1).getRate());
        Assertions.assertEquals(BigDecimal.valueOf(22), implPreScoringService.resultsOfPreScoring(BigDecimal.valueOf(200000)).get(2).getRate());
        Assertions.assertEquals(BigDecimal.valueOf(24), implPreScoringService.resultsOfPreScoring(BigDecimal.valueOf(200000)).get(3).getRate());
    }
}