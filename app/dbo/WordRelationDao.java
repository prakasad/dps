package dbo;

import Exceptions.InputDataErrException;
import dbo.db.CouchbaseDao;
import javassist.tools.rmi.ObjectNotFoundException;
import utils.PreProcessedDataKeyUtils;
import vo.WordRelationMapVo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class WordRelationDao {

    private static CouchbaseDao couchbaseDao = new CouchbaseDao();
    private static PreProcessedDataKeyUtils preProcessedDataKeyUtils = new PreProcessedDataKeyUtils();

    private static String getWordRelationKey(String text) throws NoSuchAlgorithmException {
        return preProcessedDataKeyUtils.sentenceToUnquieHashId(text) + "|dpt"  ;
    }

    private static String getWordRelationKeyForHash(String hash){
        return hash + "|dpt"  ;
    }

    public static void insertWordRelationMapVo(String text, WordRelationMapVo vo)
            throws NoSuchAlgorithmException, IOException, InputDataErrException {
        if(vo!=null) {
            couchbaseDao.updateDocument(getWordRelationKey(text), vo);
        } else throw new InputDataErrException("Null data");
    }

    public static WordRelationMapVo getWordRelationMapVo(String key) throws ObjectNotFoundException {
        return couchbaseDao.getDocument(getWordRelationKeyForHash(key), WordRelationMapVo.class);
    }
}
