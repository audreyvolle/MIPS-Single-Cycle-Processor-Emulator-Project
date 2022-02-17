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
        Hashtable<String, Integer> item = new Hashtable<String, Integer>();
        int i = 0;
        
        while( dis.available() > 0 ) {
            int addr = 96 + i;
            int x = 0;
            x =  dis.readInt() ;
            String binstr = Integer.toBinaryString(x);
            binstr = String.format("%32s", binstr).replace(' ', '0');
            //System.out.print(binstr);
            System.out.print(binstr.charAt(0)+" ");
            System.out.print(binstr.substring(1,6)+" ");
            System.out.print(binstr.substring(6,11)+" ");
            System.out.print(binstr.substring(11,16)+" ");
            System.out.print(binstr.substring(16,21)+" ");
            System.out.print(binstr.substring(21,26)+" ");
            System.out.print(binstr.substring(26,32)+" ");
            System.out.print(" " + addr + " "); //correct format

            item.put("asInt", x);
            //item.put("str", binstr);
            item.put("valid", ( x >> 31));
            item.put("opcode", (x >> 26)  & 0x0000003F);
            item.put("rs" , ((x >> 21) & 0x0000001F));
            item.put("rt", ((x >> 16) & 0x0000001F));
            item.put("rd", ((x >> 11) & 0x0000001F));
            item.put("shamt",  ((x >> 6) & 0x0000001F));
            item.put("func", (x & 0x0000003F));
            //item['imm'] = struct.unpack_from( '>h', data, i+2)[0] // need to change to java

            Integer opcode = item.get("opcode");
            
            //correct format
            if(item.get("valid") == 0){
                System.out.print("Invalid Instruction");
            }
            else if(opcode == 40){
                //addi
                System.out.print("Addi " + item.get("rt") + " " + item.get("rs"));
            }
            else if(opcode == 43){
                //sw
                System.out.print("sw " + item.get("rt") + " " + item.get("rs"));
            }
            else if(opcode == 35){
                //lw
                System.out.print("lw " + item.get("rt") + " " + item.get("rs"));
            }
            else if(opcode == 33){
                //bltz
                System.out.print("BLTZ " + item.get("rt") + " " + item.get("rs"));
            }
            else if(opcode == 32){
                //sub
                System.out.print("SUB " + item.get("rt") + " " + item.get("rs"));
            }
            else if(opcode == 34){
                //J
                System.out.print("J " + item.get("rt") + " " + item.get("rs"));
            }
            else{
                System.out.print(opcode);
            }

                System.out.println();
                //MEM[addr] = item ----> need to make array or something to store each item???
                i = i+4;
            }
            dis.close();


            //p2
        
        R = [0] * 32;
        int PC = 96;
        int cycle = 1;
        
            }
        }
    }
}
