package biz.redsoft.security.gdsauth;

import biz.redsoft.security.windows.ErrorMessages;
import biz.redsoft.security.windows.advapi.Advapi;
import org.firebirdsql.gds.impl.wire.auth.AuthCryptoException;

/**
 * Created by Roman on 27.05.2015.
 */
public class AuthException extends AuthCryptoException {
  public AuthException(final String message) {
    super(message);
  }

  public AuthException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public AuthException(final Throwable cause) {
    super(cause);
  }


  public AuthException(final String message, final boolean addCryptoErrDscr) {
    super(message + (addCryptoErrDscr ? " " + ErrorMessages.getMessage(Advapi.getLastError()) : ""));
  }
}