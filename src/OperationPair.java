/**
 * Used To maintain what option comes after the operation.
 * Example.
 * ("Operation" , "AND"), ("Operation", "OR"), ("Operation", "END")
 * <("STUDENTID", "<" , "500"), AND, "Column 1")>
 */
public class OperationPair {
    public QueryContainer query;
    public String nextOperation;

    public QueryContainer getQuery() {
        return query;
    }

    public void setQuery(QueryContainer query) {
        this.query = query;
    }

    public String getNextOperation() {
        return nextOperation;
    }

    public void setNextOperation(String nextOperation) {
        this.nextOperation = nextOperation;
    }

    public OperationPair(QueryContainer query, String nextOp) {
        this.query = query;
        nextOperation = nextOp;

    }
}
