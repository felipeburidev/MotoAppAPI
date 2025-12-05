package MOTOAPPAPI.MotoAppApi.controller;

import MOTOAPPAPI.MotoAppApi.model.Usuario;
import MOTOAPPAPI.MotoAppApi.service.PagamentoService;
import MOTOAPPAPI.MotoAppApi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
@CrossOrigin("*")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private UsuarioService usuarioService;

    // 🔗 Criar link de pagamento
    @PostMapping("/criar/{usuarioId}")
    public String criarPagamento(@PathVariable UUID usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        return pagamentoService.criarLinkPagamento(usuarioId, usuario.getEmail());
    }

    // ✅ Verificar status do pagamento
    @GetMapping("/status/{usuarioId}")
    public ResponseEntity<?> verificarStatus(@PathVariable UUID usuarioId) {
        try {
            Usuario usuario = usuarioService.buscarPorId(usuarioId);

            // Se for null, retorna PENDENTE como padrão
            String status = (usuario.getStatusPagamento() != null)
                    ? usuario.getStatusPagamento().toString()
                    : "PENDENTE";

            return ResponseEntity.ok(status);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao verificar status: " + e.getMessage());
        }
    }
}