package go.takethespread;


public class UserDaoImplTest {
    public static void main(String[] args) {
        Test t = new Test("ABALSDS");
        System.out.println(t.getName());
        testChange(t);
        System.out.println(t.getName());


    }

    public static void testChange(Test t){
        t.setName("HAHA, CHANGED");
    }


    static class Test {
        private String name;

        public Test(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
