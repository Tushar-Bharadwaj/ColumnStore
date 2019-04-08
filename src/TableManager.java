import java.io.*;
import java.util.*;

public class TableManager implements Serializable{
    //We Store name of column, with the instance of column manager for that column
    public HashMap<String, ColumnManager> columnManagers = new HashMap<String, ColumnManager>();
    public int rowCount;
    public String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

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
        System.out.println(inputKeys);
        System.out.println(columnManagers.keySet());
        Set requiredKeys = columnManagers.keySet();

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

    /**
     * Selecting all column Ids of for a particular query of operations given
     * @param operations
     * @return Array of IDs
     */
    public Integer[] select(ArrayList<OperationPair> operations ) {
        //Stores current Result Set
        Set<Integer> currentResult = new HashSet<Integer>();
        int operationCount = 0;
        String operation = "";

        //For each operation pair calculate the intersection or the union of the sets nlogn
        for(OperationPair currentOp : operations) {
            ColumnManager currentColumn = columnManagers.get(currentOp.getQuery().columnName);

            //Set of All Selected Columns
            Set<Integer> tempResult = new HashSet<>(currentColumn.conditionalSelect(currentOp.getQuery().value, currentOp.getQuery().operation));
            System.out.println("Temp Result Table Manager" +tempResult);
            if(operationCount == 0) {
                currentResult.addAll(tempResult);
                operation = currentOp.nextOperation;
                operationCount++;
            } else if(operation.equals("AND")){
                currentResult.retainAll(tempResult);
                operation = currentOp.nextOperation;
            } else if(operation.equals("OR")){
                currentResult.addAll(tempResult);
                operation = currentOp.nextOperation;
            } else {
                System.out.println(currentResult);
                //Returning current result if there's no next operation
                return currentResult.toArray(new Integer[currentResult.size()]);

            }
            System.out.println(currentResult);
        }
        System.out.println("Final Result IS :");
        System.out.println(currentResult);
        return currentResult.toArray(new Integer[currentResult.size()]);
    }

    /**
     * Retrives the rows containing the values.
     * @param selectedIds
     * @param columnNames
     */
    public void getSelectedValues(int[] selectedIds, ArrayList<String> columnNames) {
        Arrays.sort(selectedIds);
        //Check if proper keys are given
        Set<String> requiredKeys = columnManagers.keySet();
        System.out.println(requiredKeys);

        //If both key sets have same value then the keys are valid
        if(!requiredKeys.containsAll(columnNames)) {
            System.out.println("Invalid Column Names Given");
            return;
        }
        HashMap<String, ArrayList<String>> columnValues = new HashMap<String, ArrayList<String>>();
        //Get List Of Column Names
        for(String columnName : columnNames) {
            ColumnManager column = columnManagers.get(columnName);

            //Store the values in temp Map
            columnValues.put(columnName, column.getColumnValues(selectedIds));
        }
        System.out.println("Selected Column Values Are :");
        System.out.println(columnValues);
    }


    public static void serializer(TableManager obj, String columnFileName) throws IOException {

        //Setting Filename as "Column Name" + "Total No. Of Files"
        String fileName = columnFileName;

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
    public static TableManager deserializer(String filename) {
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            TableManager obj = null;
            // Method for deserialization of object
            obj = (TableManager) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
            System.out.println(obj.toString());
            return obj;
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught");
            return null;
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
            return null;
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

    public static void main(String[] args) throws NumberFormatException{
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
        scan.nextLine();
        //Insert Information Of Each Column

        for (int i = 0; i < columnCount; i++) {
            System.out.println("Enter Data Type, Element Size, Column Name");
            String dataType = scan.nextLine();
            int elementSize = scan.nextInt();
            scan.nextLine();
            String columnName = scan.nextLine();
            ColumnManager column = new ColumnManager(dataType, elementSize, serializationSize, columnName);
            System.out.println(column.toString());
            table.addColumnInfo(columnName, column);
        }

        boolean exit = false;
        while(!exit) {
            System.out.println("What action do you want to do?");
            System.out.println("1. Insert, 2. Delete, 3. Update, 4. Select, 5. Get Values, 6. Exit");
            int option = scan.nextInt();
            scan.nextLine();
            int size;
            switch (option) {
                case 1:
                    System.out.println("Enter The Data To Be Inserted");
                    HashMap<String, String> insertData = new HashMap<String, String>();
                    for (String keyNames : table.columnManagers.keySet()) {
                        //Insert Data in respective Column
                        System.out.println(keyNames + " :");
                        String inputData = scan.nextLine();
                        insertData.put(keyNames, inputData);
                    }
                    table.insert(insertData);
                    System.out.println(insertData);
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

                    table.select(operationPairs);

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

                    table.getSelectedValues(idsearch,columnNames);
                    break;
                case 6:
                    exit = true;
            }
        }
    }
}
