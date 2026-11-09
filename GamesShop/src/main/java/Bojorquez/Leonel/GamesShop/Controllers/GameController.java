package Bojorquez.Leonel.GamesShop.Controllers;

import Bojorquez.Leonel.GamesShop.models.Juego;
import Bojorquez.Leonel.GamesShop.services.JuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {


    @Autowired
    private JuegoService juegoService;

    private static List<Juego> juegosDb = new ArrayList<>();
    private static long idCounter = 1;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    static {
        // juegosDb.add(new Juego(idCounter++, "The Legend of Zelda", "Aventura épica.", "zelda.jpg"));
    }

    @GetMapping("/fragments-demo")
    public String fragmentsDemo() { return "fragments-demo"; }

    @GetMapping({"/", "/juegos"})
    public String listarJuegos(@RequestParam(name = "buscar", required = false)String buscar, Model model){
        if(buscar !=null && !buscar.isEmpty()) {
            model.addAttribute("juegos", juegoService.listarTodos());
            model.addAttribute("busquedaActual", buscar);
        }else {
            model.addAttribute("juegos", juegoService.listarTodos());
        }
        return "juegos";
    }

    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("juego", new Juego());
        return "formulario";
    }

    @PostMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("precio") Double precio,
                               @RequestParam("categoria") String categoria,
                               @RequestParam("portada") MultipartFile portada) {



        Juego nuevoJuego = new Juego();
        nuevoJuego. setTitulo (titulo); nuevoJuego. setCategoria(categoria); nuevoJuego.setDescripcion (descripcion);
        nuevoJuego. setPrecio (precio);
        juegoService.guardarJuego(nuevoJuego, portada);
        return "redirect:/juegos";
    }

    @GetMapping("juegos/editar/{id}")
    public String editarJuego(@PathVariable Long id, Model model){
        Juego juego = juegoService.obtenerJuegoPorId(id);
        if(juego != null){
            model.addAttribute("juego", juego);
            return "formulario";
        }
        return "juegos";
    }

    @PostMapping("/juegos/editar/{id}")
    public  String actualizarJuego(@PathVariable Long id,
                                   @RequestParam("titulo") String titulo,
                                   @RequestParam("descripcion") String descripcion,
                                   @RequestParam("precio") Double precio,
                                   @RequestParam("categorias") String categoria,
                                   @RequestParam(value = "portada", required = false) MultipartFile portada){
        Juego juegoExistente = juegoService.obtenerJuegoPorId(id);
        if(juegoExistente != null){
            juegoExistente.setTitulo(titulo);
            juegoExistente.setDescripcion(descripcion);
            juegoExistente.setPrecio(precio);
            juegoExistente.setCategoria(categoria);
            juegoService.guardarJuego(juegoExistente, portada);
        }

        return "redirect:/juegos";
    }

    @PostMapping("/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long id){
        Juego juegoExistente = juegoService.obtenerJuegoPorId(id);
        if(juegoExistente != null){
            juegoService.eliminarJuego(id);
        }

        return "redirect:/juegos";
    }

    @GetMapping("/juego/{id}")
    public String detalleJuegos(@PathVariable Long id, Model model){
        Juego juego = juegoService.obtenerJuegoPorId(id);

            if(juego ==null){
                return "redirect:/juegos";
            }

            model.addAttribute("juego",juego);

        return "detalle-juego";
    }
}
