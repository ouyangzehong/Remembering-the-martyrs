package entity;

/**
 * Created by Dustray on 2017/4/4 0004.
 */

public class RandomNumber {
    /**
     * n位随机数
     * @param n
     * @return
     */
    public long getRandomNumber(int n){
        if(n<1){
            throw new IllegalArgumentException("随机数位数必须大于0");
        }
        return (long)(Math.random()*9*Math.pow(10,n-1)) + (long)Math.pow(10,n-1);
    }
}
