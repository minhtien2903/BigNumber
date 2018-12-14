package bignumber;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class BigNumber implements Comparable<BigNumber> {

    private LinkedList<Byte> number = new LinkedList<Byte>();
    // indicate whether number is negative or positive
    // if sign = true the number is negative, otherwise the number is positive
    private boolean sign;

    public static BigNumber parse(String s) throws NumberFormatException {
        BigNumber b = new BigNumber();
        b.sign = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                byte val;
                val = (byte) ((int) c - 48);
                b.number.add(val);
            } else if (c == '-') {
                if (i == 0) {
                    b.sign = true;
                } else {
                    throw new NumberFormatException("Invalid format");
                }
            } else if (c == '+') {
                if (i == 0) {
                    b.sign = false;
                } else {
                    throw new NumberFormatException("Invalid format");
                }
            } else {
                throw new NumberFormatException("Invalid format");
            }
        }
        return b;
    }

    // Returns a BigInteger whose value is the absolute value of this
    // BigInteger.
    // Returns: abs(this)
    public BigNumber abs() {
        BigNumber result = new BigNumber();
        result.sign = false;
        Iterator<Byte> i = number.iterator();
        while (i.hasNext()) {
            result.number.addLast(i.next());
        }
        return result.singlify();
    }

    // Returns a BigInteger whose value is (-this).
    // Returns: -this
    // public BigNumber negate() {
    //
    // }
    // Returns a BigInteger whose value is (this + val).
    // Parameters: val - value to be added to this BigInteger.
    // Returns: this + val
    /**
     * Default add method in source code public BigNumber add(BigNumber val) {
     * BigNumber sum = new BigNumber(); byte s, carry = 0; for (int i =
     * this.number.size() - 1; i >= 0; i--) { s = (byte) (this.number.get(i) +
     * val.number.get(i) + carry); if (s > 9) { s = (byte) (s - 10); carry = 1;
     * } else carry = 0; sum.number.addFirst(s); } return sum; }
     */
    public BigNumber add(BigNumber val) {
        if (number.isEmpty()) {
            return BigNumber.parse(val.toString());
        }
        if (val.number.isEmpty()) {
            return BigNumber.parse(this.toString());
        }
        BigNumber result = new BigNumber();
        int i = number.size() - 1;
        int j = val.number.size() - 1;
        int carry = 0;
        Byte a = null, b = null;
        byte s;
        if (sign == false && val.sign == true) {
            if (this.compareTo(val) > 0) {
                return this.subtract(val.abs());
            } else {
                return val.subtract(this.abs());
            }
        }
        if (sign == true && val.sign == false) {
            return val.subtract(this.abs());
        }
        result.sign = sign;
        while (i >= 0 && j >= 0) {
            a = Byte.parseByte(number.get(i) + "");
            b = Byte.parseByte(val.number.get(j) + "");
            s = (byte) (a.byteValue() + b.byteValue() + carry);
            carry = s / 10;

            s = (byte) (s % 10);
            result.number.addFirst(s);
            i--;
            j--;
        }
        while (i >= 0) {
            a = Byte.parseByte(number.get(i) + "");
            s = (byte) (a.byteValue() + carry);
            carry = s / 10;
            s = (byte) (s % 10);
            result.number.addFirst(s);
            i--;
        }
        while (j >= 0) {
            a = Byte.parseByte(val.number.get(j) + "");
            s = (byte) (a.byteValue() + carry);
            carry = s / 10;
            s = (byte) (s % 10);
            result.number.addFirst(s);
            j--;
        }

        while (carry > 0) {
            s = (byte) (carry % 10);
            carry /= 10;
            result.number.addFirst(s);
        }
        return result.singlify();
    }

    // Returns a BigInteger whose value is (this + 1).
    // Returns: this + 1
    public BigNumber increment() {
        BigNumber result = this.add(BigNumber.parse("1"));
        this.number = result.number;
        this.sign = result.sign;
        return this;
    }

    // Returns a BigInteger whose value is (this - val).
    // Parameters: val - value to be subtracted from this BigInteger.
    // Returns: val - another
    public BigNumber subtract(BigNumber another) {
        BigNumber result = new BigNumber();
        int i = number.size() - 1;
        int j = another.number.size() - 1;
        int carry = 0;
        Byte a = null, b = null;
        byte s;
        if (sign == false) {
            if (another.sign == true) {
                return this.add(another.abs());
            } else if (this.compareTo(another) < 0) {
                result = another.abs().subtract(this);
                result.sign = true;
                return result;
            }
        } else if (another.sign == false) {
            result = this.abs().add(another.abs());
            result.sign = true;
            return result;
        } else {
            result = this.add(another.abs());
            result.sign = false;
            return result;
        }
        if (number.isEmpty()) {
            return BigNumber.parse(another.toString());
        }
        if (another.number.isEmpty()) {
            return BigNumber.parse(this.toString());
        }
        result.sign = false;
        while (i >= 0 && j >= 0) {
            a = Byte.parseByte(number.get(i) + "");
            b = Byte.parseByte(another.number.get(j) + "");
            if (a.byteValue() >= b.byteValue() + carry) {
                s = (byte) ((a.byteValue()) - b.byteValue() - carry);
                carry = 0;
            } else {
                s = (byte) ((a.byteValue() + 10) - b.byteValue() - carry);
                carry = 1;
            }
            result.number.addFirst(s);
            i--;
            j--;

        }
        while (i >= 0) {
            a = Byte.parseByte(number.get(i) + "");
            if (a >= carry) {
                s = (byte) (a.byteValue() - carry);
                carry = 0;
            } else {
                s = (byte) ((a.byteValue() + 10) - carry);
                carry = 1;
            }
            result.number.addFirst(s);
            i--;
        }

        if (j >= 0) {
            result.sign = true;
            while (j >= 0) {
                a = Byte.parseByte(another.number.get(j) + "");
                if (a > carry) {
                    s = (byte) (a.byteValue() - carry);
                    carry = 0;
                } else {
                    s = (byte) ((a.byteValue() + 10) - carry);
                    carry = 1;
                }
                result.number.addFirst(s);
                j--;
            }
        }
        if (carry > 0) {
            result.number.addFirst((byte) carry);
            result.sign = true;
        }
        return result.singlify();
    }

    // Returns a BigInteger whose value is (this - 1).
    // Returns: this - 1
    public BigNumber decrement() {
        BigNumber result = this.subtract(BigNumber.parse("1"));
        this.number = result.number;
        this.sign = result.sign;
        return this.singlify();
    }

    public BigNumber singleMultiple(BigNumber val, byte multipleNumber) {
        BigNumber result = new BigNumber();
        byte carry = 0, s;
        for (int i = val.number.size() - 1; i >= 0; i--) {
            s = (byte) (multipleNumber * val.number.get(i) + carry);
            result.number.addFirst((byte) Math.abs((s % 10)));
            carry = (byte) (s / 10);
        }
        if (carry != 0) {
            result.number.addFirst(carry);
        }
        boolean check = multipleNumber > 0 ? false : true;
        result.sign = false;
        if (check != val.sign) {
            result.sign = true;
        }
        return result.singlify();
    }

    public BigNumber singlify() {
        while (number.size() > 1 && number.get(0) == 0) {
            number.removeFirst();
        }
        if (number.size() == 1 && number.get(0) == 0) {
            sign = false;
        }
        return this;
    }

    // Returns a BigInteger whose value is (this * val).
    // Parameters: val - value to be multiplied by this BigInteger.
    // Returns: this * val
    public BigNumber multiply(BigNumber val) {
        if (this.compareTo(val) < 0) {
            return val.multiply(this);
        }
        BigNumber result = new BigNumber();
        BigNumber absThis = this.abs();
        for (int i = val.number.size() - 1; i >= 0; i--) {
            BigNumber product = singleMultiple(absThis, val.number.get(i));
            for (int j = 0; j < val.number.size() - 1 - i; j++) {
                product.number.addLast((byte) 0);
            }
            result = result.add(product);
        }
        if (this.sign != val.sign) {
            result.sign = true;
        } else {
            result.sign = false;
        }
        return result.singlify();
    }

   

    // Returns a BigInteger whose value is (this / val).
    // Parameters: val - value by which this BigInteger is to be divided.
    // Returns: this / val
    // Throws: ArithmeticException - if val is zero.
    public BigNumber divide(BigNumber val) throws ArithmeticException {
        boolean resultSign = !(this.sign == val.sign);
        val = val.abs();
        BigNumber zero = BigNumber.parse("0");
        if (val.compareTo(zero) == 0) {
            throw new ArithmeticException();
        }
        BigNumber result = new BigNumber();
        BigNumber divideNumber = BigNumber.parse(this.toString()).abs();
        BigNumber divisor = BigNumber.parse(val.toString()).abs();
        BigNumber currentDevideNumber = new BigNumber(), tmp;
        BigNumber ten = BigNumber.parse("10");
        int index = 0, i = 0;
        byte s;
        while (i < divideNumber.number.size() && currentDevideNumber.compareTo(divisor) < 0) {
        	currentDevideNumber.number.addLast(divideNumber.number.get(i));
        	i++;
        }
        while (i <= divideNumber.number.size()) {
        	s = 0;
//        	System.out.println(currentDevideNumber);
        	tmp = BigNumber.parse(currentDevideNumber.toString());
        	while (tmp.compareTo(divisor) >= 0) {
        		s = (byte) (s +1);
        		tmp = tmp.subtract(divisor);
        	}
        	result.number.addLast(s);
        	tmp = divisor.singleMultiple(divisor, s);
        	while (tmp.compareTo(zero) != 0 && tmp.multiply(ten).compareTo(currentDevideNumber) <= 0) {
        		tmp.number.addLast((byte) 0);
        	}
        	currentDevideNumber = currentDevideNumber.subtract(tmp).singlify();
        	if (i < divideNumber.number.size()) {
        		currentDevideNumber.number.addLast(divideNumber.number.get(i));
        		currentDevideNumber = currentDevideNumber.singlify();
        		i++;
        	} else break;
        }
        
        result.sign = resultSign;
        if (result.number.isEmpty()) {
            result = zero;
        }
        return result.compareTo(zero) != 0 ? result.singlify() : result;
    }

    // Returns a BigInteger whose value is exponent of this. Note that exponent
    // is an integer rather than a BigInteger.
    // Parameters: exponent - exponent to which this BigInteger is to be raised.
    // Returns: value of exponent of this.
    // Throws: ArithmeticException - exponent is negative. (This would cause the
    // operation to yield a non-integer value.)
    // public BigInteger pow(int exponent) {
    //
    // }
    public String toString() {
        String str = "";
        Iterator<Byte> it = number.iterator();
        while (it.hasNext()) {
            str += it.next().toString();
        }
        if (sign) {
            str = "-" + str;
        }
        return str;
    }

    // 0 equal
    // positive bigger
    // negative smaller
    @Override
    public int compareTo(BigNumber val) {
        // TODO Auto-generated method stub
        if (sign != val.sign) {
            if (!sign) {
                return 1;
            } else {
                return -1;
            }
        } else if (sign) {
            if (number.size() < val.number.size()) {
                return 1;
            } else if (number.size() > val.number.size()) {
                return -1;
            } else {
                int check = 0;
                for (int i = 0; i < number.size() && (check == 0); i++) {
                    check = number.get(i) - val.number.get(i);
                }
                return check * -1;
            }
        } else if (number.size() < val.number.size()) {
            return -1;
        } else if (number.size() > val.number.size()) {
            return 1;
        } else {
            int check = 0;
            for (int i = 0; i < number.size() && (check == 0); i++) {
                check = number.get(i) - val.number.get(i);
            }
            return check;
        }
    }
}

class TestBigNumber {

    public static void main(String[] args) {
        System.out.print("Enter integer 1: ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        BigNumber a = BigNumber.parse(input);
        System.out.print("Enter integer 2: ");
        input = sc.nextLine();
        BigNumber b = BigNumber.parse(input);
//        System.out.println("Abs of a =  " + a.abs());
//        System.out.println("Abs of b =  " + b.abs());
        System.out.println("a + b = " + a.add(b));
        System.out.println("a - b = " + a.subtract(b));
        System.out.println("a * b = " + a.multiply(b));
        System.out.println("a / b = " + a.divide(b));
    }
}
