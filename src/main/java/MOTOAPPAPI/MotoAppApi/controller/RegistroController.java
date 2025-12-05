package MOTOAPPAPI.MotoAppApi.controller;

import MOTOAPPAPI.MotoAppApi.model.Registro;
import MOTOAPPAPI.MotoAppApi.model.Usuario;
import MOTOAPPAPI.MotoAppApi.repository.RegistroRepository;
import MOTOAPPAPI.MotoAppApi.repository.UsuarioRepository;
import MOTOAPPAPI.MotoAppApi.service.RegistroService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/registros")
@CrossOrigin("*")
public class RegistroController {

    private final RegistroService registroService;
    private final UsuarioRepository usuarioRepository;
    private final RegistroRepository registroRepository;

    public RegistroController(RegistroService registroService, UsuarioRepository usuarioRepository, RegistroRepository registroRepository) {
        this.registroService = registroService;
        this.usuarioRepository = usuarioRepository;
        this.registroRepository = registroRepository;
    }

    //Criar Registro
    @PostMapping("/{usuarioId}")
    public Registro criarRegistro(@PathVariable UUID usuarioId, @RequestBody Registro registro) {
        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        System.out.println("=== DEBUG CONTROLLER ===");
        System.out.println("UsuarioId no PATH: " + usuarioId);
        System.out.println("Registro recebido:");
        System.out.println(" - Data: " + registro.getData());
        System.out.println(" - Plataforma: " + registro.getPlataforma());
        System.out.println(" - Lucro: " + registro.getLucro());
        return registroService.criarRegistro(usuarioId, registro);
    }

    @PostMapping("/post/{usuarioId}")
    public Registro registrar(@PathVariable UUID usuarioId, @RequestBody Registro registro){
        return  registroService.criarRegistro(usuarioId, registro);
    }

    @GetMapping("/todos/{usuarioId}")
    public List<Registro> findAllByUsuario(@PathVariable UUID usuarioId){
        System.out.println("=== DEBUG BUSCA REGISTROS ===");
        System.out.println("Buscando registros para usuário: " + usuarioId);

        // Lista TODOS os registros para ver o que tem
        List<Registro> todosRegistros = registroRepository.findAll();
        System.out.println("Total de registros no banco: " + todosRegistros.size());

        todosRegistros.forEach(r -> {
            if (r.getUsuario() != null) {
                System.out.println("Registro ID: " + r.getRegistroId() + " - Usuário: " + r.getUsuario().getUsuarioId());
            } else {
                System.out.println("Registro ID: " + r.getRegistroId() + " - Usuário: NULL");
            }
        });

        // Agora busca pelo usuário específico
        List<Registro> registrosDoUsuario = registroRepository.findByUsuarioUsuarioId(usuarioId);
        System.out.println("Registros encontrados para o usuário: " + registrosDoUsuario.size());

        return registrosDoUsuario;
    }

    // Atualizar Registro
    @PutMapping("/alter/{registroId}")
    public Registro atualizarRegistro(@PathVariable UUID registroId, @RequestBody Registro registroAtualizado) {
        return registroRepository.findById(registroId)
                .map(registroExistente -> {
                    // Atualiza apenas campos não nulos
                    if (registroAtualizado.getData() != null) {
                        registroExistente.setData(registroAtualizado.getData());
                    }
                    if (registroAtualizado.getPlataforma() != null) {
                        registroExistente.setPlataforma(registroAtualizado.getPlataforma());
                    }
                    if (registroAtualizado.getPlataformaOutro() != null) {
                        registroExistente.setPlataformaOutro(registroAtualizado.getPlataformaOutro());
                    }
                    if (registroAtualizado.getHorasTrabalhadas() != 0) {
                        registroExistente.setHorasTrabalhadas(registroAtualizado.getHorasTrabalhadas());
                    }
                    if (registroAtualizado.getCorridasRealizadas() != 0) {
                        registroExistente.setCorridasRealizadas(registroAtualizado.getCorridasRealizadas());
                    }
                    if (registroAtualizado.getValorBruto() != null) {
                        registroExistente.setValorBruto(registroAtualizado.getValorBruto());
                    }
                    if (registroAtualizado.getKmInicial() != 0) {
                        registroExistente.setKmInicial(registroAtualizado.getKmInicial());
                    }
                    if (registroAtualizado.getKmFinal() != 0) {
                        registroExistente.setKmFinal(registroAtualizado.getKmFinal());
                    }
                    if (registroAtualizado.getCombustivel() != null) {
                        registroExistente.setCombustivel(registroAtualizado.getCombustivel());
                    }
                    if (registroAtualizado.getAlimentacao() != null) {
                        registroExistente.setAlimentacao(registroAtualizado.getAlimentacao());
                    }
                    if (registroAtualizado.getGastosAdicionais() != null) {
                        registroExistente.setGastosAdicionais(registroAtualizado.getGastosAdicionais());
                    }
                    if (registroAtualizado.getObservacao() != null) {
                        registroExistente.setObservacao(registroAtualizado.getObservacao());
                    }

                    // Recalcula o lucro automaticamente
                    registroExistente.calcularLucro();

                    return registroRepository.save(registroExistente);
                })
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
    }

    // Deletar Registro
    @DeleteMapping("/delete/{registroId}")
    public String deletarRegistro(@PathVariable UUID registroId) {
        registroService.deletarRegistro(registroId);
        return "Registro deletado com sucesso";
    }

    @GetMapping
    public List<Registro> todosregistros(){

        return registroRepository.findAll();
    }
}
