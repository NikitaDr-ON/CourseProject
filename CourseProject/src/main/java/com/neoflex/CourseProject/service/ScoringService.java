package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;

public interface ScoringService {
    CreditDto scoring(ScoringDataDto scoringDataDto);
}
