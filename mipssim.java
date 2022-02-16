import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

class Mipssim
{
    public static void main(String[] args) throws IOException, FileNotFoundException
    {
        File file = new File(args[0]);
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        
        //declare hash
        //array of hashes
        Hashtable<String, String> item = new Hashtable<String, String>();
        
        
        while( dis.available() > 0 ) {
            int x = 0;
            x =  dis.readInt() ;
     
            //do not know how to format for loop
            for(int i = 0; i < ... , i + 4){
                int addr = 96 + i;
                item = //hash
                    
                 binstr = Integer.toHexString(x);
                 System.out.println(binstr);
                item.put("asInt", Integer.toString(x));
                //item['asUint'] = ;
                item.put("str", binstr);
                item.put("valid", Integer.toString( x >> 31));
                item.put['opcode'] = x >> 26;
                item.put['rs'] = (x >> 21) & 0x0000001F);
                item.putj'rt'] = (x >> 16) & 0x0000001F);
                item.put['rd'] = (x >> 11) & 0x0000001F);
                item.put['shamt'] = (x >> 6) 0x0000001F);
                item.put("func" = x & 0x0000003F;
                //item['imm'] = struct.unpack_from( '>h', data, i+2)[0] // need to change to java
                opcode = item['opcode'];
                //need to correct formating from python to java
              if (item['valid'] == 0){
                    System.out.print('Invalid Instruction') 
                  }
                else if(opcode == 40){
                    System.out.print('ADDI\tR{0}, R{1}, #{2} '.format(item['rt'], item['rs'], item['imm']) )
                   }
              else if(opcode == 43){
                     System.out.print('SW\tR{0}, {2}(R{1}) '.format(item['rt'], item['rs'], item['imm']) )
                  }
             else if(opcode == 35){
                     System.out.print('LW\tR{0}, {2}(R{1}) '.format(item['rt'], item['rs'], item['imm']) )
                  }
             else{
                     System.out.print( opcode )
                 }
             MEM[addr] = item
                    
                
            }
            //end of first part
            
        }
        dis.close();
        
        
        R = [0] * 32;
        int PC = 96;
        int cycle = 1;
        
        while(true){
            
            
            
            
            
            
            
            if (cycle > 1){
                break;
            }
        }
    }
}
