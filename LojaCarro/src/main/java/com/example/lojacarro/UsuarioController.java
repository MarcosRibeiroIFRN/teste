package com.example.lojacarro;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class UsuarioController {

    @Autowired
   private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/Usuarios")
    public List<Usuario> GetAllById(){
        return usuarioRepository.findAll();
    }

    @PostMapping("/SalvarUsuario")
    public Usuario SaveUser(@RequestBody Usuario usuario){
        System.out.println(usuario);
        return usuarioRepository.save(usuario);
    }

     @GetMapping("/Usuario")
    public ResponseEntity <Usuario> GetUserById(@RequestBody int id) {
         Optional<Usuario> usuario = usuarioRepository.findById(id);
         if (usuario.isPresent()) {
             return ResponseEntity.ok(usuario.get());
         } else {
             return ResponseEntity.notFound().build();
         }
     }

    @DeleteMapping("/DeletarUsuario")
    public void DeleteUserById(@RequestBody int id){
        Optional<Usuario>usuario = usuarioRepository.findById(id);
        if(usuario.isPresent()){
            usuarioRepository.delete(usuario.get());
        }else{
            System.out.println("Usuário não encontrado.");
        }
    }

    @GetMapping("/login")
    public Boolean Login(@RequestBody String login){
        System.out.println("login attemp for user: " + login);
        Usuario user =usuarioRepository.findBylogin(login.split(",")[0]);
        if(user != null && user.getSenha().equals(login.split(",")[1])){
            System.out.println("Usuario encontrado");
            return true;
        }
        else {
            System.out.println("Usuario Não encontrado");
            return false;
        }
    }
    @PostMapping("/enviarUsuario")
    public String enviarMensagem (@RequestBody String mensagem) {
        System.out.println("MENSAGEM RMQ = "+mensagem);
        RabbitSend.escreverMensagem(mensagem);
        return "Mensagem enviada com sucesso!";
    }
 //
 //   @PostMapping("/enviarResposta")
    public String enviarMensagem12 (String mensagem) {
        System.out.println("MENSAGEM RMQ = "+mensagem);
        RabbitSend.escreverMensagem(mensagem);
        return "Mensagem enviada com sucesso!";
    }

    @GetMapping()
    public void autenticador() throws IOException {
        String resposta = RabbitSend.lerMensagem();
        System.out.println("login attemp for user: " + resposta);
        Usuario user =usuarioRepository.findBylogin(resposta.split(",")[0]);
        if(user != null && user.getSenha().equals(resposta.split(",")[1])){
            System.out.println("Usuario encontrado");
            enviarMensagem12("true");
        }
        else {
            System.out.println("Usuario Não encontrado");
            enviarMensagem12 ("false");
        }

    }

    @GetMapping("/lerUsuario")
    public void lerMensagem() throws IOException {
        String resposta = RabbitSend.lerMensagem();

        if (resposta == null || resposta.trim().isEmpty()) {
            System.out.println("Mensagem vazia ou nula recebida da fila RabbitMQ.");
            //return;
        }
    }
    @GetMapping("/Salvar")
    public void lerMensagem2() throws IOException{
        ObjectMapper om = new ObjectMapper();
        Usuario u = om.readValue(RabbitSend.resposta, Usuario.class);
        usuarioRepository.save(u);
        System.out.println("Resposta = " + RabbitSend.resposta);
    }

}
