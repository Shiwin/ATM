package currency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IllegalFormatException;

/**
 * Created by ivan on 21.07.15.
 */
public class MyDecimail {

    private static final byte SHIFT = 48;//distance between value and askii value

    /**
     * if false then < 0
     * if true then > 0
     */
    private boolean sign;
    private ArrayList<Byte> integer;
    private ArrayList<Byte> fraction;

    public MyDecimail(boolean sign, ArrayList<Byte> integer, ArrayList<Byte> fraction) {
        this.sign = sign;
        this.integer = integer;
        this.fraction = fraction;
    }

    public MyDecimail(String number){
        try {
            this.sign = number.charAt(0) != '-';
            int pointIndex = number.indexOf(".");
            byte[] bytes = number.getBytes();
            if (pointIndex==-1){//no fraction part
                ArrayList<Byte> integer = new ArrayList<>();
                for(int i = !sign?1:0;i<bytes.length;i++){
                    byte buf = (byte) (bytes[i]-SHIFT);//ascii - SHIFT -> real value
                    integer.add(buf);
                }
                Collections.reverse(integer);//low index - low cost of digit
                this.integer = integer;
                this.fraction = new ArrayList<>();
            }else{//with float part
                ArrayList<Byte> fraction = new ArrayList<>();
                ArrayList<Byte> integer = new ArrayList<>();
                for(int i = !sign?1:0;i<pointIndex;i++){
                    byte buf = (byte) (bytes[i]-SHIFT);//ascii - SHIFT -> real value
                    integer.add(buf);
                }
                this.integer = integer;
                for(int i = pointIndex+1;i<bytes.length;i++){
                    byte buf = (byte) (bytes[i]-SHIFT);//ascii - SHIFT -> real value
                    fraction.add(buf) ;
                }
                Collections.reverse(integer);
                this.fraction = fraction;
                this.integer = integer;
            }
        }catch (IllegalFormatException e){
            this.sign = true;
            this.integer = new ArrayList<>(0);
            this.fraction = new ArrayList<>();
        }
    }


    public ArrayList<Byte> getInteger() {
        return integer;
    }

    public ArrayList<Byte> getFraction() {
        return fraction;
    }

    public boolean isPositive(){
        return sign;
    }

    /**
     * use only for unimportant arguments
     */
    private void negate(){
        this.sign = ! sign;
    }

    public MyDecimail add(MyDecimail arg){
        Boolean sign = true;
        int minIntegerSize = Math.min(this.getInteger().size(), arg.getInteger().size());
        ArrayList<Byte> integer = new ArrayList<>();
        int maxFractionSize = Math.max(this.getFraction().size(), arg.getFraction().size());
        ArrayList<Byte> fraction = new ArrayList<>();

        while (this.getFraction().size() < maxFractionSize) {//fill fraction in this
            this.getFraction().add((byte) 0);
        }

        while (arg.getFraction().size() < maxFractionSize) {//fill fraction in arg
            arg.getFraction().add((byte) 0);
        }

        if(this.isPositive()&&arg.isPositive()||!(this.isPositive()|| arg.isPositive())) {//both are positive or negative in one time

            sign = this.isPositive();
            int buf = 0;

            for (int i = maxFractionSize -1 ; i >= 0; i--) {
                int sum = this.getFraction().get(i) + arg.getFraction().get(i) + buf;
                buf = sum / 10;
                fraction.add((byte) (sum % 10));
            }
            Collections.reverse(fraction);


            int i = 0;
            for (; i < minIntegerSize; i++) {
                int sum = this.getInteger().get(i) + arg.getInteger().get(i) + buf;
                buf = sum / 10;
                integer.add((byte) (sum % 10));
            }

            while (i < this.getInteger().size()) {
                int sum = this.getInteger().get(i) + buf;
                buf = sum / 10;
                integer.add((byte) (sum % 10));
                i++;
            }

            while (i < arg.getInteger().size()) {
                int sum = arg.getInteger().get(i) + buf;
                buf = sum / 10;
                integer.add((byte) (sum % 10));
                i++;
            }

            if(buf!=0){
                integer.add((byte) buf);
            }
        }else{//one number - positive and other - negative. add => sub
            MyDecimail minuend = this.isPositive()?this:arg;
            MyDecimail subtrahend = this.isPositive()?arg:this;


            while (minuend.getFraction().size() < maxFractionSize) {//fill fraction in this
                minuend.getFraction().add((byte) 0);
            }

            while (subtrahend.getFraction().size() < maxFractionSize) {//fill fraction in arg
                subtrahend.getFraction().add((byte) 0);
            }

            int buf = 0;

            for (int i = maxFractionSize-1; i>=0 ; i--) {
                int sub = minuend.getFraction().get(i) - subtrahend.getFraction().get(i) - buf;
                if (sub<0){
                    buf = 1;
                    fraction.add((byte) (10-sub));
                }else{
                    fraction.add((byte) sub);
                }
            }
            Collections.reverse(fraction);


            int i=0;
            for(;i<minIntegerSize;i++){
                int sub = minuend.getInteger().get(i) - subtrahend.getInteger().get(i) - buf;
                if (sub<0){
                    buf = 1;
                    integer.add((byte) (10 - sub));
                }else{
                    buf = 0;
                    integer.add((byte) sub);
                }
            }
            while (i<minuend.getInteger().size()){
                int sub = minuend.getInteger().get(i) - buf;
                if(sub<0){
                    buf = 1;
                    integer.add((byte) (10 - sub));
                }else {
                    buf = 0;
                    integer.add((byte) sub);
                }
                i++;
            }
            while (i<subtrahend.getInteger().size()){
                int sub = subtrahend.getInteger().get(i) - buf;
                buf = 1;
                if(sub<0){
                        integer.add((byte) (-sub));
                }else {
                    integer.add((byte) sub);
                }
                i++;
            }

            i = integer.size() - 1;
            while (integer.get(i)==0){
                integer.remove(i);
                i--;
            }

            sign = buf != 1;
        }

        return new MyDecimail(sign,integer,fraction);
    }

    public MyDecimail sub(MyDecimail arg){
        arg.negate();
        return this.add(arg);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(!sign){
            sb.append("-");
        }
        for (int i = integer.size()-1; i >=0 ; i--) {
            sb.append(integer.get(i));
        }
        if(fraction.size()!=0){
            sb.append(".");
            for (int i = 0; i<fraction.size() ; i++) {
                sb.append(fraction.get(i));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {


        MyDecimail md = new MyDecimail("-123233.1234423");
        MyDecimail md1 = new MyDecimail("13.302");
        MyDecimail md2 = new MyDecimail("-112");
        MyDecimail md3 = new MyDecimail("99999.0011");
        MyDecimail md4 = new MyDecimail("0");
//        System.out.println(md.toString()+"\n"+md1+"\n"+md2+"\n"+md3+"\n"+md4);
        MyDecimail sum = md1.add(md2);
        System.out.println("\n"+sum);

    }
}
