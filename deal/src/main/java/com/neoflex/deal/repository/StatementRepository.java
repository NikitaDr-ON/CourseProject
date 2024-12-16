package com.neoflex.deal.repository;

import com.neoflex.deal.entity.Statement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatementRepository extends CrudRepository<Statement, UUID> {
}
