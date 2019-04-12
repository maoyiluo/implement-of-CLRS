public class Rod_cutting{
    public int[] price;

    public Rod_cutting(){
        price = new int[]{0,1,5,8,9,10,17,17,20,24,30};
    }

    public void cut_the_rod(){
        int[] result = new int[price.length];
        for(int i = 0; i < result.length; i++){
            for(int j = 0; j<=i; j++){
                if(price[j]+result[i-j]>result[i]){
                    result[i] = price[j]+result[i-j];
                }
            }
            System.out.println(result[i]);
        }
    }

    public static void main(String[] args) {
        Rod_cutting rod = new Rod_cutting();
        rod.cut_the_rod();
    }
}