import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.Map;

public class mipssim {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        String inputFile = "test1.bin"; // will be = args[0];
        String file1 = " ";
        String file2 = " ";
        
        if(inputFile.equals("test1.bin")){
            file1= "test1_dis.txt";
            file2 = "test1_sim.txt";
        }
        else if(inputFile.equals("test2.bin")){
            file1 = "test2_dis.txt";
            file2 = "test2_sim.txt";
        }else if(inputFile.equals("test3.bin")){
            file1 = "test3_dis.txt";
            file2 = "test3_sim.txt";
        }
        File file = new File(inputFile);
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        PrintStream o = new PrintStream(new File(file1));
        System.setOut(o);
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
                } else if (binstr.substring(26, 32).equals("100010")) {
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
                    System.out.printf("%-7s R%-2s R%-2s #%-2s", "SLL", item.get("rd") + ",", item.get("rt") + ",",
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
        o.close();

       PrintStream b = new PrintStream(file2);
       System.setOut(b);

        // p2
        int[] R = new int[32];
        Arrays.fill(R, 0);

        // I don't even know what the data is???
        int[] D = new int[32];
        Arrays.fill(D, 0);
        int cycle = 1;
       
        DataInputStream sim = new DataInputStream(new FileInputStream(file1));
        //Scanner sim = new Scanner(file1);
        String ins = "";
        while (sim.available() > 0) {

            ins = sim.readLine();
            if (!ins.contains("Invalid")  && ins.length() > 13) {
                String addr = ins.substring(40 , 43);
                String mips = ins.substring(43);

                if(addr.equals("96 ") || addr.equals(" 96")){
                    addr = "96";
                }

                Hashtable<String, Integer> I = MEM.get(Integer.parseInt(addr));

                
                System.out.println("====================");
                System.out.printf("cycle:%-2s%-5s%3s%s", cycle, addr, "", mips);
                System.out.println();
                // If satements to alter the R array and D array
                if (ins.contains("ADDI")) {
                    // not correct and i dont know why
                    R[I.get("rt")] = (R[I.get("rs")] + I.get("imm"))*I.get("asInt");
                } /*
                   * //some of these instructions do not change anything so we might not need
                   * //to include them.
                   * else if(ins.contains("SW")){
                   * 
                   * }
                   * else if(ins.contains("LW")){
                   * 
                   * }
                   * else if(ins.contains("SUB")){
                   * 
                   * }
                   * else if(ins.contains("J")){
                   * 
                   * }
                   * else if(ins.contains("MUL")){
                   * 
                   * }
                   * else if(ins.contains("NOP")){
                   * 
                   * }
                   * else if(ins.contains("MOVZ")){
                   * 
                   * }
                   * else if(ins.contains("BREAK")){
                   * 
                   * }
                   * else if(ins.contains("JR")){
                   * 
                   * }
                   * else if(ins.contains("BLTZ")){
                   * //no change
                   * }
                   * else if(ins.contains("SRL")){
                   * 
                   * }
                   * else if(ins.contains("SLL")){
                   * 
                   * }
                   * else if(ins.contains("ADD")){
                   * 
                   * }
                   * 
                   */

                printRegisters(R);
                System.out.println();
                //printData(D);
                cycle++;
                System.out.println();
            }

        }
        // }
        sim.close();
        b.close();
    }

    public static void printRegisters(int[] R) {
        int reg = 0;
        System.out.println();
        System.out.print("registers:");
        for (int l = 0; l < 32; l++) {
            if (l % 8 == 0) {
                System.out.println();
                System.out.printf("r%2.5s:", reg); // not correct spacing need to replace spaces with 0
                reg = reg + 8;
            }
            System.out.printf("%5s", R[l]); // not correct spacing

        }
    }

    public static void printData(int[] D) {
        int data = 172;
        System.out.println();
        System.out.print("data:");
        for (int l = 0; l < 32; l++) {
            if (l % 8 == 0) {
                System.out.println();
                System.out.printf("%2.5s:", data); // ditto
                data = data + 32;
            }
            System.out.printf("%2s", D[l]); // ditto

        }
    }
}
