import java.util.HashMap;
import java.util.Map;

public class DatabaseTable<K> {
    private Map<K, Map<String, String>> table;
    private int readCount, writeCount;

    public DatabaseTable() {
        table = new HashMap<>();
        readCount = 0;
        writeCount = 0;
    }

    /**
     * Returns data for the row with given key and field name.
     * Return null if there are no rows with the provided key or if the row
     * does not have the provided field name.
     * @param key - unique identifier for database row
     * @param field - name of field to return
     * @return - data associated with key and field (e.g., given key=124
     * and field="sport" return "Hockey" from the table above.
     */
    private String getData(K key, String field) {
        if(!table.containsKey(key)||!table.get(key).containsKey(field)) return null;
        return table.get(key).get(field);
    }

    /**
     * Update table row with given key and fields with values in the rowData parameter.
     * For example, a rowData string might look like "name:Alice, year:24, hometown:Hanover"
     * where name, year and hometown are field names whose values follow the colon.  Multiple
     * <field name>:<field value> pairs may be separated by commas.  You may assume this parameter
     * is properly formatted.
     * If a row already exists in the DatabaseTable with the given key, overwrite its field
     * values with the ones passed in as parameters.  Add new field names/values to the row
     * if necessary.  Create a new row if the table does not have a row with the given key.
     * @param key - unique identifier for a table row
     * @param rowData - string formatted as one or more comma separated <field name>:<field value> pairs
     */
    private void updateData(K key, String rowData) {
        String[] pairs = rowData.split(", ");
        if(!table.containsKey(key)) table.put(key, new HashMap<>());
        Map<String, String> row = table.get(key);
        for(String pair: pairs) {
            String[] apair = pair.split(":");
            row.put(apair[0], apair[1]);
        }
    }

    private synchronized void takeReadLock() throws InterruptedException {
        readCount++;
        while(writeCount>0) {
            wait();
        }
    }
    private synchronized void releaseReadLock() {
        readCount--;
        notifyAll();
    }
    private synchronized void takeWriteLock() throws InterruptedException {
        writeCount++;
        while(readCount>0||writeCount>1) {
            wait();
        }
    }
    private synchronized void releaseWriteLock() {
        writeCount--;
        notifyAll();
    }

    public String read(K key, String field) throws InterruptedException {
        takeReadLock();
        String result = getData(key, field);
        releaseReadLock();
        return result;
    }

    public void write(K key, String rowData) throws InterruptedException {
        takeWriteLock();
        updateData(key, rowData);
        releaseWriteLock();
    }

    public static void main(String[] args) throws InterruptedException {
        DatabaseTable<Integer> table = new DatabaseTable<>();
        table.write(1, "name:sam, sport:hockey, hometown:boston");
        table.write(2, "name:jane, sport:soccer");
        System.out.println(table.read(1, "sport"));
    }
}
