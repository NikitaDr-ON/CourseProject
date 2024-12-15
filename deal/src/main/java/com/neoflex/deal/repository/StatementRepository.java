package com.neoflex.deal.repository;

import com.neoflex.deal.entity.Statement;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AutoConfiguration
public interface StatementRepository extends CrudRepository<Statement, UUID> {
}
