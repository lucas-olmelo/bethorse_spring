package bethorse.site_spring;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bethorse.Conectar.Conectar;
import bethorse.conectarMongo.ConectarMongo;

@Controller
public class WebController {

    @RequestMapping("/")
    public String Home(Model modelo){
        System.out.println("Home");
        return "index";
    }

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
        Conectar conecta = new Conectar();
        conecta.dataBaseInsert(nome, email, cpf, fone, pass, user);
        System.out.println("Valores inseridos");
        return "login";
    }

    // @RequestMapping("/perfil")
    // public String AcessaPerfil(Model modelo){
    //     return "perfil";
    // }

    @PostMapping("/logar")
    public String Logar(Model modelo, String email, String pass){
        Conectar conecta = new Conectar();
        List<String> atributos = conecta.Logar(email, pass);

        if (atributos.size() != 0){
            modelo.addAttribute("nome", atributos.get(0));
            modelo.addAttribute("email", atributos.get(1));
            modelo.addAttribute("cpf", atributos.get(2));
            modelo.addAttribute("senha", atributos.get(3));
            modelo.addAttribute("telefone", atributos.get(4));
            modelo.addAttribute("tipo", atributos.get(5));
            modelo.addAttribute("saldo", atributos.get(6));

            setEmail(atributos.get(1));
            setSenha(atributos.get(3));

            List<String> apostas = conecta.retornaUltimasApostas(atributos.get(2));
            for (int i = 0; i < apostas.size(); i++) {
                modelo.addAttribute("aposta"+(i+1), apostas.get(i));
            }

            List<String> cavalos = conecta.retornaMelhoresCavalos(atributos.get(2));
            for (int i = 0; i < cavalos.size(); i++) {
                modelo.addAttribute("cavalo"+(i+1), cavalos.get(i));
            }

            return "/perfil";
        }
        modelo.addAttribute("erro", "Usuário ou senha incorretos!");
        return "/login";
    }

    private String email;
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @GetMapping("/logar")
    public String teste(Model modelo){
        Logar(modelo, getEmail(), getSenha());
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

    @RequestMapping("/alterar")
    public String alterarSenha(Model modelo){
        return "alterar";
    }

    @PostMapping("/confirmed")
    public String alteracaoConfirmada(Model modelo, String senhaAtual, String novaSenha){
        Conectar conecta = new Conectar();
        String senhaBD = conecta.retornaSenha(getEmail(), senhaAtual);
        if (senhaBD.equals(senhaAtual)){
            conecta.atualizaSenha(novaSenha, getEmail());
            return "confirmed";
        }
        modelo.addAttribute("erro", "Senha incorreta");
        return "alterar";
    }
}
