/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data_Structures;

import static Assistants.General.Get_TimeStamp_String;
import java.io.File;
import org.mapdb.*;

/**
 *
 * @author Aviad
 */
public class MapDB {

    public static DB m_db_off_heap_FE = DBMaker.newMemoryDirectDB().transactionDisable().asyncWriteEnable().closeOnJvmShutdown().deleteFilesAfterClose().make();
    public static DB m_db_off_heap_CFE = DBMaker.newMemoryDirectDB().transactionDisable().asyncWriteEnable().closeOnJvmShutdown().deleteFilesAfterClose().make();
    public static DB m_db_off_heap_FS = DBMaker.newMemoryDirectDB().transactionDisable().asyncWriteEnable().closeOnJvmShutdown().deleteFilesAfterClose().make();
    public static int m_db_counter = 0;

    public static HTreeMap<String, Integer> Get_HTreeMap_String_Integer() {
        return m_db_off_heap_FE.createHashMap(++m_db_counter + "")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.INTEGER)
                .counterEnable()
                .make();
        /*return DBMaker.newCacheDirect(3);*/
    }

    public static HTreeMap<String, int[]> Get_HTreeMap_String_Array_int() {
        return m_db_off_heap_CFE.createHashMap(++m_db_counter + "")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.INT_ARRAY)
                .counterEnable()
                .make();
        /*return DBMaker.newCacheDirect(8);*/
    }

    public static HTreeMap<String, Double> Get_HTreeMap_String_Double() {
        return m_db_off_heap_FS.createHashMap(++m_db_counter + "")
                .keySerializer(Serializer.STRING)
                .counterEnable()
                .make();
        /*return DBMaker.newCacheDirect(3);*/
    }

    public static HTreeMap<String, Integer> Get_Temp_HTreeMap_String_Integer() {
        return DBMaker.newTempHashMap();
    }

    public static HTreeMap<String, int[]> Get_Temp_HTreeMap_String_Array_int() {
        return DBMaker.newTempHashMap();
    }

    public static void Get_DB() {
        String db_file_path = String.format("DatasetBuilderMapDB_%s", Get_TimeStamp_String());
        File db_file = new File(db_file_path);
        DBMaker.newFileDB(db_file);
    }

}
