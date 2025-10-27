package Ratelimit1;
//fixed window counter
import java.util.HashMap;

public interface RateLimit {
    boolean checkrate(String id);

}
class Window{
    long start  , counter;
    Window(long start, int counter) {
        this.start = start;
        this.counter =counter;
    }
    long getStart(){
        return start;
    }
    long getCounter(){
        return counter;
    }
}

class RateLimiting implements RateLimit{
    final int MAX_PER_USER = 5;
    final  long WINDOW =1000;
    HashMap<String , Window> map = new HashMap<>();

    @Override
    public boolean checkrate(String id){
        long currentTime = System.currentTimeMillis();
        if(!map.containsKey(id)){
            Window window = new Window(currentTime , 1 );
            map.put(id , window);
            System.out.println("added into the map"+id);
            return true;
        }else {
            Window win = map.get(id);
            if(currentTime-win.getStart()<=WINDOW){
                if(win.getCounter()< MAX_PER_USER){
                    win.counter+=1;
                    System.out.println("counter request has been updated"+id);
                    return true;
                }else{
                    System.out.println("limit crossed");
                    return false;
                }
            }else{
                System.out.println("time reset"+id);
                win.start= currentTime;
                win.counter=1;
                return true;
            }
        }
    }
}
class main{
    public static void main(String[] args) throws InterruptedException {
        RateLimiting rateLimiting = new RateLimiting();
        for(int i = 0 ;i< 10; i++){
            rateLimiting.checkrate("customerid"+i);
            rateLimiting.checkrate("customerid"+(10-i));

            Thread.sleep(500);
        }
    }
}
