import java.io.Serializable;
import java.util.*;

public class TableManager {
    //We Store name of column, with the instance of column manager for that column
    private HashMap<String, ColumnManager> columnManagers = new HashMap<String, ColumnManager>();
    private int rowCount;
    private String tableName;

    /**
     * Create A Empty Table Manager Class
     * @param tableName
     */
    public TableManager(String tableName){
        this.tableName = tableName;
        this.rowCount = 0;
        System.out.println("Table Created with Name : "+tableName);
    }

    /**
     * Adds Columns To The Table
     * @param columnName name of column to be added
     * @param columnInfo all info about the column to be added.
     * @return
     */
    public boolean addColumnInfo(String columnName, ColumnManager columnInfo) {
        //Storing the Column Name with It's Manager Object
        columnManagers.put(columnName, new ColumnManager(columnInfo.dataType, columnInfo.elementSize, columnInfo.serializeSize, columnInfo.columnName));
        System.out.println("Data Inserted Successfully");
        System.out.println(toString());
        return true;
    }

    /**
     * Inserting Data Into Columns
     * @param data Hash Map of Column Name, Column Value
     */
    public void insert(HashMap<String, String> data) {

        //Check if proper keys are given
        Set inputKeys = data.keySet();
        Set requiredKeys = data.keySet();

        //If both key sets have same value then the keys are valid
        if(!inputKeys.equals(requiredKeys)) {
            System.out.println("Invalid Column Names Given");
            return;
        }

        //Iterate through each column.
        for (Map.Entry<String, String> entry : data.entrySet()) {
            //Insert Data in respective Column
            ColumnManager column = columnManagers.get(entry.getKey());
            column.insert(entry.getValue());
        }
    }

    @Override
    public String toString() {
        return "TableManager{" +
                "columnManagers=" + columnManagers +
                ", rowCount=" + rowCount +
                ", tableName='" + tableName + '\'' +
                '}';
    }

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the name of the table that you want to create.");
        String name = scan.nextLine();

        //Creating The Table
        TableManager table = new TableManager(name);

        System.out.println("Enter the number of columns you want to be added in the table.");
        int columnCount = scan.nextInt();
        List<String[]> columnInfo = new ArrayList<String[]>();

        System.out.println("Enter The Serialization File Size");
        //Enter Serialization Size
        int serializationSize = scan.nextInt();

        //Insert Information Of Each Column
        for (int i = 0; i < columnCount; i++) {
            System.out.println("Enter Data Type, Element Size, Column Name");
            String dataType = scan.next();
            String size = scan.next();
            int elementSize = Integer.parseInt(size);
            String columnName = scan.next();
            ColumnManager column = new ColumnManager(dataType, elementSize, serializationSize, columnName);
            System.out.println(column.toString());
            table.addColumnInfo(columnName, column);
        }

        boolean exit = false;
        while(!exit) {
            System.out.println("What action do you want to do?");
            System.out.println("1. Insert, 2. Delete, 3. Update, 4. Select, 5. Exit");
            String input = scan.next();
            int option = Integer.parseInt(input);
            int size;
            switch (option) {
                case 1:
                    System.out.println("Enter The Data To Be Inserted");
                    HashMap<String, String> insertData = new HashMap<String, String>();
                    for (String keyNames : table.columnManagers.keySet()) {
                        //Insert Data in respective Column
                        System.out.println(keyNames+" :");
                        String inputData = scan.next();
                        insertData.put(keyNames, inputData);
                    }
                    table.insert(insertData);
                    break;

                case 5:
                    exit = true;
            }
        }
    }
}
