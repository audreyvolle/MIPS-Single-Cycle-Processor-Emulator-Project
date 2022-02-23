import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.Map;

public class mipssim {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        File file = new File("test3.bin");
        DataInputStream dis = new DataInputStream(new FileInputStream(file));

        Hashtable<String, Integer> item = new Hashtable<String, Integer>();
        Map<Integer, Hashtable<String, Integer>> MEM = new HashMap<Integer, Hashtable<String, Integer>>();
        int i = 0;

        while (dis.available() > 0) {
            int addr = 96 + i;
            int x = 0;
            x = dis.readInt();
            String binstr = Integer.toBinaryString(x);
            binstr = String.format("%32s", binstr).replace(' ', '0');
            if (binstr.substring(0, 15).equals("111111111111111")
                    || binstr.substring(0, 15).equals("000000000000000")) {
                System.out.print(binstr);
                System.out.printf("%11.5s %-2s  %-7s", addr, "", x);
                System.out.println();
                i = i + 4;
            } else {
                System.out.print(binstr.charAt(0) + " ");
                System.out.print(binstr.substring(1, 6) + " ");
                System.out.print(binstr.substring(6, 11) + " ");
                System.out.print(binstr.substring(11, 16) + " ");
                System.out.print(binstr.substring(16, 21) + " ");
                System.out.print(binstr.substring(21, 26) + " ");
                System.out.print(binstr.substring(26, 32) + "  ");
                // System.out.print(binstr);
                System.out.printf("%-5.5s %-2s", addr, ""); // correct format

                item.put("asInt", x);
                item.put("valid", (x >> 31));
                item.put("opcode", (x >> 26) & 0x0000003F);
                item.put("rs", ((x >> 21) & 0x0000001F));
                item.put("rt", ((x >> 16) & 0x0000001F));
                item.put("rd", ((x >> 11) & 0x0000001F));
                item.put("shamt", ((x >> 6) & 0x0000001F));
                item.put("func", (x & 0x0000003F));
                item.put("imm", x & 0xFFFF);
                item.put("offset", item.get("imm") << 2);
                item.put("target", (x << 2) & 0xFF);

                Integer opcode = item.get("opcode");

                if (binstr.charAt(0) == '0') {
                    // Invalid Instruction
                    System.out.print("Invalid Instruction");
                } else if (binstr.substring(1, 6).equals("01000")) {
                    // addi
                    System.out.printf("%-7s R%-2s R%-2s #%-2s", "ADDI", item.get("rt") + ",", item.get("rs") + ",",
                            item.get("imm"));
                } else if (binstr.substring(1, 6).equals("01011")) {
                    // sw
                    System.out.printf("%-7s R%-2s %sR%s", "SW", item.get("rt") + ",", item.get("imm") + "(",
                            item.get("rs") + ")");
                } else if (binstr.substring(1, 6).equals("00011")) {
                    // lw
                    System.out.printf("%-7s R%-2s %sR%s", "LW", item.get("rt") + ",", item.get("imm") + "(",
                            item.get("rs") + ")");
                }
                else if (binstr.substring(26, 32).equals("100010")) {
                    // sub
                    System.out.printf("%-7s R%-2s R%-2s R%-2s", "SUB", item.get("rd") + ",", item.get("rs") + ",",
                            item.get("rt"));
                } else if (binstr.substring(1, 6).equals("00010")) {
                    // J
                    System.out.printf("%-7s #%-2s", "J", item.get("target"));
                } else if (opcode == 60) {
                    // MUL
                    System.out.printf("%-7s R%-2s R%-2s R%-2s", "MUL", item.get("rd") + ",", item.get("rs") + ",",
                            item.get("rt"));
                } else if (binstr.substring(0, 32).equals("10000000000000000000000000000000")) {
                    // NOP done
                    System.out.print("NOP");
                } else if (binstr.substring(26, 32).equals("001010")) {
                    // MOVZ
                    System.out.printf("%-7s R%-2s R%-2s R%-2s", "MOVZ", item.get("rd") + ",", item.get("rs") + ",",
                            item.get("rt"));
                } else if (binstr.substring(26, 32).equals("001101")) {
                    // BREAK done
                    System.out.print("BREAK");
                } else if (binstr.substring(26, 32).equals("001000")) {
                    // JR done
                    System.out.printf("%-7s R%-2s", "JR", item.get("rs"));
                } else if (binstr.substring(16, 21).equals("00000")) {
                    // bltz
                    System.out.printf("%-7s R%-2s #%-2s", "BLTZ", item.get("rs") + ",", item.get("offset"));
                } else if (binstr.substring(26, 32).equals("000010")) {
                    // SRL
                    System.out.printf("%-7s R%-2s R%-2s #%-2s", "SRL", item.get("rd") + ",", item.get("rt") + ",",
                            item.get("shamt"));
                } else if (binstr.substring(26, 32).equals("000000")) {
                    // SLL
                    System.out.printf("%-7s R%-2s R%-2s #%-2s", "SLL", item.get("rd") + ",", item.get("rs") + ",",
                            item.get("shamt"));
                } else if (binstr.substring(26, 32).equals("100000")) {
                    // ADD
                    System.out.printf("%-7s R%-2s R%-2s R%-2s", "ADD", item.get("rd") + ",", item.get("rs") + ",",
                            item.get("rt"));
                }

                else {
                    System.out.print(opcode);
                }

                System.out.println();
                MEM.put(addr, item); // ----> need to make array or something to store each item???
                i = i + 4;
            }
        }

