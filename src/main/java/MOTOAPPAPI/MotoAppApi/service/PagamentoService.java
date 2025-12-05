package MOTOAPPAPI.MotoAppApi.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PagamentoService {

    // 🔗 Versão SIMPLES - retorna URL fake para testes
    public String criarLinkPagamento(UUID usuarioId, String emailUsuario) {
        try {
            // URL fake do Mercado Pago para testes
            String urlFake = "https://www.mercadopago.com.br/checkout/v1/redirect?pref_id=TEST-" +
                    usuarioId.toString() +
                    "&email=" + emailUsuario;

            return urlFake;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar pagamento: " + e.getMessage());
        }
    }
}