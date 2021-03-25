package mg.ituproject.stm.utils.databases;

import java.util.List;

import org.bson.Document;

import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoHelper {
	public MongoHelper(String host, int port, String database) {
		this.mongoclient = new MongoClient(host);
		this.mongodatabase = this.mongoclient.getDatabase(database); 
	}
	
	// Field 
	protected MongoClient mongoclient;
	protected MongoDatabase mongodatabase;
	
	public void insert(String collectionName, Document document) {
	    MongoCollection<Document> collection = this.mongodatabase.getCollection(collectionName);	    
	    collection.insertOne(document);
	}
	
	public Document find(String collectionName, Document doc) {
		MongoCollection<Document> collection = this.mongodatabase.getCollection(collectionName);
		return collection.find(doc).first();
	}
}
