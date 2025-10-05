package ru.realtorcrm.content_processor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.realtorcrm.content_processor.enums.SourceType;

import java.math.BigDecimal;

@Entity
@Table(name = "estates",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cadastre", "source"}))
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Estate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cadastre", nullable = false)
    private String cadastre;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private SourceType source;

    private String type;
    private BigDecimal square;
    private BigDecimal price;
}
