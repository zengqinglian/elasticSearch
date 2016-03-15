
package eric.zeng.elasticsearch;


public class SearchException extends Exception {

    private static final long serialVersionUID = 1L;

    public SearchException() {
    }

    public SearchException(String s) {
        super(s);
    }

    public SearchException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SearchException(Throwable throwable) {
        super(throwable);
    }
}
