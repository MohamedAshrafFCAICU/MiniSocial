package CustomizedExceptions;

public class NotFoundException extends _BaseCustomizedException
{
   private static final int HTTP_STATUS = 404;

   public NotFoundException(String description )
   {
      super(description, HTTP_STATUS);
   }

  public NotFoundException(String description,Throwable cause )
  {
    super(description, HTTP_STATUS , cause) ;
  }
}
