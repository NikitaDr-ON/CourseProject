package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.pojo.PreScoringProperties;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mock testing the PreScoringService")
class PreScoringServiceTest {

    @Mock
    PreScoringProperties preScoringProperties;

    @InjectMocks
    private PreScoringServiceImpl implPreScoringService;

    @BeforeEach
    void setUp() {
        when(preScoringProperties.getPointsForInsurance()).thenReturn(3);
        when(preScoringProperties.getPointsForSalaryClient()).thenReturn(1);
        when(preScoringProperties.getDefaultRate()).thenReturn(20);
        when(preScoringProperties.getInsurancePrice()).thenReturn(20000);
    }

    @Test
    @DisplayName("Mock testing the method resultsOfPreScoring for the number of returned objects")
    void resultsOfPreScoring_shouldReturnArrayOf4(){
        Assertions.assertEquals(4, implPreScoringService.resultsOfPreScoring(BigDecimal.valueOf(200000)).size());
    }

    @Test
    @DisplayName("Mock testing the method resultsOfPreScoring for equality of objects")
    void resultsOfPreScoring_shouldBeEquals(){
        Assertions.assertEquals(BigDecimal.valueOf(24), implPreScoringService.resultsOfPreScoring(BigDecimal.valueOf(200000)).get(0).getRate());
        Assertions.assertEquals(BigDecimal.valueOf(22), implPreScoringService.resultsOfPreScoring(BigDecimal.valueOf(200000)).get(1).getRate());
        Assertions.assertEquals(BigDecimal.valueOf(18), implPreScoringService.resultsOfPreScoring(BigDecimal.valueOf(200000)).get(2).getRate());
        Assertions.assertEquals(BigDecimal.valueOf(16), implPreScoringService.resultsOfPreScoring(BigDecimal.valueOf(200000)).get(3).getRate());
    }
}