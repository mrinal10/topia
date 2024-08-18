package com.assignment.topia.repository;

import com.assignment.topia.model.CurrencyExchangeRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CurrencyExchangeRateRepo extends CrudRepository<CurrencyExchangeRate, UUID> {

    public CurrencyExchangeRate findDistinctFirstByDateAndCurrency(String date, String currency);
}
