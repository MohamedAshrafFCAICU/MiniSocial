package CustomizedExceptions;

public class _BaseCustomizedException extends RuntimeException
{
    private final String description;
    private final int statusCode;

    public _BaseCustomizedException(String description, int statusCode)
    {
        super(description);
        this.description = description;
        this.statusCode = statusCode;
    }

    public _BaseCustomizedException(String description , int statusCode, Throwable cause)
    {
        super(description, cause);
        this.description = description;
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
