import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Test
 * @Description TODO
 * @Author zehao
 * @Date 2020/5/14/014 16:10
 * @Version 1.0
 **/
public class Test {

    public static void main(String[] args){

        List<Integer> list = new ArrayList<>();
        list.add(2);
        Test t = new Test();
        System.out.println(list.size());
        t.insert(list);
        System.out.println(list.size());
    }
    private void insert(List<Integer> list){
        list.add(1);
    }
}
