package Bojorquez.Leonel.GamesShop.services;

import Bojorquez.Leonel.GamesShop.models.Juego;
import Bojorquez.Leonel.GamesShop.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class JuegoService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Autowired
    private JuegoRepository juegoRepository;


    public List<Juego> listarTodos(){
        return juegoRepository.findAll();
    }

    public Juego obtenerJuegoPorId(Long id){
        return juegoRepository.findById(id).orElse(null);
    }

    public void eliminarJuego(Long id){
        Juego juego = juegoRepository.findById(id).orElse(null);
        if (juego != null) {
            if (juego.getPortadaUrl() != null && !juego.getPortadaUrl().equals("default.png")) {
                try {
                    Path uploadPath = Paths.get(UPLOAD_DIR);
                    Path filePath = uploadPath.resolve(juego.getPortadaUrl());
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            juegoRepository.deleteById(id);
        }
    }

    public void guardarJuego(Juego juego, MultipartFile portada) {
        String urlImagen = juego.getPortadaUrl();

        if (portada != null && !portada.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String nombreArchivo = UUID.randomUUID().toString() + "_" + portada.getOriginalFilename();
                Path filePath = uploadPath.resolve(nombreArchivo);

                Files.copy(portada.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                urlImagen = nombreArchivo;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (urlImagen != null) {
            juego.setPortadaUrl(urlImagen);
        }

        juegoRepository.save(juego);
    }

    public List<Juego> buscarPorTitulo(String palabra){
        if(palabra == null || palabra.trim().isEmpty()){
            return listarTodos();
        }
        return juegoRepository.findByTituloContainingIgnoreCase(palabra);
    }

    public List<Juego> obtenerJuegosPorUsuario(Long usuarioId){
        return juegoRepository.findByUsuarioId(usuarioId);
    }


}
