package pers.adlered.liteftpd.tool;

import java.util.Random;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Sum random num.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class RandomNum {
    public static boolean debugMode = false; //为true 控制台会显示计算过程输出(影响取随机数性能)

    public static int sumIntger(int min, int max, boolean needNegative) {
        int result;
        Random random = new Random();
        int sumNum = random.nextInt(max - min + 1) + min;
        if (needNegative == true) {
            int temp = random.nextInt(10) + 1;
            Int2String i2s = new Int2String(0);
            i2s.setInt(sumNum);
            String randomArg = i2s.returnString();
            switch (temp) {
                case 1:
                    if ((randomArg.contains("0")) || (randomArg.contains("9"))) {
                        randomArg = "-" + randomArg;
                        break;
                    }
                case 2:
                    if ((randomArg.contains("1")) || (randomArg.contains("8"))) {
                        randomArg = "-" + randomArg;
                        break;
                    }
                case 3:
                    if ((randomArg.contains("2")) || (randomArg.contains("7"))) {
                        randomArg = "-" + randomArg;
                        break;
                    }
                case 4:
                    if ((randomArg.contains("3")) || (randomArg.contains("6"))) {
                        randomArg = "-" + randomArg;
                        break;
                    }
                case 5:
                    if ((randomArg.contains("4")) || (randomArg.contains("5"))) {
                        randomArg = "-" + randomArg;
                        break;
                    }
            }
            sumNum = Integer.parseInt(randomArg);
            result = sumNum;
            return result;
        }
        return sumNum;
    }

    public static double sumDecimal(double min, double max, boolean needNegative) {
        double result = 0;
        int minInt = (int) min;
        if (debugMode == true) {
            System.out.println("(最低) 整数位 = " + minInt);
        }
        String temp = min + "";
        String temp2 = temp.substring(temp.indexOf("."));
        String temp3 = temp2.replace(".", ""); //提取出小数位
        if (debugMode == true) {
            System.out.println("(最低) 小数位 = " + temp3);
            System.out.println("最低小数位长度: " + temp3.length());
        }
        int maxInt = (int) max;
        if (debugMode == true) {
            System.out.println("(最高) 整数位: " + maxInt);
        }
        String decTemp = max + "";
        String decTemp2 = decTemp.substring(temp.indexOf("."));
        String decTemp3 = decTemp2.replace(".", "");
        if (debugMode == true) {
            System.out.println("(最高) 小数位: " + decTemp3);
            System.out.println("最高小数位长度: " + decTemp3.length());
        }
        // String结果转int
        int minDecimalResult;
        int maxDecimalResult;
        minDecimalResult = Integer.valueOf(temp3).intValue();
        maxDecimalResult = Integer.valueOf(decTemp3).intValue();
        if (debugMode == true) {
            System.out.println("转换后小数范围: " + minDecimalResult + " " + maxDecimalResult);
        }
        // 先生成一个整数位范围内的数字
        // 再生成一个小数位范围内的数字
        // 最后判断是否符合范围要求, 如果符合 返回 如果不符合 重来
        boolean isOk = false; //do while会检查isOk 如果不符合条件(isOk = true)会重来
        do {
            // 整数数字生成
            int getInt = sumIntger(minInt, maxInt, false);
            if (debugMode == true) {
                System.out.println("整数数字已生成: " + getInt);
            }
            // 小数数字生成 随机就好 注意小数位数
            // 其实不用看最低小数,取0-最高小数就可以了
            int getDec = 0;
            if (minInt == maxInt) {
                if (minDecimalResult < maxDecimalResult)
                    getDec = sumIntger(minDecimalResult, maxDecimalResult, false);
                if (minDecimalResult > maxDecimalResult)
                    getDec = sumIntger(maxDecimalResult, minDecimalResult, false);
            } else { //瞎猫碰上死耗子
                if (minDecimalResult < maxDecimalResult)
                    getDec = sumIntger(0, maxDecimalResult, false);
                if (minDecimalResult > maxDecimalResult)
                    getDec = sumIntger(0, minDecimalResult, false);
            }
            if (debugMode == true) {
                System.out.println("小数数字已生成: " + getDec);
            }
            // 开始组合
            String spell = getInt + "." + getDec;
            double getSpell = Double.valueOf(spell);
            if (debugMode == true) {
                System.out.println("随机数已生成: " + getSpell);
            }
            // if (getDec >= minDecimalResult && getDec <= maxDecimalResult) {
            if (minInt != maxInt) { //算法不同 不相为谋
                if (getInt == minInt && getDec >= minDecimalResult) {
                    isOk = true;
                } else if (getInt == maxInt && getDec <= maxDecimalResult) {
                    isOk = true;
                } else if (getInt != minInt && getInt != maxInt) {
                    isOk = true;
                }
            } else {
                if (getDec >= minDecimalResult && getDec <= maxDecimalResult) {
                    isOk = true;
                }
            }
            // 如果整数不是最小也不是最大 那小数位就可以随便生成了
            if (isOk == false) {
                if (debugMode == true) {
                    System.out.println("随机生成的随机数不符合要求,自动重新生成...");
                }
            } else {
                result = getSpell;
            }
        } while (isOk == false);
        // 利用判断 看看组合后的小数是否在设定的范围之内(有点影响性能...不过可以实现)
        if (needNegative == true) {
            Random random = new Random();
            int tempR = random.nextInt(10) + 1;
            Double2String d2s = new Double2String(result);
            String randomArg = d2s.returnString();
            switch (tempR) {
                case 1:
                    if ((randomArg.contains("0")) || (randomArg.contains("9"))) {
                        randomArg = "-" + randomArg;
                        break;
                    }
                case 2:
                    if ((randomArg.contains("1")) || (randomArg.contains("8"))) {
                        randomArg = "-" + randomArg;
                        break;
                    }
                case 3:
                    if ((randomArg.contains("2")) || (randomArg.contains("7"))) {
                        randomArg = "-" + randomArg;
                        break;
                    }
                case 4:
                    if ((randomArg.contains("3")) || (randomArg.contains("6"))) {
                        randomArg = "-" + randomArg;
                        break;
                    }
                case 5:
                    if ((randomArg.contains("4")) || (randomArg.contains("5"))) {
                        randomArg = "-" + randomArg;
                        break;
                    }
            }
            double getRes = Double.valueOf(randomArg);
            result = getRes;
        }
        if (debugMode == true) {
            System.out.println("最终取数: " + result);
        }
        return result;
    }

    static class Int2String {
        String ArgNum = "";

        public Int2String(int RecivedNum) {
            ArgNum = RecivedNum + "";
        }

        String returnString() {
            return ArgNum;
        }

        void setInt(int RecivedNum) {
            ArgNum = RecivedNum + "";
        }
    }

    static class Double2String {
        String ArgNum = "";

        public Double2String(double RecivedNum) {
            ArgNum = RecivedNum + "";
        }

        String returnString() {
            return ArgNum;
        }

        void setDouble(double RecivedNum) {
            ArgNum = RecivedNum + "";
        }
    }
}
