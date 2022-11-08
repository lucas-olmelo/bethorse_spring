package bethorse.conectarMongo;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class ConectarMongo {
    
    String database = "teste1";
    String collection = "dados";

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

    public void insertValues(String nome, String email, long cpf, String pass, String fone){
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
        docs.insertOne(docBuilder);
    }

    public String Logar(String email, String pass){
        System.out.println("Logar");
        String uri = "mongodb://localhost";
        MongoClient mongo = MongoClients.create(uri);
        MongoDatabase db = mongo.getDatabase(database);
        MongoCollection<Document> docs = db.getCollection(collection);
        Document doc = docs.find(Filters.eq("email", email)).first();
        if (doc == null){
            return "Usuário não encontrado";
        } else {
            String senhaMongo = doc.getString("senha");
            if (senhaMongo.equals(pass)) {
                return doc.getString("nome");
            } else {
                return "Senha errada";
            }
        }
    }
}
