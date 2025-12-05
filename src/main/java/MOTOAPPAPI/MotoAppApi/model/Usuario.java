package MOTOAPPAPI.MotoAppApi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "usuario_id", columnDefinition = "BINARY(16)")
    private UUID usuarioId;

    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;

    // 🔐 CAMPOS NOVOS PARA PAGAMENTO (SIMPLES!)
    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento = StatusPagamento.PENDENTE;

    private String idPagamento; // ID do pagamento no Mercado Pago
    private LocalDateTime dataPagamento;
    private LocalDateTime acessoValidoAte;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Para evitar loop infinito no JSON
    private List<Registro> registros = new ArrayList<>();

}
