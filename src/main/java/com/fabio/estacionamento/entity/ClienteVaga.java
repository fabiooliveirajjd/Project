package com.fabio.estacionamento.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

// Essa é a classe de relacionamento entre as entidades Cliente e Vaga
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes_tem_vagas")
@EntityListeners(AuditingEntityListener.class)
public class ClienteVaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "numero_recibo", nullable = false, unique = true, length = 15) // obrigatório, único e com no máximo 15 caracteres
    private String recibo;
    @Column(name = "placa", nullable = false, length = 8)
    private String placa;
    @Column(name = "marca", nullable = false, length = 45)
    private String marca;
    @Column(name = "modelo", nullable = false, length = 45)
    private String modelo;
    @Column(name = "cor", nullable = false, length = 45)
    private String cor;
    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime dataEntrada;
    @Column(name = "data_saida")
    private LocalDateTime dataSaida;
    @Column(name = "valor", columnDefinition = "decimal(7,2)") // valor com 7 dígitos e 2 decimais
    private BigDecimal valor;
    @Column(name = "desconto", columnDefinition = "decimal(7,2)") // desconto com 7 dígitos e 2 decimais
    private BigDecimal desconto;
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false) // Relacionamento muitos para um com a tabela clientes
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "id_vaga", nullable = false) // Relacionamento muitos para um com a tabela vagas
    private Vaga vaga;

    // Campos de auditoria
    @CreatedDate
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    @LastModifiedDate
    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;
    @CreatedBy
    @Column(name = "criado_por")
    private String criadoPor;
    @LastModifiedBy
    @Column(name = "modificado_por")
    private String modificadoPor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClienteVaga that = (ClienteVaga) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

