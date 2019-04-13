import com.sun.media.jfxmedia.locator.ConnectionHolder;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.util.Pair;
import java.util.Hashtable;

class StringMatchingBase{
    public String context;
    public int d; //number of alphabat;
    public char[] alphabet = {'a','b','c'};
    public ArrayList<Integer> matchPosition;
    
    public StringMatchingBase(){
        context = "abcbaccabccbbacca"; //a trivial case for testing.
        this.matchPosition = new ArrayList<Integer>();
        this.d = 3;
    };

    public void matching(String pattern){
        return;
    }

    public void printResult(){
        char[] p = context.toCharArray();
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

class Rabin_krap extends StringMatchingBase{
    public int value_of_char(char a){
        if(a == 'a')
            return 0;
        else if(a == 'b')
            return 1; 
        else
            return 2;
    }

    public void matching(String pattern){
        char[] p = context.toCharArray();
        char[] q = pattern.toCharArray();
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
    }
}

class AutoMaton extends StringMatchingBase{
    Hashtable <Pair<Character,Integer>, Integer> t = new Hashtable<Pair<Character,Integer>, Integer>();

    public int ComputeTransitionFunction(char[] pattern, char a, int p){
        //char is the character we meet.
        //p is the length of matched string.
        
        //build the current string pattern[p] + a;
        char[] currentString = Arrays.copyOf(pattern, p+1);
        currentString[p] = a;

        //find the longest suffix
        int longest = 0;
        boolean isMatch = true; //whether currentString[p-i:p] is matched.
        for(int i = 0; i < p+1; i++){
            //scan the suffix of currentString to find the longest matching.
            isMatch = true;
            for(int j = 0; j<=i; j++){
                if (currentString[p-i+j] != pattern[j]){
                    isMatch = false;
                    break;
                }
            }
            if(isMatch)
                longest = i+1;
        }
        return longest;
    }

    public void FiniteAutoMatonMatcher(String pattern){
        // n is the length of pattern
        // build the table whose size is n*z, z is the size of alphabet
        char[] p = pattern.toCharArray(); 
        for(int i = 0; i < p.length; i++){
            for(int j = 0; j <alphabet.length; j++){
                Pair<Character,Integer> index = new Pair<Character,Integer>(alphabet[j],i);
                t.put(index ,ComputeTransitionFunction(p, alphabet[j], i));
            }
        }
    }

    public void testHashTable(String pattern){
        char[] p = pattern.toCharArray();
        System.out.printf("%c %c %c\n", alphabet[0], alphabet[1],alphabet[2]);
        for(int i = 0; i<p.length; i++){
            for(int j = 0; j<alphabet.length; j++){
                Pair<Character,Integer> key = new Pair<Character,Integer>(alphabet[j],i);
                System.out.printf("%d ",t.get(key));
            }
            System.out.printf("\n");
        }
    }

    public void matching(String pattern){
        //build DFA
        FiniteAutoMatonMatcher(pattern);
        //print the table;
        //scan the text;
        char[] p = context.toCharArray();
        int stage = 0;
        for(int i= 0; i < p.length; i++){
            Pair<Character,Integer> key = new Pair<Character,Integer>(p[i], stage);
            stage = t.get(key);
            //if matched
            if(stage == pattern.length()){
                matchPosition.add(i + 1 -pattern.length());
                matchPosition.add(i+1);
                stage = 0;
            }
        }
    }
}

public class stringMatching{

    public static void main(String[] args) {
        StringMatchingBase p = new AutoMaton();
        p.matching(args[0]);
        p.printResult();
    }
}
