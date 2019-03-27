import java.io.BufferedReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ColumnSerialized implements Serializable {
    private int elementCount;
    private int storageSize;
    private ArrayList<String> data = new ArrayList<String>();

    public ColumnSerialized(int size) {
        elementCount = 0;
        storageSize = size;
    }




    /**
     * Getter For element Count
     * @return
     */
    public int getElementCount() {
        return elementCount;
    }

    /**
     * Setter for Element Count
     * @param elementCount
     */
    public void setElementCount(int elementCount) {
        this.elementCount = elementCount;
    }

    /**
     * Checks If Given
     * @return true if the storage is full, false if not full
     */
    public boolean checkIfFull() {
        if(elementCount == storageSize) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Inserts An element To Column
     * @param input A String Value
     * @return True if inserted successfully, false otherwise
     */
    public boolean insert(String input){
        if(!checkIfFull()) {
            data.add(input);

            //Incrementing Total Elements Present
            elementCount++;

//            System.out.println("Input : "+input+", Element Count :"+elementCount);

            return true;
        } else {
//            System.out.println("This Column Serialized Obj Is Full!");
            return false;
        }
    }

    /**
     * Get Index of an element within the given part of column store.
     * @param input string to be searched
     * @return index of the string within this store. If not found, returns -1.
     */
    public int[] select(String input) {
        ArrayList<Integer> index = indexOfAll(input, data);
        int size = index.size();
        //If any elements are present
        if(size > 0) {
            Integer[] temp = index.toArray(new Integer[size]);
            int []result = new int[size];
            for(int i = 0; i < size; i++)
                result[i] = temp[i];

            return result;
        }
        return null;
    }
    /**
     * Update a value at given index
     * @param id index where data is to be updated
     * @param input String
     * @return true if successful, false if invalid index has been choosen
     */
    public boolean update(int id, String input) {

        //Check if given ID exceeds the total number of elements existws
        if(elementCount < id) {
//            System.out.println("Invalid Index");

            return false;
        } else {
//            System.out.println("Update");
//            System.out.println(data.get(id));

            data.set(id, input);

//            System.out.println(data.get(id));

            return true;
        }
    }

    /**
     * Deleting the value at a given index
     * @param id index where the data's to be deleted
     * @return false if invalid index, true if successful
     */
    public boolean delete(int id) {
        if(elementCount < id) {

          //  System.out.println("Invalid Index");

            return false;
        } else {
            data.remove(id);
            //Reducing the total elements present in the column store instance
            elementCount--;

           // System.out.println("Deleted : "+id+"Element Count :"+elementCount);

            return true;
        }
    }

    @Override
    public String toString() {
        return "ColumnSerialized{" +
                "elementCount=" + elementCount +
                ", storageSize=" + storageSize +
                ", data=" + data +
                '}';
    }


    /**
     * Function to get a List Of all Items Present In a Given List
     * @return
     */
    static <T> ArrayList<Integer> indexOfAll(T obj, ArrayList<T> list) {
        final ArrayList<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (obj.equals(list.get(i))) {
                indexList.add(i);
            }
        }
        return indexList;
    }

}
