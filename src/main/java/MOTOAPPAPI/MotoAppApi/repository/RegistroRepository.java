package MOTOAPPAPI.MotoAppApi.repository;

import MOTOAPPAPI.MotoAppApi.model.Registro;
import MOTOAPPAPI.MotoAppApi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RegistroRepository extends JpaRepository<Registro, UUID> {


    List<Registro> findByUsuario(Usuario usuario);

    List<Registro> findByUsuarioAndDataBetween(
            Usuario usuario,
            LocalDate dataInicio,
            LocalDate dataFim
    );

    // Buscar todos os registros de um usuário específico
    List<Registro> findByUsuarioUsuarioId(UUID usuarioId);

    List<Registro> findByUsuarioOrderByDataAsc(Usuario usuario);

}
