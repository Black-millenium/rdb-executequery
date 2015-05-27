package biz.redsoft.security.gdsauth;

import biz.redsoft.security.cryptopro.exception.CryptoException;
import biz.redsoft.security.windows.Wincrypt;
import biz.redsoft.security.windows.advapi.Advapi;
import biz.redsoft.security.windows.crypt32.Crypt32;
import biz.redsoft.security.windows.crypt32._CERT_CONTEXT;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import org.firebirdsql.gds.impl.wire.Bytes;
import org.firebirdsql.gds.impl.wire.auth.AuthCryptoException;
import org.firebirdsql.gds.impl.wire.auth.AuthPrivateKeyContext;

/**
 * Created by Roman on 27.05.2015.
 */
public class ServerAuthCryptoPlugin extends AuthCryptoPluginImpl {
  private final String containerPin;
  private AuthPrivateKeyContext userKey;

  public ServerAuthCryptoPlugin(String containerPin) throws CryptoException {
    this.containerPin = containerPin;
  }

  @Override
  public synchronized AuthPrivateKeyContext getUserKey(String certBase64) throws AuthCryptoException {
    if (userKey == null)
      userKey = super.getUserKey(certBase64);
    return userKey;
  }

  @Override
  protected AuthPrivateKeyContext getUserKey(_CERT_CONTEXT.PCCERT_CONTEXT cert) throws AuthCryptoException {
    final PointerByReference provHandle = new PointerByReference();
    final IntByReference keySpec = new IntByReference();
    final IntByReference callerFreeProv = new IntByReference();
    if (!Crypt32.cryptAcquireCertificatePrivateKey(cert, 0, provHandle, keySpec, callerFreeProv))
      throw new AuthException("Can't get private key for user certificate", true);
    setContainerPin(provHandle.getValue());

    try {
      final Pointer keyHandle = Advapi.cryptGetUserKey(provHandle.getValue(), Wincrypt.AT_KEYEXCHANGE);
      return new AuthPrivateKeyContext(provHandle.getValue(), keyHandle);
    } catch (CryptoException e) {
      freeProviderContext(provHandle);
      throw new AuthException("Can't get private key for user certificate", e);
    }
  }

  @Override
  public Object getSessionPublicKey(Bytes publicKeyData, Bytes exchangeKeyData, AuthPrivateKeyContext userKey)
      throws AuthCryptoException {
    setContainerPin((Pointer) userKey.getProvHandle());
    return super.getSessionPublicKey(publicKeyData, exchangeKeyData, userKey);
  }

  private void setContainerPin(Pointer provHandle) {
    if (containerPin != null)
      Advapi.cryptSetProvParam(provHandle, Wincrypt.PP_KEYEXCHANGE_PIN, (containerPin + '\0').getBytes(), 0);
  }

  @Override
  public void freeProviderContext(Object provHandle) {
    //Не освобождаем после каждой процедуры обмена - делаем при завершении работы сервера.
  }

  @Override
  public void freeKeyHandle(Object keyHandle) {
    //Не освобождаем после каждой процедуры обмена - делаем при завершении работы сервера.
  }

  @Override
  protected void finalize() throws Throwable {
    if (userKey != null)
      userKey.free(this);
    super.finalize();
  }
}
