package ru.realtorcrm.price_history.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.realtorcrm.price_history.enums.SourceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "estates_history", uniqueConstraints = @UniqueConstraint(columnNames = {"cadastre", "source", "load_date"}))
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EstateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "load_date", nullable = false)
    private LocalDateTime loadDate;

    @Column(name = "cadastre", nullable = false)
    private String cadastre;
    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private SourceType source;

    private BigDecimal square;
    private BigDecimal price;
    private String type;
}
