package com.neoflex.CourseProject.service;

import com.neoflex.CourseProject.dto.CreditDto;
import com.neoflex.CourseProject.dto.ScoringDataDto;

public interface Scoring {
    public CreditDto scoring(ScoringDataDto scoringDataDto);
}
