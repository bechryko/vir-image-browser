package org.example.db;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

public class DBRealm extends JdbcRealm {

    public DBRealm() {
        super();

        PasswordService pwdService = new DefaultPasswordService();
        PasswordMatcher pwdMatcher = new PasswordMatcher();
        pwdMatcher.setPasswordService(pwdService);
        setCredentialsMatcher(pwdMatcher);

        SQLiteDataSource dataSource = new SQLiteDataSource(getConfig());
        dataSource.setUrl(SQLUtils.CONNECTION_URL);
        setDataSource(dataSource);

        setPermissionsLookupEnabled(true);
    }

    private SQLiteConfig getConfig() {
        SQLiteConfig config = new SQLiteConfig();

        config.enforceForeignKeys(true);

        return config;
    }

}