import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class EX_readBinaryFile
{
    public static void main(String[] args) throws IOException, FileNotFoundException
    {
        File file = new File(args[0]);
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        while( dis.available() >0 ) {
            int x = 0;
            x =  dis.readInt() ;
            System.out.println("int: " + x);
            System.out.println((x>>26) & 0x0000003F);
            System.out.println(((x<<6)>>27) & 0x0000001F);
            System.out.println( "hex: " +Integer.toHexString(x) );
            System.out.println("--------- ");
        }
        dis.close();
    }
}
