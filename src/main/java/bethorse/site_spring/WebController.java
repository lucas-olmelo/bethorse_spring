package bethorse.site_spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bethorse.conectarMongo.ConectarMongo;

@Controller
public class WebController {

    @RequestMapping("/login")
    public String Login(Model modelo){
        System.out.println("Login");
        modelo.addAttribute("mensagem2", "Formulário de inscrição");
        return "login";
    }

    @RequestMapping("/servicos")
    public String Servicos(Model modelo){
        System.out.println("Serviços");
        modelo.addAttribute("mensagem", "Formulário de inscrição");
        return "servicos";
    }

    @RequestMapping("/contato")
    public String Contato(Model modelo){
        System.out.println("Contato");
        modelo.addAttribute("mensagem1", "Formulário de inscrição");
        return "contato";
    }

    @RequestMapping("/cadastro")
    public String Cadastro(Model modelo){
        System.out.println("Cadastro");
        modelo.addAttribute("mensagem1", "Formulário de inscrição");
        return "cadastro";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String RespCadastro(Model modelo, String nome, String email, long cpf, String pass, String fone){
        System.out.println("Resposta do form");
        ConectarMongo cmd = new ConectarMongo();
        cmd.insertValues(nome, email, cpf, pass, fone);
        cmd.getValues();
        System.out.println("Valores inseridos");
        return "login";
    }

    @RequestMapping("/perfil")
    public String Perfil(Model modelo, String email, String pass){
        System.out.println("Acessando perfil");
        ConectarMongo cmd = new ConectarMongo();
        String nome = cmd.Logar(email, pass);
        modelo.addAttribute("nome", "Olá, " + nome);
        return "perfil";
    }
}
