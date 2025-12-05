package MOTOAPPAPI.MotoAppApi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "registro")
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "registro_id", columnDefinition = "BINARY(16)")
    private UUID registroId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", columnDefinition = "BINARY(16)")
    private Usuario usuario;

    private LocalDate data;
    private String plataforma;

    @Column(name = "plataforma_outro")
    private String plataformaOutro;

    private int horasTrabalhadas;
    private int corridasRealizadas;
    private BigDecimal valorBruto;

    private int kmInicial;

    @Column(name = "km_final")
    private int kmFinal;

    private BigDecimal combustivel;
    private BigDecimal alimentacao;
    private BigDecimal gastosAdicionais;
    private String observacao;

    // CORRIGIDO: double → BigDecimal
    private BigDecimal lucro;

    // CALCULA ANTES DE SALVAR
    @PrePersist
    @PreUpdate
    public void calcularLucro() {
        BigDecimal totalDespesas = BigDecimal.ZERO;

        if (combustivel != null) totalDespesas = totalDespesas.add(combustivel);
        if (alimentacao != null) totalDespesas = totalDespesas.add(alimentacao);
        if (gastosAdicionais != null) totalDespesas = totalDespesas.add(gastosAdicionais);

        this.lucro = valorBruto.subtract(totalDespesas);
    }

    // GETTER para kmRodados (calculado)
    public int getKmRodados() {
        return kmFinal - kmInicial;
    }

}
