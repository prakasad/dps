package dbo.db;

import com.couchbase.client.java.document.JsonDocument;
import helper.Couchbase;
import helper.CustomMapper;
import javassist.tools.rmi.ObjectNotFoundException;
import play.Logger;
import vo.JsonEntity;

import java.io.IOException;

public class CouchbaseDao {

    public <T extends JsonEntity> T getDocument(String objID, Class<T> cls) throws ObjectNotFoundException {
        try {
            JsonDocument mycas = Couchbase.get(objID);
            if(mycas != null){
                T object = CustomMapper.apiMapper.readValue(mycas.content().toString(), cls);
                //object.setCas(mycas.cas());
                return object;
            } else {
                throw new ObjectNotFoundException("Object "+ objID + " not found.");
            }
        } catch (IOException e) {
            Logger.error("Error in parsing JSON while getting object from couchbase", e);;
            throw new ObjectNotFoundException("Could not parse JSON for key "+objID);
        }
    }

    public <T extends JsonEntity> void updateDocument(String id, Object vo) throws IOException {
        try {
            JsonEntity bvo = (JsonEntity) vo;
            Couchbase.update(id, CustomMapper.apiMapper.writeValueAsString(vo));
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }


}
