import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class k {
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public k() throws FileNotFoundException {
        conn = JavaConnect.connectDb();
        File file = new File("E:\\sastav_atmosfere_primjer.csv");
        Scanner sc = new Scanner(file);
//        while (sc.hasNextLine()){
//            System.out.println(sc.nextLine());
//        }
        sc.nextLine();
        String[] polje = sc.nextLine().split(";");
        System.out.println(polje[2]);
        try{
            String sql;
            sql = "INSERT INTO atmosfera(PlanetId, ElementId, SpojId, Udio) VALUES (?, ?, ?, ?)";

            pst = conn.prepareStatement(sql);

            pst.setString(1, "2");
            pst.setString(2, "4");
            pst.setString(3, "2");
            pst.setString(4, polje[3]);

            pst.executeUpdate();

        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        new k();
    }
}
