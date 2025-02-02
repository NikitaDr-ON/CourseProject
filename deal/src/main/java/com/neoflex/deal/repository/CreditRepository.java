package com.neoflex.deal.repository;

import com.neoflex.deal.entity.Credit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CreditRepository extends CrudRepository<Credit, UUID> {
}
