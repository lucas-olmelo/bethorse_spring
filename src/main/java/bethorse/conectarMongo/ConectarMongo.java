package bethorse.conectarMongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class ConectarMongo {
    
    String database = "bethorse";
    String collection = "usuarios";

    public void getValues(){
        System.out.println("get Values");
        String uri = "mongodb://localhost";
        MongoClient mongo = MongoClients.create(uri);
        MongoDatabase db = mongo.getDatabase(database);
        MongoCollection<Document> docs = db.getCollection(collection);
        for(Document doc : docs.find()){
            System.out.println("Item: " + doc);
        }
    }

    public void getValuesMensagem(){
        System.out.println("get Values");
        String uri = "mongodb://localhost";
        MongoClient mongo = MongoClients.create(uri);
        MongoDatabase db = mongo.getDatabase(database);
        MongoCollection<Document> docs = db.getCollection("mensagens");
        for(Document doc : docs.find()){
            System.out.println("Item: " + doc);
        }
    }

    public String selectValues(int code){
        String y = "";
        System.out.println("Select Values");
        String uri = "mongodb://localhost";
        MongoClient mongo = MongoClients.create(uri);
        MongoDatabase db = mongo.getDatabase(database);
        MongoCollection<Document> docs = db.getCollection(collection);
        Document doc = docs.find(Filters.eq("_id", code)).first();
        y = doc.getString("nome");
        return y;
    }

    public void insertValues(String nome, String email, String cpf, String pass, String fone, String user){
        System.out.println("Insert Values");
        String uri = "mongodb://localhost";
        MongoClient mongo = MongoClients.create(uri);
        MongoDatabase db = mongo.getDatabase(database);
        MongoCollection<Document> docs = db.getCollection(collection);
        Document docBuilder = new Document();
        docBuilder.append("cpf", cpf);
        docBuilder.append("nome", nome);
        docBuilder.append("email", email);
        docBuilder.append("senha", pass);
        docBuilder.append("telefone", fone);
        docBuilder.append("tipo_de_usuario", user);
        docs.insertOne(docBuilder);
    }

    public void insertValuesMensagem(String nome, String email, String telefone, String mensagem, String contato, String motivo){
        System.out.println("Insert Values");
        String uri = "mongodb://localhost";
        MongoClient mongo = MongoClients.create(uri);
        MongoDatabase db = mongo.getDatabase(database);
        MongoCollection<Document> docs = db.getCollection("mensagens");
        Document docBuilder = new Document();
        docBuilder.append("nome", nome);
        docBuilder.append("email", email);
        docBuilder.append("telefone", telefone);
        docBuilder.append("mensagem", mensagem);
        docBuilder.append("meio_de_contato", contato);
        docBuilder.append("motivo_de_contato", motivo);
        docs.insertOne(docBuilder);
    }

    public List<String> Logar(String email, String pass){
        System.out.println("Logar");
        String uri = "mongodb://localhost";
        MongoClient mongo = MongoClients.create(uri);
        MongoDatabase db = mongo.getDatabase(database);
        MongoCollection<Document> docs = db.getCollection(collection);
        Document doc = docs.find(Filters.eq("email", email)).first();
        List<String> usuario = new ArrayList<String>();
        usuario.add(doc.getString("nome"));
        usuario.add(doc.getString("email"));
        usuario.add(doc.getString("cpf"));
        usuario.add(doc.getString("tipo_de_usuario"));
        usuario.add(doc.getString("senha"));
        usuario.add(doc.getString("telefone"));

        String senhaMongo = doc.getString("senha");
        if (senhaMongo.equals(pass)) {
            return usuario;
        } else {
            List<String> erro = new ArrayList<String>(){{
                add("Senha errada");
            }};
            return erro;
        }
    }
}
