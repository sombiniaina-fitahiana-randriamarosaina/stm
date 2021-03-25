/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.ituproject.stm.utils.databases;

import java.util.List;


public enum Database {
    ORACLE(1), MYSQL(2), POSTGRESQL(3), SQLSERVER(4);
    
    private String COLUMNNAME;
            
    private Database(int number){
        switch(number){
            case 1:
                this.COLUMNNAME = "SELECT column_name FROM USER_TAB_COLUMNS WHERE table_name = '%s'";
                break;
                
            case 2:
                this.COLUMNNAME = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '%s'";
                break;
                
            case 3:
                this.COLUMNNAME = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = LOWER('%s')";
                break;
            
            case 4:
                this.COLUMNNAME = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '%s'";
                break;
        }
    }
    
    public String columnNameRequest(String table){
        return String.format(this.COLUMNNAME, table);
    }
}
