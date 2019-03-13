package dbo;

import Exceptions.InputDataErrException;
import dbo.db.CouchbaseDao;
import javassist.tools.rmi.ObjectNotFoundException;
import utils.PreProcessedDataKeyUtils;
import vo.WordSentenceIdReverseMapVo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class WordSentenceIdDao {

    private static CouchbaseDao couchbaseDao = new CouchbaseDao();
    private static PreProcessedDataKeyUtils preProcessedDataKeyUtils = new PreProcessedDataKeyUtils();

    private static String getWordRelationKey(String text) throws NoSuchAlgorithmException {
        return text  ;
    }

    public static void insertWordSentenceIdReverseMapVo( WordSentenceIdReverseMapVo vo)
            throws NoSuchAlgorithmException, IOException, InputDataErrException {
        if(vo!=null) {
            couchbaseDao.updateDocument(getWordRelationKey(vo.tokenizedWord), vo);
        } else throw new InputDataErrException("Null data");
    }

    public static WordSentenceIdReverseMapVo getWordSentenceIdReverseMapVo(String key) throws ObjectNotFoundException {
        return couchbaseDao.getDocument(key, WordSentenceIdReverseMapVo.class);
    }
}
