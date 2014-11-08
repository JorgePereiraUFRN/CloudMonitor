package br.ufrn.consiste.CloudMonitor.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rhyan
 */
public class acessBD {
    Connection con;
   

    public static void main(String args[]){
        new acessBD();
    }

   
    public Connection conect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/CloudMonitor", "root", "mysql");
        } catch (SQLException ex) {
            Logger.getLogger(acessBD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(acessBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;

    }
    public void disconect(){
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(acessBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
