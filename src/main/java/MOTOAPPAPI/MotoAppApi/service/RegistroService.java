package MOTOAPPAPI.MotoAppApi.service;

import MOTOAPPAPI.MotoAppApi.model.Registro;
import MOTOAPPAPI.MotoAppApi.model.Usuario;
import MOTOAPPAPI.MotoAppApi.repository.RegistroRepository;
import MOTOAPPAPI.MotoAppApi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; // ← IMPORTANTE: Adicione este import

@Service
public class RegistroService {
    private final RegistroRepository registroRepository;
    private final UsuarioRepository usuarioRepository;

    public RegistroService(RegistroRepository registroRepository, UsuarioRepository usuarioRepository) {
        this.registroRepository = registroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Criar novo Registro - COM WORKAROUND
    public Registro criarRegistro(UUID usuarioId, Registro registro) {
        System.out.println("=== DEBUG CRIAR REGISTRO ===");
        System.out.println("Usuario ID: " + usuarioId);

        // WORKAROUND: Carrega todos e filtra manualmente
        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        System.out.println("Total de usuários: " + todosUsuarios.size());

        Usuario usuario = todosUsuarios.stream()
                .filter(u -> {
                    System.out.println("Comparando: " + u.getUsuarioId() + " com " + usuarioId);
                    return u.getUsuarioId().equals(usuarioId);
                })
                .findFirst()
                .orElseThrow(() -> {
                    System.out.println("USUÁRIO NÃO ENCONTRADO!");
                    return new RuntimeException("Usuário não encontrado");
                });

        System.out.println("Usuário encontrado: " + usuario.getNome());
        registro.setUsuario(usuario);

        Registro saved = registroRepository.save(registro);
        System.out.println("Registro salvo com ID: " + saved.getRegistroId());
        System.out.println("Usuário no registro salvo: " + (saved.getUsuario() != null ? saved.getUsuario().getUsuarioId() : "NULL"));

        return saved;
    }

    // Listar registros por usuário
    public List<Registro> listarPorUsuario(UUID usuarioId) {
        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        Usuario usuario = todosUsuarios.stream()
                .filter(u -> u.getUsuarioId().equals(usuarioId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return registroRepository.findByUsuario(usuario);
    }

    // REMOVA ESTE MÉTODO DUPLICADO ↓
    /*
    // Buscar registros por intervalo de datas - MÉTODO ORIGINAL (REMOVER)
    public List<Registro> listarPorData(UUID usuarioId, LocalDate inicio, LocalDate fim) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return registroRepository.findByUsuarioAndDataBetween(usuario, inicio, fim);
    }
    */

    // MANTENHA APENAS ESTE - MÉTODO ATUALIZADO PARA DASHBOARD
    public List<Registro> listarPorData(UUID usuarioId, LocalDate inicio, LocalDate fim) {
        // WORKAROUND: Carrega todos e filtra manualmente
        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        Usuario usuario = todosUsuarios.stream()
                .filter(u -> u.getUsuarioId().equals(usuarioId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Filtra por data
        List<Registro> registrosDoUsuario = registroRepository.findByUsuario(usuario);

        return registrosDoUsuario.stream()
                .filter(registro -> !registro.getData().isBefore(inicio) && !registro.getData().isAfter(fim))
                .collect(Collectors.toList());
    }

    // Excluir registro
    public void deletarRegistro(UUID registroId) {
        if (!registroRepository.existsById(registroId)) {
            throw new RuntimeException("Registro não encontrado.");
        }
        registroRepository.deleteById(registroId);
    }
}