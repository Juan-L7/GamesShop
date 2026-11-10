package Bojorquez.Leonel.GamesShop.repository;

import Bojorquez.Leonel.GamesShop.models.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego,Long> {

    List<Juego> findByTituloContainingIgnoreCase(String titulo);

    List<Juego> findByDescripcionContainingIgnoreCase(String descripcion);

    List<Juego> findByTituloStartingWithIgnoreCase(String letra);

    List<Juego> findByUsuarioId(Long usuarioID);

    @Query("SELECT j FROM Juego j WHERE LOWER(j.titulo) LIKE LOWER(CONCAT('%',:palabra,'%')) OR LOWER(j.categoria) LIKE LOWER(CONCAT('%',:palabra,'%'))")
    List<Juego> buscarPorTituloOCategoria(@Param("palabra")String palabra);

    @Query("SELECT j FROM Juego j WHERE j.precio < :precioComparacion AND LOWER(j.titulo) LIKE CONCAT(:letra, '%')")
    List<Juego> buscarPrecioMenorAQueEmpiecepor(@Param("precioComparacion") Double precioComparacion, @Param("letra") String letra);



}
