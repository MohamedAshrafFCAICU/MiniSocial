package CustomizedExceptions;

public class UnAuthorizedException extends _BaseCustomizedException
{
    private static final int HTTP_STATUS = 401;

    public UnAuthorizedException(String description)
    {
      super(description, HTTP_STATUS);
    }

    public UnAuthorizedException(String description, Throwable cause)
    {
      super(description , HTTP_STATUS, cause);
    }

}
