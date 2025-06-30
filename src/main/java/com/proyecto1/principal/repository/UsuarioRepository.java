package com.proyecto1.principal.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto1.principal.model.entity.UsuarioEntity;




@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    UsuarioEntity findByCorreo(String correo);
    Boolean existsByCorreo(String correo);
    void deleteByCorreo(String correo);
    UsuarioEntity findByIdUsuario(int idUsuario);

}
