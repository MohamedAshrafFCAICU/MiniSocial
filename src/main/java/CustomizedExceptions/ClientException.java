package CustomizedExceptions;

public class ClientException extends _BaseCustomizedException
{
    private static final int HTTP_STATUS = 400;

    public ClientException(String description)
    {
        super(description, HTTP_STATUS);
    }

    public ClientException(String description,Throwable cause )
    {
        super(description, HTTP_STATUS , cause) ;
    }
}
