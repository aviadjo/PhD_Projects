/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

//import com.sleepycat.collections.StoredMap;

import Assistants.General;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.collections.PrimaryKeyAssigner;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import java.io.File;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Aviadjo
 */
public class BerekelyDB {

    /*public static String m_tempFolderPath = FileUtils.getTempDirectoryPath();
    public static Database m_berkleyDB = GetDatabase();

    //public static StoredMap  = new StoredMap(new Database(),, null, null);
    public static Database GetDatabase() {
        String berkeleyDBTempFilePath = FileUtils.getTempDirectoryPath() + General.GetTimeStamp();
        String environmentFilePath = m_tempFolderPath + General.GetTimeStamp() + ".DB";

        Database database = null;
        try {
            Environment env = new Environment(new File(environmentFilePath), EnvironmentConfig.DEFAULT);
            DatabaseConfig dbConfig = new DatabaseConfig();
            database = env.openDatabase(null, General.GetTimeStamp(), dbConfig);
        } catch (DatabaseException ex) {
            
        }
        return database;
    }

    public static Map<String, Integer> GetMapStringInteger() {
        StoredMap a = StoredMap(m_berkleyDB, null, null, true);
        return new StoredMap(m_berkleyDB, null, null, true);
    }

    public static Map<String, int[]> GetMapStringIntArray() {

        
    }

    public static Map<String, Double> GetMapStringDouble() {

        
    }*/
}
