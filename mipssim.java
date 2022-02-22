import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map;


public class mipssim {
    public static void main(String[] args) throws IOException, FileNotFoundException
    {
        File file = new File(args[0]);
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        
        
        Hashtable<String, Integer> item = new Hashtable<String, Integer>();
        Map<Integer, Hashtable<String, Integer>> MEM = new HashMap<Integer, Hashtable<String, Integer>>();
        int i = 0;
        
        while( dis.available() > 0 ) {
            int addr = 96 + i;
            int x = 0;
            x =  dis.readInt() ;
            String binstr = Integer.toBinaryString(x);
            binstr = String.format("%32s", binstr).replace(' ', '0');
            if(binstr.substring(0,15).equals("111111111111111") || binstr.substring(0,15).equals("000000000000000"))
            {
                System.out.print(binstr);
                System.out.printf("%11.5s", addr );
                System.out.println();
                i=i+4;
            } else {
            System.out.print(binstr.charAt(0)+" ");
            System.out.print(binstr.substring(1,6)+" ");
            System.out.print(binstr.substring(6,11)+" ");
            System.out.print(binstr.substring(11,16)+" ");
            System.out.print(binstr.substring(16,21)+" ");
            System.out.print(binstr.substring(21,26)+" ");
            System.out.print(binstr.substring(26,32)+"  ");
            //System.out.print(binstr);
             System.out.printf("%-5.5s %-2s", addr, ""); //correct format

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
            //item.put("imm", );

            Integer opcode = item.get("opcode");
            
            //correct format
            if(binstr.charAt(0)=='0'){
                // In IN done
                System.out.print("Invalid Instruction");
            }
            else if(binstr.substring(1,6).equals("01000")){
                //addi need immediate
                System.out.printf("%-7s R%-2s R%-2s #%-2s", "ADDI", item.get("rt") + ",", item.get("rs") + ",", item.get("func") );
            }
            else if(binstr.substring(1,6).equals("01011")){
                //sw
                System.out.printf("%-7s R%-2s %s R%s", "SW", item.get("rt") + ",", item.get("shamt") + "(", item.get("rs") + ")" );
            }
            else if(binstr.substring(1,6).equals("00011")){
                //lw
                System.out.printf("%-7s R%-2s %s R%s", "SW", item.get("rt") + ",", item.get("shamt") + "(", item.get("rs") + ")" );
            }
            
            else if(binstr.substring(26,32).equals("100010")){
                //sub
                System.out.printf("%-7s R%-2s R%-2s R%-2s", "SUB", item.get("rt") + ",", item.get("rs") + ",", item.get("rt") );
            }
            else if(binstr.substring(1,6).equals("00010")){
                //J
                //System.out.print("J " + item.get("rt") + " " + item.get("rs"));
                System.out.printf("%-7s #%-2s", "J", item.get("shamt"));
            }
            else if(opcode == 60){
                //MUL
                System.out.printf("%-7s R%-2s R%-2s R%-2s", "MUL", item.get("rt") + ",", item.get("rs") + ",", item.get("rt") );
            }
            else if(binstr.substring(0, 32).equals("10000000000000000000000000000000")){
                //NOP done
                System.out.print("NOP	");
            }
            else if(binstr.substring(26,32).equals("001010"))
            {
                //MOVZ
                System.out.printf("%-7s R%-2s R%-2s R%-2s", "MOVZ", item.get("rt") + ",", item.get("rs") + ",", item.get("rt") );
            }
            else if(binstr.substring(26,32).equals("000010"))
            {
                //SRL
                System.out.printf("%-7s R%-2s R%-2s #%-2s", "SRL", item.get("rt") + ",", item.get("rs") + ",", item.get("rt") );
            }
            
            else if(binstr.substring(26,32).equals("001000"))
            {
                //JR done
                System.out.printf("%-7s R%-2s", "JR", item.get("rs"));
            }
            else if(binstr.substring(26,32).equals("000000"))
            {
                //SLL
                System.out.printf("%-7s R%-2s R%-2s #%-2s", "SLL", item.get("rt") + ",", item.get("rs") + ",", item.get("rt") );
            }
            else if(binstr.substring(26,32).equals("100000"))
            {
                //ADD
                System.out.printf("%-7s R%-2s R%-2s R%-2s", "ADD", item.get("rt") + ",", item.get("rs") + ",", item.get("rt") );
            }
            else if(binstr.substring(26,32).equals("001101"))
            {
                //BREAK done
                System.out.print("BREAK");
            }
            else if(binstr.substring(16,21).equals("00000")){
                //bltz NNEEDS FORMATTING
                System.out.print("BLTZ " + item.get("rt") + " " + item.get("rs"));
            }
            
            else{
                System.out.print(opcode);
            }

                System.out.println();
                MEM.put(addr, item); //----> need to make array or something to store each item???
                i = i+4;
                }
            }

            dis.close();

/*
            //p2
            int[] R = new int[32];
            Arrays.fill(R, 0);
            
            int PC = 96;
            int cycle = 1;

while (true){

     Hashtable<String, Integer> I = MEM.get(PC);
    while (item.get("valid") == 0){
        PC +=4;
        I = MEM.get(PC);
        int opcode = I.get("opcode");
        if (opcode == 40){ //ADDI
             R[I.get("rt")] = R[ I.get("rs")]  + I.get("imm");
        }
        else if (opcode == 43){ //SW
            //MEM[  R[ I.get("rs")] +  I.get("imm") ][I.get("asInt")]= R[ I.get("rt")  ]   ; ----> i don't even know what this is supposed to do
             //print('SW\tR{0}, {2}(R{1}) '.format(item['rt'], item['rs'], item['imm']) );
        }
    }
    if (cycle > 1){
     break;
    }
}*/
    } 
}
