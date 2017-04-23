/**
 * 
 */
package models;

public class Sentiment {    
    int positive;
    int negative;
    int neutral;   
    
    public Sentiment(){
        this.positive = 0;
        this.negative = 0;
        this.neutral = 0;
    }
    public int getPositive() {
        return positive;
    }
    public void setPositive(int positive) {
        this.positive = positive;
    }
    public int getNegative() {
        return negative;
    }
    public void setNegative(int negative) {
        this.negative = negative;
    }
    public int getNeutral() {
        return neutral;
    }
    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }    
    
    public void incrementPositive(){
        this.positive = this.positive + 1;
    }
    
    public void incrementNegative(){
        this.negative = this.negative + 1;
    }
    
    public void incrementNeutral(){
        this.neutral = this.neutral + 1;
    }
}
