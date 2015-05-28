package biz.redsoft.security.gdsauth;

import biz.redsoft.security.cryptopro.CertUtils;
import biz.redsoft.security.cryptopro.ContainerInfo;
import biz.redsoft.security.cryptopro.CryptoProProvider;
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
import org.firebirdsql.gds.impl.wire.auth.AuthCryptoPlugin;
import org.firebirdsql.gds.impl.wire.auth.AuthPrivateKeyContext;

import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Roman on 27.05.2015.
 */
public class AuthCryptoPluginImpl extends AuthCryptoPlugin {
  private final Pointer provider;
  private final Pointer myStore;

  public AuthCryptoPluginImpl() throws CryptoException {
    try {
      provider = CryptoProProvider.acquireContext();
      myStore = Crypt32.certOpenSystemStore(null, "MY");
    } catch (CryptoException e) {
      throw e;
    }
  }

  @Override
  public AuthPrivateKeyContext getUserKey(final String certBase64) throws AuthCryptoException {
    final _CERT_CONTEXT.PCCERT_CONTEXT certContext;
    try {
      certContext = CertUtils.getCertContext(certBase64);
    } catch (CryptoException e) {
      throw new AuthCryptoException(e);
    }
    try {
      // поищем сначала в контейнерах
      final AuthPrivateKeyContext keyContext = findCertInContainers(certBase64);
      if (keyContext != null)
        return keyContext;
      // на Linux мы должны сами поискать сертификат в хранилище, так как  риптоѕро не ищет.
      final _CERT_CONTEXT.PCCERT_CONTEXT cert = CertUtils.findCertificate(myStore, certContext);
      if (cert == null)
        throw new AuthException("Can't find certificate in personal store");
      try {
        return getUserKey(cert);
      } finally {
        Crypt32.certFreeCertificateContext(cert);
      }
    } finally {
      Crypt32.certFreeCertificateContext(certContext);
    }
  }

  private AuthPrivateKeyContext findCertInContainers(String certBase64) throws AuthCryptoException {
    try {
      final Pointer hProv = CryptoProProvider.acquireContext();
      final List<ContainerInfo> certs = CertUtils.getAvailableContainersCertificatesList(hProv);
      final byte[] certData = CertUtils.decode(certBase64);
      for (ContainerInfo cert : certs) {
        if (Arrays.equals(cert.certData, certData)) {
          final Pointer keyProv = Advapi.cryptAcquireContext(cert.containerName, null, CryptoProProvider.PROV_DEFAULT, 0);
          try {
            final Pointer hKey = Advapi.cryptGetUserKey(keyProv, Wincrypt.AT_KEYEXCHANGE);
            return new AuthPrivateKeyContext(keyProv, hKey);
          } catch (CryptoException e) {
            Advapi.cryptReleaseContext(keyProv);
            throw e;
          }
        }
      }
      return null;
    } catch (CryptoException e) {
      throw new AuthCryptoException(e);
    } catch (CertificateException e) {
      throw new AuthCryptoException(e);
    }
  }

  protected AuthPrivateKeyContext getUserKey(_CERT_CONTEXT.PCCERT_CONTEXT cert) throws AuthCryptoException {
    final PointerByReference provHandle = new PointerByReference();
    final IntByReference keySpec = new IntByReference();
    final IntByReference callerFreeProv = new IntByReference();
    if (!Crypt32.cryptAcquireCertificatePrivateKey(cert, 0, provHandle, keySpec, callerFreeProv))
      throw new AuthException("Can't get private key for user certificate", true);

    try {
      final Pointer keyHandle = Advapi.cryptGetUserKey(provHandle.getValue(), keySpec.getValue());
      return new AuthPrivateKeyContext(provHandle.getValue(), keyHandle);
    } catch (CryptoException e) {
      freeProviderContext(provHandle);
      throw new AuthException("Can't get exchange private key for user certificate", e);
    }
  }

  @Override
  public void setIV(final Object keyHandle, final Bytes iVdata) throws AuthCryptoException {
    if (!Advapi.cryptSetKeyParam((Pointer)keyHandle, Wincrypt.KP_IV, iVdata.bytes(), 0))
      throw new AuthException("Can't set initialization vector for key.", true);
  }

  @Override
  public byte[] getIV(final Object keyHandle) throws AuthCryptoException {
    try {
      return Advapi.cryptGetKeyParam((Pointer)keyHandle, Wincrypt.KP_IV);
    } catch (Exception e) {
      throw new AuthException("Can't get initialization vector for key.", true);
    }
  }


  @Override
  public void freeKeyHandle(final Object keyHandle) {
    Advapi.cryptDestroyKey((Pointer)keyHandle);
  }

  @Override
  public byte[] encrypt(final Object keyHandle, final byte[] data) throws AuthCryptoException {
    try {
      return Advapi.cryptEncrypt((Pointer)keyHandle, null, true, data);
    } catch (Exception e) {
      throw new AuthCryptoException("Can't create session key.", e);
    }
  }

  @Override
  public Object createHash(final byte[] data) throws AuthCryptoException {
    final Pointer hashHandle = Advapi.cryptCreateHash(provider, Wincrypt.CALG_GR3411);
    if (!Advapi.cryptHashData(hashHandle, data, 0))
      throw new AuthException("Error hashing data.", true);
    return hashHandle;
  }

  @Override
  public boolean destroyHash(final Object hashHandle) {
    return Advapi.cryptDestroyHash((Pointer)hashHandle);
  }

  @Override
  public byte[] hashData(final byte[] data, final int hashingCount) throws AuthCryptoException {
    try {
      return Advapi.hashData(provider, data, Wincrypt.CALG_GR3411, hashingCount);
    } catch (CryptoException e) {
      throw new AuthCryptoException(e);
    }
  }

  @Override
  public Object deriveKey(final Object hashHandle, final boolean exportable) throws AuthCryptoException {
    try {
      return Advapi.cryptDeriveKey(provider, Wincrypt.CALG_G28147, (Pointer)hashHandle, exportable ? Wincrypt.CRYPT_EXPORTABLE : 0);
    } catch (Exception e) {
      throw new AuthCryptoException("Can't create session key.", e);
    }
  }

  @Override
  public void freeProviderContext(final Object provHandle) {
    Advapi.cryptReleaseContext((Pointer)provHandle);
  }

  @Override
  public Object getSessionPublicKey(final Bytes publicKeyData, final Bytes exchangeKeyData, final AuthPrivateKeyContext userKey)
      throws AuthCryptoException {
    final Pointer exchKey = Advapi.cryptImportKey((Pointer)userKey.getProvHandle(), exchangeKeyData.bytes(), (Pointer)userKey.getKeyHandle(), 0);
    if (exchKey == null)
      throw new AuthException("Can't import public key.", true);
    try {
      final Pointer keyHandle = Advapi.cryptImportKey(provider, publicKeyData.bytes(), exchKey, 0);
      if (keyHandle == null)
        throw new AuthException("Can't import public key.", true);
      return keyHandle;
    } finally {
      freeKeyHandle(exchKey);
    }
  }

  @Override
  public byte[] decrypt(final Object keyHandle, final byte[] data) throws AuthCryptoException {
    try {
      return Advapi.cryptDecrypt((Pointer)keyHandle, null, true, 0, data);
    } catch (Exception e) {
      throw new AuthCryptoException("Can't create session key.", e);
    }
  }

  @Override
  protected void finalize() throws Throwable {
    if (provider != null)
      Advapi.cryptReleaseContext(provider);
    if (myStore != null)
      Crypt32.certCloseStore(myStore);
    super.finalize();
  }
}
