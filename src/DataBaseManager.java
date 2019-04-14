import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DataBaseManager implements Serializable{
    //An Array List Of Tables
    ArrayList<String> tableNames = new ArrayList<String>();
    public String databaseName;
    private static final long serialVersionUID = 1L;

    public DataBaseManager(String name) {
        databaseName = name;
    }

    public TableManager loadTable(String tableName) {
        return TableManager.deserializer(tableName);
    }

    public static void serializer(DataBaseManager obj, String dataBaseName) throws IOException {


        String fileName = dataBaseName;

        //Serializing The File
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(obj);

            out.close();
            file.close();

            //Adding the filename to list of filenames.
            System.out.println("Object has been serialized");
            System.out.println(obj.toString());

        } catch (IOException ex) {

            System.out.println("Error Has Occured During Serialization");
        }
    }


    /**
     * De-Serializes The Given Column instance
     * @param filename name of the file to be de-serialized
     * @return persisted object, if false "null"
     */
    public static DataBaseManager deserializer(String filename) {
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            DataBaseManager obj = (DataBaseManager) in.readObject();

            in.close();
            file.close();

            System.out.println("Data Base has been deserialized ");
            System.out.println(obj.toString());
            return obj;
        }

        catch(IOException ex)
        {
            ex.printStackTrace();
            System.out.println("IOException is caught");
            return null;
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
            return null;
        }
    }




    public static void insert(String tableName, int columnCount, int serializationSize, List<String> listdatatype, List<Integer> listsize,
                              List<String> listcolumnnames) throws IOException {

        //Creating The Table
        String DataBaseName = "CollegeManagement.ser";
        //Object has been serialized previous and2 stored. Only one Database for now.
        DataBaseManager dbManager = deserializer(DataBaseName);

        TableManager table = new TableManager(tableName);

        List<String[]> columnInfo = new ArrayList<String[]>();

        //Insert Information Of Each Column

        for (int i = 0; i < columnCount; i++) {

            String dataType = listdatatype.get(i);
            int elementSize = listsize.get(i);
            String columnName = listcolumnnames.get(i);
            table.addColumnInfo(columnName,new ColumnManager(dataType, elementSize, serializationSize, columnName));
        }

        TableManager.serializer(table, tableName+".ser");
        //Here we've info of the table, we then attached this table to a particular DB
        dbManager.tableNames.add(tableName+".ser");
        serializer(dbManager, DataBaseName);

    }



    public static void insertintotable (String loadTable, List<String> listinp) throws IOException {

        String DataBaseName = "CollegeManagement.ser";
        //Object has been serialized previous and2 stored. Only one Database for now.
        DataBaseManager dbManager = deserializer(DataBaseName);

        HashMap<String, String> insertData = new HashMap<String, String>();
        TableManager tableManager = TableManager.deserializer(loadTable + ".ser");
        int i=0 ;
        for(String keyNames : tableManager.columnManagers.keySet()) {
            System.out.println(keyNames + " :");
            String inputData = listinp.get(i);
            insertData.put(keyNames, inputData);
        i++;
        }
        tableManager.insert(insertData);
        //Storing the info of the table
        TableManager.serializer(tableManager, loadTable+".ser");
        System.out.println(dbManager);

    }



    public static void searchintable (int noOps, String loadTable, List<String> listcolumnName, List<String> listValue,
                                      List<String> listComparator,List<String> listOperation) throws IOException {
        ArrayList<OperationPair> operationPairs = new ArrayList<OperationPair>();
        for(int i = 0; i < noOps; i++) {
            //System.out.println("Enter Column :");
            String columnName = listcolumnName.get(i);
            //System.out.println("Enter Value :");
            String value = listValue.get(i);
            //System.out.println("Enter Comparator :");
            String operation = listComparator.get(i);
            //System.out.println("Next Operation");
            String nextOp = listOperation.get(i);

            QueryContainer queryPart = new QueryContainer(columnName,operation,value);
            OperationPair ops = new OperationPair(queryPart,nextOp);
            operationPairs.add(ops);

        }
        TableManager selectTable = TableManager.deserializer(loadTable + ".ser");
        selectTable.select(operationPairs);
        TableManager.serializer(selectTable, loadTable+ ".ser");

    }







    public static void main(String[] args) throws IOException {
        String DataBaseName = "CollegeManagement.ser";
        //Object has been serialized previous and2 stored. Only one Database for now.
        DataBaseManager dbManager = deserializer(DataBaseName);


        boolean exitMain = false;
        Scanner scan = new Scanner(System.in);
        while(!exitMain) {
            System.out.println("What action do you want to do?");
            System.out.println("1. Create Table 2. Show Tables 3. Load Existing Tables, 4. Exit");
            int option = Integer.parseInt(scan.next());
            int size;
            switch (option) {
                case 1:
                    scan.nextLine();
                    System.out.println("Enter the name of the table that you want to create.");
                    String tableName = scan.nextLine();

                    //Creating The Table
                    TableManager table = new TableManager(tableName);

                    System.out.println("Enter the number of columns you want to be added in the table.");
                    int columnCount = scan.nextInt();
                    List<String[]> columnInfo = new ArrayList<String[]>();

                    System.out.println("Enter The Serialization File Size");
                    //Enter Serialization Size
                    int serializationSize = scan.nextInt();
                    scan.nextLine();

                    //Insert Information Of Each Column

                    for (int i = 0; i < columnCount; i++) {
                        System.out.println("Enter Data Type, Element Size, Column Name");
                        String dataType = scan.nextLine();
                        int elementSize = scan.nextInt();
                        scan.nextLine();
                        String columnName = scan.nextLine();
                        table.addColumnInfo(columnName,new ColumnManager(dataType, elementSize, serializationSize, columnName));
                    }
                    TableManager.serializer(table, tableName+".ser");
                    //Here we've info of the table, we then attached this table to a particular DB
                    dbManager.tableNames.add(tableName+".ser");
                    serializer(dbManager, DataBaseName);
                    break;
                case 2:
                    System.out.println("The list of tables are :");
                    for(String currentTable : dbManager.tableNames) {
                        System.out.println(currentTable.substring(0, currentTable.length() - 4));
                    }
                    break;

                case 3:
                    System.out.println("Enter the name of the table to be loaded.");
                    scan.nextLine();
                    String loadTable = scan.nextLine();

                    boolean tableFound = false;
                    for(String selectedTable : dbManager.tableNames) {
                        if(loadTable.equals(selectedTable.substring(0, selectedTable.length() - 4))) {
                            tableFound = true;
                            break;
                        }
                    }
                    /**
                     * Doing functions inside of a particular table.
                     */
                    if(tableFound) {
                        boolean exit = false;
                        while(!exit) {
                            System.out.println("What action do you want to do?");
                            System.out.println("1. Insert, 2. Delete, 3. Update, 4. Select, 5. Get Values, 6. Exit");
                            int choice = scan.nextInt();
                            scan.nextLine();

                            switch (choice) {
                                case 1:
                                    System.out.println("Enter the data to be inserted.");
                                    HashMap<String, String> insertData = new HashMap<String, String>();
                                    //Loading the info of the table
                                    TableManager tableManager = TableManager.deserializer(loadTable + ".ser");
                                    for(String keyNames : tableManager.columnManagers.keySet()) {
                                        System.out.println(keyNames + " :");
                                        String inputData = scan.nextLine();
                                        insertData.put(keyNames, inputData);
                                    }
                                    tableManager.insert(insertData);
                                    //Storing the info of the table
                                    TableManager.serializer(tableManager, loadTable+".ser");
                                    System.out.println(dbManager);
                                    break;
                                case 4:
                                    System.out.println("Enter the Number Of actions you want to perform ex: (A < B)");
                                    int noOps = scan.nextInt();
                                    scan.nextLine();
                                    ArrayList<OperationPair> operationPairs = new ArrayList<OperationPair>();
                                    for(int i = 0; i < noOps; i++) {
                                        System.out.println("Enter Column :");
                                        String columnName = scan.nextLine();
                                        System.out.println("Enter Value :");
                                        String value = scan.nextLine();
                                        System.out.println("Enter Comparator :");
                                        String operation = scan.nextLine();
                                        System.out.println("Next Operation");
                                        String nextOp = scan.nextLine();
                                        QueryContainer queryPart = new QueryContainer(columnName,operation,value);
                                        OperationPair ops = new OperationPair(queryPart,nextOp);
                                        operationPairs.add(ops);
                                    }
                                    TableManager selectTable = TableManager.deserializer(loadTable + ".ser");
                                    selectTable.select(operationPairs);
                                    TableManager.serializer(selectTable, loadTable+ ".ser");
                                    break;
                                case 5:
                                    System.out.println("Enter Number of Ids to be selected");
                                    int count = scan.nextInt();
                                    scan.nextLine();
                                    int idsearch[] = new int[count];
                                    System.out.println("Enter Number Of Columns To Be Retrived");
                                    int colCount = scan.nextInt();
                                    scan.nextLine();
                                    System.out.println("Select Columns To Be Retrived");
                                    ArrayList<String> columnNames = new ArrayList<String>();
                                    for(int i = 0; i < colCount; i++)
                                        columnNames.add(scan.nextLine());
                                    System.out.println ("Enter Ids to be Serached");
                                    for(int i = 0 ; i < count ; i++) {
                                        idsearch[i] = scan.nextInt();
                                        scan.nextLine();
                                    }
                                    TableManager getTableValues = TableManager.deserializer(loadTable + ".ser");
                                    getTableValues.getSelectedValues(idsearch,columnNames);
                                    TableManager.serializer(getTableValues, loadTable+ ".ser");
                                    break;
                                case 6:
                                    exit = true;
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Table Not Found!!");
                    }
                    break;
                case 4:
                    exitMain = true;
                    break;
            }
        }
    }
}
