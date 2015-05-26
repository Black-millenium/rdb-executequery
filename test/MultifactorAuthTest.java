import biz.redsoft.ncore.db.jdbc.ConnectionProps;
import biz.redsoft.ncore.db.jdbc.IsolationLevel;
import biz.redsoft.ncore.db.jdbc.JdbcConnectionFactory;
import biz.redsoft.security.dss.XmlSign;
import biz.redsoft.security.gdsauth.AuthCryptoPluginImpl;
import biz.redsoft.util.Base64;
import junit.framework.TestCase;
import org.executequery.datasource.ConnectionPoolImpl;
import org.firebirdsql.gds.impl.wire.auth.AuthCryptoPlugin;
import org.junit.Test;

import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MultifactorAuthTest extends TestCase {
  @Test
  public void testLoginPassword() throws Exception {
    int result = 0;
    System.setProperty("ncore.db.pool.enabled", "false");
    AuthCryptoPlugin.register(new AuthCryptoPluginImpl());
    ConnectionProps props = new ConnectionProps("jdbc:firebirdsql:localhost/3050:test?lc_ctype=WIN1251",
        "userpass",
        "123",
        null,
        true,
        "Test process",
        null,
        null);
    JdbcConnectionFactory factory = JdbcConnectionFactory.getConnectionFactory(props);
    try {
      Connection conn = factory.newConnection(IsolationLevel.READ_COMMITTED);
//      result = JdbcUpdate.update(conn, "insert into(...)");
    } finally {
      factory.close();
    }
    assertEquals(result, 0);
  }

  @Test
  public void testLoginPasswordCert() throws Exception {
    int result = 0;

    AuthCryptoPlugin.register(new AuthCryptoPluginImpl());
    ConnectionProps props = new ConnectionProps("jdbc:firebirdsql:localhost/3050:test?lc_ctype=WIN1251",
        "userpasscert",
        "123",
        null,
        true,
        "Test process",
        null,
        getCertbase64());
    JdbcConnectionFactory factory = JdbcConnectionFactory.getConnectionFactory(props);
    try {
      Connection conn = factory.newConnection(IsolationLevel.READ_COMMITTED);
//      result = JdbcUpdate.update(conn, "insert into(...)");
    } finally {
      factory.close();
    }
    assertEquals(result, 0);
  }

  @Test
  public void testCertOnly() throws Exception {
    int result = 0;

    AuthCryptoPlugin.register(new AuthCryptoPluginImpl());
    ConnectionProps props = new ConnectionProps("jdbc:firebirdsql:localhost:test?lc_ctype=WIN1251",
        null,
        null,
        null,
        true,
        "Test process",
        null,
        getCertbase64());
    JdbcConnectionFactory factory = JdbcConnectionFactory.getConnectionFactory(props);
    try {
      Connection conn = factory.newConnection(IsolationLevel.READ_COMMITTED);
//    result = JdbcUpdate.update(conn, "insert into(...)");
    } finally {
      factory.close();
    }
    assertEquals(result, 0);
  }

  public Object getCertAlias() throws Exception {
    XmlSign.init();
    List<String> certsList = new ArrayList<String>();
    final Enumeration<String> en = XmlSign.certificates();
    while (en.hasMoreElements()) {
      final String cert = en.nextElement();
      certsList.add(cert);
    }
    return certsList.get(0);
  }

  private String getCertbase64() throws Exception {
      final Object cert = getCertAlias();
      if (cert != null) {
        final Certificate certificate = XmlSign.getCertificate(cert.toString());
        return certificate == null ? null : Base64.encodeBytes(certificate.getEncoded());
      }
    return null;
  }

}