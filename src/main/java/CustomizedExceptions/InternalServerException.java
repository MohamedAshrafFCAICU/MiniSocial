package CustomizedExceptions;

import org.jboss.logging.Logger;

public class InternalServerException extends _BaseCustomizedException
{
    private static final int HTTP_STATUS = 500;
    private static final Logger logger = Logger.getLogger(InternalServerException.class.getName());

    public InternalServerException(String description)
    {
        super(description , HTTP_STATUS);
        logger.log(Logger.Level.ERROR, "Internal Server Exception: " + description);
    }
    public InternalServerException(String description , Throwable cause)
    {
        super(description , HTTP_STATUS , cause);
        logger.log(Logger.Level.ERROR, "Internal Server Exception: " + description , cause);
    }

    public InternalServerException(Throwable cause)
    {
        super("An Unexpected Error occurred" , HTTP_STATUS , cause);
        logger.log(Logger.Level.ERROR ,"An Unexpected Error occurred", cause);
    }

}
