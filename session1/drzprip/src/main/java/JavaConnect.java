import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Toni
 */
public class JavaConnect {
    
    Connection con;
    
    
    
    public static Connection connectDb(){
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:1433/session1", "root", "Root");
            
            System.out.println(con);
            
            return con;
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JavaConnect.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(JavaConnect.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        
    }
}
/*<dependencies>
        <dependency>
            <groupId>org.jfree</groupId>
            <artifactId>jfreechart</artifactId>
            <version>1.5.3</version>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>9.2.0</version>
        </dependency>
        <dependency>
            <groupId>com.example</groupId> <!-- Replace with a suitable groupId -->
            <artifactId>rs2xml</artifactId> <!-- Replace with a suitable artifactId -->
            <version>1.0</version> <!-- Replace with the version of the jar -->
            <scope>system</scope>
            <systemPath>${project.basedir}/rs2xml.jar</systemPath>
        </dependency>
    </dependencies>*/