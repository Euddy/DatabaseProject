/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.IO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author James2
 */
public class Database
        
{
      private static String user, pass, url;
      private static int port;
    private static ArrayList<String> comandossql;
    
    
    public static void Initialize()
    {
        comandossql = new ArrayList<>();
    }
    
    public static DefaultTableModel RemoveParametro(DefaultTableModel tabla, int row)
    {
        String id = tabla.getValueAt(row, 0).toString();
        comandossql.add("DELETE FROM parametros WHERE idparametro='" + id + "';");
        tabla.removeRow(row);
        return tabla;
    }
    
    public static DefaultTableModel LoadParametros(DefaultTableModel tabla) throws ClassNotFoundException, SQLException
    {
        Connection conn = Connect();
        String descripcion = "";
        if (tabla.getValueAt(0, 0) != null) descripcion = tabla.getValueAt(0, 0).toString();
        tabla.removeRow(0);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM parametros WHERE descripcion LIKE '%" + descripcion + "%';");
        while (rs.next())
        {
            tabla.addRow(new Object[] { rs.getString(1), rs.getString(2) });
        }
        return tabla;
    }
    
    public static Connection Connect() throws ClassNotFoundException, SQLException
    {
        // Class.forName("oracle.jdbc.driver.OracleDriver");
          Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(
                //"jdbc:postgresql://127.0.0.1:5432/Estructura", "postgres", "dbabase3") ;
               // "jdbc:oracle:thin:@" + url + ":" + port + ":" + host, user, pass);
              //  "jdbc:postgresql://localhost:5432/Estructura", "postgres", "dbabase3") ;
        "jdbc:postgres://" + url + ":" + port + "/Estructura", user, pass);
        

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
//        String userName = "postgres";         //El usuario para conectarse a la base de datos.
//        String password = "test1234";   //La contraseña para conectarse a la base de datos.
//        String url = "jdbc:postgresql://localhost:5432/Institucion";
//        //La cadena de conexión para la base de datos.
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");//La clase de conexión
//        Connection conn = DriverManager.getConnection(url, userName, password);//El objeto de conexión
        return conn;
    }
    public static void Commit() throws ClassNotFoundException, SQLException
    {
        if (comandossql.size() > 0)
        {
            Connection con = Connect();
            Statement stmt = con.createStatement();
            for (int i = 0; i < comandossql.size(); i++)
            {
                try
                {
                    stmt.execute(comandossql.get(i));
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
            con.close();
            comandossql.clear();
        }
    }
    
    public static void Connection(String user, String pass, int port, String url)
    {
        Database.user = user;
        Database.pass = pass;
        Database.port = port;
       // Database.host = host;
        Database.url = url;
        comandossql = new ArrayList<>();
    }
    
    public static boolean TryConnection()
    {
        try
        {
            Connect();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
