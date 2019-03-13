package dbo;

import Exceptions.InputDataErrException;
import dbo.db.CouchbaseDao;
import javassist.tools.rmi.ObjectNotFoundException;
import utils.PreProcessedDataKeyUtils;
import vo.SentenceVo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class SentenceDao {
    private static CouchbaseDao couchbaseDao = new CouchbaseDao();
    private static PreProcessedDataKeyUtils preProcessedDataKeyUtils = new PreProcessedDataKeyUtils();

    private static String getWordRelationKey(String text) throws NoSuchAlgorithmException {
        return preProcessedDataKeyUtils.sentenceToUnquieHashId(text)  ;
    }

    public static void insertSentenceVo(SentenceVo vo)
            throws NoSuchAlgorithmException, IOException, InputDataErrException {
        if(vo!=null) {
            couchbaseDao.updateDocument(getWordRelationKey(vo.sentence), vo);
        } else throw new InputDataErrException("Null data");
    }

    public static SentenceVo getSentenceVo(String key) throws ObjectNotFoundException {
        return couchbaseDao.getDocument(key, SentenceVo.class);
    }
}
