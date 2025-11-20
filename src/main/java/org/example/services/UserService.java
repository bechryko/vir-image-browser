package org.example.services;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.example.db.DBRealm;
import org.example.db.SQLiteJDBC;

public class UserService {

    private final Subject currentUser;
    private final SQLiteJDBC jdbc = new SQLiteJDBC();
    private final PasswordService pwdService;

    public UserService() {
        pwdService = new DefaultPasswordService();

        DBRealm realm = new DBRealm();
        SecurityManager securityManager = new DefaultSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);

        currentUser = SecurityUtils.getSubject();
    }

    public boolean createUser(String username, String password, boolean isAdmin) {
        String hashedPassword = pwdService.encryptPassword(password);
        return jdbc.createUser(username, hashedPassword, isAdmin);
    }

    public boolean login(String username, String password) {
        if (currentUser.isAuthenticated()) {
            System.err.println("The user is already logged in!");
            return false;
        }

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // TODO: token.setRememberMe(false);

        try {
            currentUser.login(token);
            return true;
        } catch (UnknownAccountException e) {
            System.err.println("There is no user with username of " + username);
        } catch (IncorrectCredentialsException e) {
            System.err.println("Password for account " + username + " was incorrect!");
        } catch (LockedAccountException e) {
            System.err.println("The account for username " + username + " is locked.");
        } catch (AuthenticationException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void logout() {
        if (!currentUser.isAuthenticated()) {
            System.err.println("The user is already logged out!");
            return;
        }

        currentUser.logout();
    }

    public void givePermission(String username, String permission) {
        jdbc.givePermission(username, permission);
    }

    public void revokePermission(String username, String permission) {
        jdbc.revokePermission(username, permission);
    }

    public boolean hasPermission(String fileExtension) {
        return currentUser.isPermitted(fileExtension);
    }

    public boolean isAdmin() {
        return currentUser.hasRole("admin");
    }

    static void main() {
        var service = new UserService();
        //service.createUser("admin2", "admin2", true);
        service.login("admin2", "admin2");
        System.out.println(service.currentUser);
        System.out.println(service.hasPermission("png"));
    }

}
