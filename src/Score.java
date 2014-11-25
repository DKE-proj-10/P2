import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Rico Montulet on 11-11-2014.
 */
public class Score implements Comparable<Score>, Serializable{
    private static final transient DecimalFormat SCOREFORMAT = new DecimalFormat("00000");
    private int amountPoint=0;
    private String name="";
    private Date today;
    public Score(String name, int amountPoint){
        this.name = name;
        this.amountPoint=amountPoint;
        this.today=new Date();
    }

    public String getName() {
        return name;
    }

    public int getAmountPoint() {
        return amountPoint;
    }

    public Date getToday() {
        return today;
    }

    @Override
    public int compareTo(Score o) {
        //Descending high score.
        if (this.amountPoint<o.getAmountPoint()){
            return 1;
        }
        if (o.getAmountPoint()<this.amountPoint){
            return -1;
        }
        return today.compareTo(o.getToday());
    }

    @Override
    public String toString() {
        return SCOREFORMAT.format(amountPoint) +" : "+name;
    }
}
