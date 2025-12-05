package MOTOAPPAPI.MotoAppApi.controller;

import MOTOAPPAPI.MotoAppApi.model.Usuario;
import MOTOAPPAPI.MotoAppApi.repository.UsuarioRepository;
import MOTOAPPAPI.MotoAppApi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService  usuarioService;
    private final UsuarioRepository usuarioRepository;


    @Autowired
    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;

    }

    //Criar Usuario
    @PostMapping("/post")
    public Usuario save(@RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario);
    }



    @GetMapping("/{id}")
    public Usuario fyndBy_usuarioId(@PathVariable("id") UUID usuarioId) {

        return usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("USUARIO NÃO ENCONTRADO"));
    }


    //GET TODOS (TESTE)
    @GetMapping("/TODOS")
    public List<Usuario> listarTodos() {

        return usuarioRepository.findAll();
    }




    // Login
    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario) {
        return usuarioService.login(usuario.getEmail(), usuario.getSenha());
    }
}
