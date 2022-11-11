package bethorse.site_spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bethorse.Conectar.Conectar;
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
    public String RespCadastro(Model modelo, String nome, String email, String cpf, String pass, String fone, String user){
        System.out.println("Resposta do form");
        ConectarMongo cmd = new ConectarMongo();
        cmd.insertValues(nome, email, cpf, pass, fone, user);
        cmd.getValues();
        // Conectar conecta = new Conectar();
        // conecta.dataBaseInsert(nome, email, cpf, fone, pass, user);
        System.out.println("Valores inseridos");
        return "login";
    }

    @RequestMapping("/perfil")
    public String Perfil(Model modelo, String email, String pass){
        System.out.println("Acessando perfil");
        ConectarMongo cmd = new ConectarMongo();
        modelo.addAttribute("nome", cmd.Logar(email, pass).get(0));
        modelo.addAttribute("email", cmd.Logar(email, pass).get(1));
        modelo.addAttribute("cpf", cmd.Logar(email, pass).get(2));
        modelo.addAttribute("usuario", cmd.Logar(email, pass).get(3));
        modelo.addAttribute("senha", cmd.Logar(email, pass).get(4));
        modelo.addAttribute("telefone", cmd.Logar(email, pass).get(5));
        return "perfil";
    }

    @RequestMapping(value = "/contato", method = RequestMethod.POST)
    public String EnviaFormulario(String nome, String email, String telefone, String mensagem, String contato, String motivo){
        System.out.println("Enviando os dados para o mongoDb");
        ConectarMongo cmd = new ConectarMongo();
        cmd.insertValuesMensagem(nome, email, telefone, mensagem, contato, motivo);
        cmd.getValuesMensagem();
        System.out.println("Valores inseridos");
        return "contato";
    }
}
