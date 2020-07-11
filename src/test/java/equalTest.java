import java.util.Objects;

/**
 * @ClassName equalTest
 * @Description TODO
 * @Author zehao
 * @Date 2020/7/9 16:22
 * @Version 1.0
 **/
class User{
    private String name;
    private int age;

    User(String name,int age){
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
public class equalTest {
    public static void main(String[] args) {
        User user1 = new User("lihua",13);
        User user2 = new User("lihua",13);
        System.out.println(user1.hashCode() + " -- "+user2.hashCode());
        System.out.println(user1 == user2);
        System.out.println(user1.equals(user2));

        String str1 = "string";
        String str2 = new String("string");
        String str3 = new String("string");
        String str4 = String.valueOf("string");

        System.out.println(str1 == str2);
        System.out.println(str3 == str2);
        System.out.println(str3 == str4);
        System.out.println(str1 == str4);
        System.out.println(str1.equals(str2));
    }


}
