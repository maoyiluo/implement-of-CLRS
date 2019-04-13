import com.sun.media.jfxmedia.locator.ConnectionHolder;
import java.lang.Math;
import java.util.ArrayList;

class StringMatchingBase{
    public String context;
    public int d; //number of alphabat;
    public char[] alphabat = {'a','b','c'};
    public StringMatchingBase(){
        context = "abcbaccabccbbacca"; //a trivial case for testing. Using 
        d = 3;
    };

    public int value_of_char(char a){
        if(a == 'a')
            return 0;
        else if(a == 'b')
            return 1; 
        else
            return 2;
    }

    public void matching(String pattern){
        return;
    }

}

class Rabin_krap extends StringMatchingBase{
    
    public void matching(String pattern){
        char[] p = context.toCharArray();
        char[] q = pattern.toCharArray();
        ArrayList<Integer> matchPosition = new ArrayList<Integer>();
        int p_value = 0;
        int q_value = 0;
        if(p.length < q.length){
            System.out.println("pattern is longer than context, matching is meanless in this case");
            return;
        }
        for(int i = 0; i<q.length; i++){
            p_value = d*p_value + value_of_char(p[i]);
            q_value = d*q_value + value_of_char(q[i]);
        }
        int h = (int)Math.pow(d,q.length - 1);
        for(int s = 0; s<=p.length - q.length; s++){
            if(p_value == q_value){
                for(int i = 0; i<q.length; i++){
                    if(q[i] != p[s+i])
                        break;
                    if(i == q.length -1 && q[i] == p[s+i]){
                        matchPosition.add(s);
                        matchPosition.add(s+i+1);
                    }
                }
            }
            else if(s + q.length < p.length){
                p_value = d*(p_value - h*value_of_char(p[s])) + value_of_char(p[s+q.length]);
            }
        }
        System.out.printf("The context wait for matching is:\n%s\n", context);
        if(matchPosition.size() == 0){
            System.out.println("No pattern in context!");
        }
        else{
            int i = 0;
            int j = 0;
            String output = "";
            while(i < matchPosition.size()-1){
                for(;j< matchPosition.get(i);j++){
                    output = output + p[j];
                }
                output = output + " \"";
                for(;j< matchPosition.get(i+1);j++){
                    output = output + p[j];
                }
                output = output + "\" ";
                i = i+2;
            }
            for(; j < p.length; j++){
                output = output + p[j];
            }
            System.out.printf("The result of matching:\n%s\n",output);
        }
    }
}

class AutoMaton extends StringMatchingBase{
    public void matching(String pattern){

    }
}

public class stringMatching{
    public static void main(String[] args) {
        StringMatchingBase p = new Rabin_krap();
        p.matching(args[0]);
    }
}