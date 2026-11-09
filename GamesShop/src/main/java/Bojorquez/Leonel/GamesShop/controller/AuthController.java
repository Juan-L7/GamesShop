package Bojorquez.Leonel.GamesShop.controller;

import Bojorquez.Leonel.GamesShop.models.Usuario;
import Bojorquez.Leonel.GamesShop.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de autenticación: registro e inicio de sesión.
 */
@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("email") String email,
                                @RequestParam("password") String password,
                                Model model) {

        Usuario usuario = usuarioService.autenticarUsuario(email, password);

        if (usuario != null) {
            // Las credenciales son correctas, pero todavía no recordamos quién es:
            // en cuanto cambie de página, la aplicación lo olvida.
            model.addAttribute("mensaje", "Bienvenido, " + usuario.getNombre());
            return "login";
        } else {
            model.addAttribute("error", "Credenciales inválidas. Inténtalo de nuevo.");
            return "login";
        }
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        // Objeto vacío que el formulario llena con th:field
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
        return "redirect:/login?registrado=true";
    }
}
