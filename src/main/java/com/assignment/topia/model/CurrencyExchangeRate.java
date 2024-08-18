package com.assignment.topia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "exchange_rates", uniqueConstraints = { @UniqueConstraint(columnNames = { "date", "currency" }) })
public class CurrencyExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String currency;

    String value;

    String date;
}