        dis.close();

        // p2
        int[] R = new int[32];
        Arrays.fill(R, 0);
        int[] D = new int[32];
        Arrays.fill(D, 0);
        //int PC = 96;
        int cycle = 1;
        /*
        while (true) {

            Hashtable<String, Integer> I = MEM.get(PC);
            while (item.get("valid") == 0) {
                PC += 4;
                I = MEM.get(PC);
                int opcode = I.get("opcode");
                System.out.println("====================");
                System.out.printf("cycle:%3.5s%3s", cycle, PC);
                if (opcode == 40) { // ADDI
                    R[I.get("rt")] = R[I.get("rs")] + I.get("imm");
                } else if (opcode == 43) { // SW
                    // MEM[ R[ I.get("rs")] + I.get("imm") ][I.get("asInt")]= R[ I.get("rt") ] ;
                    // ----> i don't even know what this is supposed to do
                    // print('SW\tR{0}, {2}(R{1}) '.format(item['rt'], item['rs'], item['imm']) );
                }
            }
            if (cycle > 1) {
                break;
            }
        }
            */

            File file2 = new File("test1_dis.txt");
            //DataInputStream sim = new DataInputStream(new FileInputStream(file2));
            Scanner sim = new Scanner(file2);
            String ins = "";
            while (sim.hasNextLine()) {
                
                ins = sim.nextLine();
                if( !ins.contains("BREAK")){
                String addr = ins.substring(39, 43);
                String mips = ins.substring(43);
                System.out.println();
                System.out.println("====================");
                System.out.printf("cycle:%-2s %-2s%-2s", cycle, addr, mips);
                System.out.println();
                //If satements to alter the R array and D array

                    printRegisters(R);
                    System.out.println();
                    printData(D);
                cycle++;
                }

            }
        //}
            sim.close();
        }


 
    
    public static void printRegisters(int[] R){
        int reg = 0;
        System.out.println();
        System.out.print("registers:");
        for(int l = 0; l < 32; l++){
            if(l % 8 == 0){
                System.out.println();
                System.out.printf("r%2.5s", reg);
                reg = reg + 8;
            }
            System.out.printf("%2s", R[l]);

        }
    }
    public static void printData(int[] D){
        int data = 172;
        System.out.println();
        System.out.print("data:");
        for(int l = 0; l < 32; l++){
            if(l % 8 == 0){
                System.out.println();
                System.out.printf("%2.5s", data);
                data = data + 32;
            }
            System.out.printf("%2s", D[l]);

        }
    }
}
