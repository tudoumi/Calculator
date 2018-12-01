import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String strIn;//="1+(2-3*(5+1)/(-4+2*(-6))-3*(+6-(-2)))+6*(5-4)";
        System.out.println("请输入需要计算的式子：");
        strIn = input.nextLine();
        center(strIn);
        input.close();
    }

    private static void center(String strIn)//计算器主体
    {
        //计算前进行验证处理
        judge(strIn);
        String str0 = strIn;
        strIn = minus(strIn);
        strIn = plus(strIn);
        strIn = addBrackets(strIn);
        ArrayList<String> count1 = new ArrayList<>();
        ArrayList<Double> count2 = new ArrayList<>();
        ArrayList<Double> re = new ArrayList<>();
        ArrayList<Double> num = separateNum(strIn);
        ArrayList<Integer> serNum = new ArrayList<>();
        ArrayList<String> sym = separateSym(strIn);
        serNum = serialNumber(sym);//统计数字
        double result = 0;//总计算结果
        int cm = brackets0(sym);//总括号数
        int bra[][] = new int[cm][2];
        bra = brackets(sym);//括号统计
        int m = 0;//m 最大层数
        for (int i = 0; i < bra.length; i++) {
            if (m < bra[i][0]) {
                m = bra[i][0];
            }
        }
        int i = 0, k, t = 0, d, f = 1, g = 1, n, r1 = -1, r2 = 0, l1 = -1, l2 = -1;//t,d 相对应的一对括号
        for (i = m; i >= 0; i--)//层数循环
        {
            for (t = 0; t < bra.length; t++) {
                if (bra[t][0] == i)//每一对括号
                {
                    d = t;
                    count1.clear();//清空
                    count2.clear();
                    re.clear();
                    for (; ; )//找出相对应的另一半括号
                    {
                        d++;
                        if (bra[d][0] == bra[t][0]) {
                            break;
                        }
                    }
                    l1 = bra[t][1];
                    r1 = bra[d][1];
                    for (k = bra[t][1] + 2; k < bra[d][1]; k += 2) {

                        count1.add(sym.get(k));

                    }

                    for (k = 0, g = 1, l2 = 0, r2 = 0; k < serNum.size(); k++)
                    {
                        n = serNum.get(k);
                        if (n > bra[t][1] && n < bra[d][1]) {
                            count2.add(num.get(k));
                            r2++;
                            if (g == 1) {
                                l2 = k;
                                g = 0;
                            }
                        }
                    }
                    for (int x = l1; x <= r1 && l1 >= 0 && r1 >= 0; x++) {
                        sym.set(x, "@");
                    }
                    //调用函数计算
                    result = caculate(count1, count2);
                    //删除
                    int z = l1;
                    while (z >= 0) {
                        sym.remove(z);
                        z = sym.indexOf("@");

                    }
                    sym.add(l1, "0");
                    for (z = 0; z < r2; z++) {
                        num.remove(l2);
                    }
                    num.add(l2, result);
                    //计算
                    bra = brackets(sym);
                    serNum = serialNumber(sym);
                    //System.out.println(d);
                    t = 0;
                }
            }
        }
        output(str0, result);

    }


    private static String aToS(ArrayList<String> a)  //将 String 转化为 ArrayList
    {
        String s = "";
        for (int i = 0; i < a.size(); i++) {
            s = s + a.get(i);
        }
        return s;
    }

    private static String minus(String s)
    {
        String a, b;
        int i = 1;
        if (s.charAt(0) == '-') {
            s = "0" + s;
        }
        while (i != -1) {
            i = s.indexOf('-', i);
            if (i >= 0) {
                if (i > 0 && s.charAt(i - 1) == '(') {
                    a = s.substring(0, i);
                    b = s.substring(i);
                    s = a + "0" + b;
                    i += 2;
                } else {
                    i++;
                }

            } else
                break;

        }
        return s;
    }

    private static String plus(String s)
    {
        String a, b;
        int i = 1;
        if (s.charAt(0) == '+') {
            s = "0" + s;
        }
        while (i != -1) {
            i = s.indexOf('+', i);
            if (i >= 0) {

                if (i > 0 && s.charAt(i - 1) == '(') {
                    a = s.substring(0, i);
                    b = s.substring(i);
                    s = a + "0" + b;
                    i += 2;
                } else {
                    i++;
                }

            } else
                break;
            //System.out.println(s);
        }
        return s;
    }

    private static String addBrackets(String s) {
        if (!(s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')')) {
            s = "(" + s + ")";
        }
        return s;
    }

    private static int brackets0(ArrayList<String> str)   //实现括号的粗略统计
    {
        ArrayList<String> s = new ArrayList<>(str);
        int c = 0, i = 0;
        for (; ; ) {
            if ((i = s.indexOf("(")) < 0) {
                break;
            }
            s.remove(i);
            c++;
        }
        for (; ; ) {
            if ((i = s.indexOf(")")) < 0) {
                break;
            }
            s.remove(i);
            c++;
        }
        return c;
    }

    private static ArrayList<Integer> serialNumber(ArrayList<String> s) //实现数字的统计
    {
        ArrayList<Integer> a = new ArrayList<Integer>();
        int i = 0;
        String str;
        char c;
        for (i = 0; i < s.size(); i++) {
            str = s.get(i);
            c = str.charAt(0);
            if (c >= '0' && c <= '9') {
                a.add(i);
            }

        }
        return a;
    }

    private static int[][] brackets(ArrayList<String> sym) //实现括号的统计
    {
        //                   +(-*(+)/(-+*(-))-*(+-(-)))+*(-)
        ArrayList<Integer> b1 = new ArrayList<>();//层数
        ArrayList<Integer> b2 = new ArrayList<>();//位置
        int c = -1;//层数
        int cm = 0;//最大层数
        int i, f = 1;
        String s = aToS(sym);
        for (i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                if (f == 1) {
                    c++;
                }
                f = 1;
                b1.add(c);
                b2.add(i);
            }
            if (s.charAt(i) == ')') {
                if (f == 0) {
                    c--;
                }
                f = 0;
                b1.add(c);
                b2.add(i);
            }
            if (cm < c) {
                cm = c;
            }

        }


        int bra[][] = new int[b1.size()][2];
        for (i = 0; i < b1.size(); i++) {
            bra[i][0] = b1.get(i);
            bra[i][1] = b2.get(i);
        }

        return bra;

    }

    private static double caculate(ArrayList<String> s, ArrayList<Double> a) //计算

    {
        double result = 0, left, right;
        int i = 0;
        while (i >= 0) {
            i = s.indexOf("/");
            if (i < 0) break;
            left = a.remove(i);
            right = a.remove(i);
            try {
                if (Math.abs(right) < 10e-8) {
                    throw new Exception("除数不能为零！");
                }
                a.add(i, left / right);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            s.remove(i);
        }
        i = 0;
        while (i >= 0) {
            i = s.indexOf("*");
            if (i < 0) break;
            left = a.remove(i);
            right = a.remove(i);
            a.add(i, left * right);
            s.remove(i);
        }
        i = 0;
        while (i >= 0) {
            i = s.indexOf("-");
            if (i < 0) break;
            left = a.remove(i);
            right = a.remove(i);
            a.add(i, left - right);
            s.remove(i);
        }
        i = 0;
        while (i >= 0) {
            i = s.indexOf("+");
            if (i < 0) break;
            left = a.remove(i);
            right = a.remove(i);
            a.add(i, left + right);
            s.remove(i);
        }

        //end
        result = a.get(0);
        return result;
    }


    private static ArrayList<Double> separateNum(String s) {
        ArrayList<Double> num = new ArrayList<Double>();
        String c = "";
        int i = 0, t = 0, f = 0, l;
        double d = 0, a;
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                c = c + s.charAt(i);
                f = 1;
                //    System.out.println("add"+c);
            } else if (f == 1) {
                //字符转数字
                l = c.length();
                for (t = 0, d = 0; t < l; t++) {
                    a = c.charAt(t) - '0';

                    d = d + a * Math.pow(10, l - 1 - t);

                }

                num.add(d);
                f = 0;
                c = "";
            }
        }

        return num;
    }

    private static ArrayList<String> separateSym(String s) {
        ArrayList<String> sym = new ArrayList<String>();
        int f = 1;
        if (s.charAt(0) < '0' || s.charAt(0) > '9') {
            s = "0+" + s;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                f = 1;
            } else if (f == 1) {
                sym.add("0");
                f = 0;
            }
            if (s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '*' || s.charAt(i) == '/' || s.charAt(i) == '(' || s.charAt(i) == ')') {
                sym.add(s.substring(i, i + 1));
            }

        }
        //System.out.println(sym);
        sym.remove(0);
        sym.remove(0);
        return sym;
    }

    private static void judge(String s)//验证式子是否正确
    {
        try {
            //字符是否正确
            for (int i = 0, f = 0; i < s.length(); i++) {
                f = 0;
                if (s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '*' || s.charAt(i) == '/' || s.charAt(i) == '(' || s.charAt(i) == ')') {
                    f = 1;
                }
                if (s.charAt(i) >= 'a' && s.charAt(i) <= 'z' || s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') {
                    f = 1;
                }
                if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                    f = 1;
                }
                if (f == 0) throw new Exception("未识别的符号\" " + s.charAt(i) + " \"，位置：" + (i + 1));
            }
            //括号是否匹配
            int left = 0, right = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(') {
                    left++;
                }
                if (s.charAt(i) == ')') {
                    right++;
                }
            }


            if (left != right) throw new Exception("括号不匹配");
            //符号是否正确
            int f = 1;
            if (s.charAt(0) == '-') {
                s = "0" + s;
                f = 0;
            }
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                    if (s.charAt(i - 1) == '+' || s.charAt(i - 1) == '-' || s.charAt(i - 1) == '*' || s.charAt(i - 1) == '/') {
                        throw new Exception("运算符" + s.charAt(i) + "左边没有数字，位置：" + (i + f));
                    }
                    if (s.charAt(i + 1) == '+' || s.charAt(i + 1) == '-' || s.charAt(i + 1) == '*' || s.charAt(i + 1) == '/' || s.charAt(i + 1) == ')') {
                        throw new Exception("运算符" + s.charAt(i) + "右边没有数字，位置：" + (i + f));
                    }
                    if (s.charAt(s.length() - 1) == '+' || s.charAt(s.length() - 1) == '-' || s.charAt(s.length() - 1) == '*' || s.charAt(s.length() - 1) == '/') {
                        throw new Exception("运算符" + s.charAt(s.length() - 1) + "右边没有数字，位置：" + (s.length() - 1 + f));
                    }
                }
                if (s.charAt(i) == '*' || s.charAt(i) == '/') {
                    if (s.charAt(i - 1) == '+' || s.charAt(i - 1) == '-' || s.charAt(i - 1) == '*' || s.charAt(i - 1) == '/' || s.charAt(i - 1) == '(') {
                        throw new Exception("运算符" + s.charAt(i) + "左边没有数字，位置：" + (i + f));
                    }
                    if (s.charAt(i + 1) == '+' || s.charAt(i + 1) == '-' || s.charAt(i + 1) == '*' || s.charAt(i + 1) == '/' || s.charAt(i + 1) == ')') {
                        throw new Exception("运算符" + s.charAt(i) + "右边没有数字，位置：" + (i + f));
                    }
                    if (s.charAt(s.length() - 1) == '+' || s.charAt(s.length() - 1) == '-' || s.charAt(s.length() - 1) == '*' || s.charAt(s.length() - 1) == '/') {
                        throw new Exception("运算符" + s.charAt(s.length() - 1) + "右边没有数字，位置：" + (s.length() - 1 + f));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void output(String s, double b) {
        System.out.println(s + " = " + b);
    }
}