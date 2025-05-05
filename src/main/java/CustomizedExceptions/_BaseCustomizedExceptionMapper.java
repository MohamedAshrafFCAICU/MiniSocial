package CustomizedExceptions;

import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Provider
public class _BaseCustomizedExceptionMapper implements ExceptionMapper<_BaseCustomizedException>
{
    private static final Logger logger = Logger.getLogger(_BaseCustomizedExceptionMapper.class.getName());

    @Override
    public Response toResponse(_BaseCustomizedException exception)
    {
        Map<String , Object> response = new HashMap<>();
        response.put("message", exception.getMessage());
        response.put("status" , exception.getStatusCode());

        if(exception instanceof InternalServerException)
        {
            Throwable cause = exception.getCause();
            if(cause != null)
            {
                logger.log(Logger.Level.ERROR , "Internal Server Error details", cause);

                StringBuilder stackTrace = new StringBuilder();
                for (StackTraceElement ste : cause.getStackTrace())
                {
                    stackTrace.append("\n\t").append(ste.toString()) ;
                }
                response.put("stackTrace", stackTrace.toString());

            }

            response.put("message", "An Unexpected error occurred please try again later or contact this Email: mohamedashraf35000@gmail.com");
        }

        return Response.status(exception.getStatusCode()).entity(response).type(MediaType.APPLICATION_JSON).build();
    }

}
