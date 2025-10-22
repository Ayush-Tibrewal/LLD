import java.util.*;

class Heap{
    ArrayList<Integer> arr;
    int index;
    

    Heap(){
        arr = new ArrayList<>();
        index = 0;
    }
    
    int parent(int index){
        return (index-1)/2;
    }
    int left_index(int index){
        return index*2+1;
    }
    
    int right_index(int index){
        return index*2+2;
    }
    
     void put(int val){
        arr.add(val);
        uphead(arr.size()-1);
       
      
        }
        
       private void uphead(int index){
            while(index!=0){
                int parentindex  = parent(index);
                if(arr.get(index) < arr.get(parentindex)){
                    swap(index , parentindex);
                    index = parentindex;
                }else{
                    break;
                }
            }
        }
        
        private void uphead_rec(int index){
            if(index == 0) return;
            int parentindex  = parent(index);
                if(arr.get(index) < arr.get(parentindex)){
                    swap(index , parentindex);
                    uphead(parentindex);
                }
        }
        
        private int delect(){
            if(arr.isEmpty()){
                System.out.println("list is empty");
                return 0;
            }
            
            int temp = arr.get(0);
            int last = arr.get(arr.size()-1);
            arr.set(0 , last);
            downheap(0);
            return temp;
        }
        
       private void swap(int index , int index2){
            int temp = arr.get(index);
            arr.set(index , arr.get(index2));
            arr.set(index2 , temp);
        }
        
 private void downheap(int index) {
    int left = left_index(index);
    int right = right_index(index);
    int smallest = index;

    if (left < arr.size() && arr.get(left) < arr.get(smallest)) {
        smallest = left;
    }

    if (right < arr.size() && arr.get(right) < arr.get(smallest)) {
        smallest = right;
    }

    if (smallest != index) {
        swap(index, smallest);
        downheap(smallest);
    }
}


 public void show(){
    int index = 0;
    if(arr.isEmpty()) System.out.println("nothing present in the list");
    while(index!= arr.size()){
        System.out.print(arr.get(index++));
    }
}

}

public class Main{
    public static void main(String[] args){
        Heap map = new Heap();
        map.put(1);
        map.put(11);
        map.put(10);
        map.put(2);
        map.show();
    }
}
