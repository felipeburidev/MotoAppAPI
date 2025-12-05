package MOTOAPPAPI.MotoAppApi.repository;

import MOTOAPPAPI.MotoAppApi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository  extends JpaRepository<Usuario, UUID> {
    boolean existsByEmail(String email);
    Usuario findByEmail(String email);
}
