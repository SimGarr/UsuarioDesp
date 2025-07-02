package com.proyecto1.principal.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.proyecto1.principal.model.Usuario;
import com.proyecto1.principal.model.dto.UsuarioDto;
import com.proyecto1.principal.model.entity.UsuarioEntity;
import com.proyecto1.principal.repository.UsuarioRepository;

class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService; // # Inyecta los mocks dentro de UsuarioService

    @Mock
    private UsuarioRepository usuarioRepository; // # Simula el repositorio de usuario

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // # Inicializa los mocks antes de cada test
    }

    @Test
    void testCrearUsuario_CorreoNoExiste() {
        // # Simula un usuario nuevo
        Usuario usuario = new Usuario(1, "Juan", "Perez", "juan@mail.com", "1234");

        // # Simula que el correo NO existe en la BD
        when(usuarioRepository.existsByCorreo(usuario.getCorreo())).thenReturn(false);

        // # Llama al método a probar
        String resultado = usuarioService.crearUsuario(usuario);

        // # Verifica que se creó correctamente
        assertEquals("Usuario creado correctamente", resultado);

        // # Verifica que se haya guardado
        verify(usuarioRepository).save(any(UsuarioEntity.class));
    }

    @Test
    void testCrearUsuario_CorreoExiste() {
        // # Simula un usuario con correo ya existente
        Usuario usuario = new Usuario(1, "Juan", "Perez", "juan@mail.com", "1234");

        // # Simula que el correo ya existe
        when(usuarioRepository.existsByCorreo(usuario.getCorreo())).thenReturn(true);

        // # Llama al método a probar
        String resultado = usuarioService.crearUsuario(usuario);

        // # Verifica que se devuelva el mensaje de existencia
        assertEquals("el correo ya existe", resultado);

        // # Verifica que NO se haya guardado el usuario
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testEliminarUsuario_Existe() {
        // # Simula un usuario existente
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);

        // # Simula que se encuentra el usuario por ID
        when(usuarioRepository.findByIdUsuario(1)).thenReturn(usuarioEntity);

        // # Ejecuta el método
        boolean resultado = usuarioService.eliminarUsuario(1);

        // # Verifica que se eliminó correctamente
        assertTrue(resultado);
        verify(usuarioRepository).delete(usuarioEntity);
    }

    @Test
    void testEliminarUsuario_NoExiste() {
        // # Simula que no se encuentra el usuario
        when(usuarioRepository.findByIdUsuario(1)).thenReturn(null);

        // # Ejecuta el método
        boolean resultado = usuarioService.eliminarUsuario(1);

        // # Verifica que NO se eliminó
        assertFalse(resultado);
        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    void testObtenerUsuario_Existe() {
        // # Crea y configura un entity con datos simulados
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(1);
        entity.setNombre("Juan");
        entity.setApellidos("Perez");
        entity.setCorreo("juan@mail.com");
        entity.setContrasena("1234");

        // # Simula la búsqueda por correo
        when(usuarioRepository.findByCorreo("juan@mail.com")).thenReturn(entity);

        // # Ejecuta el método
        Usuario usuario = usuarioService.obtenerUsuario("juan@mail.com");

        // # Verifica que no sea null y los datos sean correctos
        assertNotNull(usuario);
        assertEquals("Juan", usuario.getNombre());
    }

    @Test
    void testObtenerUsuario_NoExiste() {
        // # Simula que el usuario no se encuentra por correo
        when(usuarioRepository.findByCorreo("noexiste@mail.com")).thenReturn(null);

        // # Ejecuta el método
        Usuario usuario = usuarioService.obtenerUsuario("noexiste@mail.com");

        // # Verifica que devuelva null
        assertNull(usuario);
    }

    @Test
    void testModificarUsuario_Existe() {
        // # Crea un usuario con nuevos datos
        Usuario usuario = new Usuario(1, "Carlos", "Gomez", "carlos@mail.com", "abcd");

        // # Crea un entity existente para simular modificación
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(1);

        // # Simula que se encuentra el usuario por ID
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(entity));

        // # Ejecuta la modificación
        String resultado = usuarioService.modificarUsuario(usuario);

        // # Verifica que se modificó correctamente
        assertEquals("Usuario modificado correctamente", resultado);
        verify(usuarioRepository).save(entity);
    }

    @Test
    void testModificarUsuario_NoExiste() {
        // # Crea usuario que se intentará modificar
        Usuario usuario = new Usuario(1, "Carlos", "Gomez", "carlos@mail.com", "abcd");

        // # Simula que no se encuentra el usuario por ID
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        // # Ejecuta la modificación
        String resultado = usuarioService.modificarUsuario(usuario);

        // # Verifica que se devuelva el mensaje de no encontrado
        assertEquals("Usuario no encontrado con ID: 1", resultado);

        // # Verifica que no se guarde nada
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testObtenerUsuarioDto_Existe() {
        // # Crea un usuario con datos para el DTO
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(1);
        entity.setNombre("Ana");
        entity.setApellidos("Lopez");
        entity.setCorreo("ana@mail.com");

        // # Simula la búsqueda por ID
        when(usuarioRepository.findByIdUsuario(1)).thenReturn(entity);

        // # Ejecuta el método
        UsuarioDto dto = usuarioService.obtenerUsuarioDto(1);

        // # Verifica que no sea null y que los datos coincidan
        assertNotNull(dto);
        assertEquals("Ana", dto.getNombre());
    }

    @Test
    void testObtenerUsuarioDto_NoExiste() {
        // # Simula que no existe el usuario por ID
        when(usuarioRepository.findByIdUsuario(1)).thenReturn(null);

        // # Ejecuta el método
        UsuarioDto dto = usuarioService.obtenerUsuarioDto(1);

        // # Verifica que el resultado sea null
        assertNull(dto);
    }

    @Test
    void testObtenerTodosLosUsuarios() {
        // # Crea 2 usuarios simulados
        UsuarioEntity u1 = new UsuarioEntity();
        UsuarioEntity u2 = new UsuarioEntity();

        // # Simula que la BD retorna una lista con ambos
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        // # Ejecuta el método
        List<UsuarioEntity> lista = usuarioService.obtenerTodosLosUsuarios();

        // # Verifica que se devuelvan los 2 usuarios
        assertEquals(2, lista.size());
    }
}
