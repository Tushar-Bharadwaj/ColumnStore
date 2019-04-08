import java.io.BufferedReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ColumnSerialized implements Serializable {
    private int elementCount;
    private int storageSize;
    private String dataType;
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
     * Fetches the id's of elements that match the privided condition from the column
     * @param value Value to be compared against
     * @param operation operation to be done
     * @return array of indices from the column
     */
    public Integer[] conditionalSelect(String value, String operation) {
        ArrayList<Integer> values = new ArrayList<Integer>();
        int index = 0; //Index at current element;

        /**
         * If the selected value matches the conditions add it to the values arraylist which
         * contains the final list of values that satisfiy the conditions
         */
        for(String element : data) {
            //If the selected operation is 'equals'
            try {
                if (operation.equals("=") && value.equals(element)) {
                    values.add(index);
                } else if (operation.equals("<=") && Float.parseFloat(value.trim()) >= Float.parseFloat(element)) {
                    values.add(index);
                } else if (operation.equals(">=") && Float.parseFloat(value.trim()) <= Float.parseFloat(element)) {
                    values.add(index);
                } else if (operation.equals("<") && Float.parseFloat(value.trim()) > Float.parseFloat(element)) {
                    values.add(index);
                } else if (operation.equals(">") && Float.parseFloat(value.trim()) < Float.parseFloat(element)) {
                    values.add(index);
                } else if (operation.equals("!=") && !value.equals(element)) {
                    values.add(index);
                }
            } catch (NumberFormatException e) {
                System.out.println("Not a number/"+ Float.parseFloat(value.trim())+"/");
            }

            /**
             * Fututre <= < > And other operations here, Need to check the data type at this level also.
             */

            index++;
        }

        int size = values.size();
        //If any elements are present
        if(size > 0) {
            Integer[] temp = values.toArray(new Integer[size]);
            Integer []result = new Integer[size];
            for(int i = 0; i < size; i++)
                result[i] = temp[i];
            return result;
        }
        return null;
    }

    /**
     * Gets value of element by id
     * @param id
     * @return
     */
    public String getSelectedValues(int id) {
        return data.get(id);
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
