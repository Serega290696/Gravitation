package being.mathematics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Test {
    //    private List<String> st = new ArrayList<String>();
    private List<String> st = new CopyOnWriteArrayList<String>();
//    private List<String> st = Collections.synchronizedList(new ArrayList<String>());

    private Object o = new Object();

    @org.junit.Test
    public void name8() throws Exception {
//        List<String> list = new ArrayList<>();
        List<String> list = new CopyOnWriteArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        new Thread(() -> {
            while (true) {
                for (String s : list) {
                    System.out.println("s = " + s);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        while (true) {
//            for (String s : list) {
            String s1 = String.valueOf(list.size() + 1);
            System.out.println("New s1 = " + s1);
            list.add(s1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            }
        }
    }

    @org.junit.Test
    public void name7() throws Exception {
        double x = 5 * Math.pow(10, 22);
        System.out.println(x);

    }

    @org.junit.Test
    public void name6() throws Exception {
        ThreeVector v1 = new ThreeVector(2, -2, 0);
        ThreeVector v2 = new ThreeVector(1, -1, 0);
//        ThreeVector v2 = new ThreeVector(-1, 1, 0);
//        ThreeVector v3 = new ThreeVector(-1, -1, 0);
//        ThreeVector v4 = new ThreeVector(1, -1, 0);
        System.out.println(v1.projectOn(v1));
        System.out.println(v1.projectRestOn(v2).angle());

    }

    @org.junit.Test
    public void name5() throws Exception {

        String fontFileName = "\\src\\main\\resources\\fonts\\neuro.ttf";
        String fontFileName2 = "src\\main\\resources\\fonts\\neuro.ttf";
        File fontFile = new File(fontFileName);
        File fontFile2 = new File(fontFileName2);
        System.out.println(fontFile.exists());
        System.out.println(fontFile2.exists());

    }

    @org.junit.Test
    public void name3() throws Exception {
        ThreeVector v1 = new ThreeVector(1, 1, 0);
        ThreeVector v2 = new ThreeVector(-1, 1, 0);
        ThreeVector v3 = new ThreeVector(-1, -1, 0);
        ThreeVector v4 = new ThreeVector(1, -1, 0);
        System.out.println("v1.angle() = " + v1.angle() + " ?= " + Math.PI / 4);
        System.out.println("v2.angle() = " + v2.angle() + " ?= " + Math.PI / 4 * 3);
        System.out.println("v3.angle() = " + v3.angle() + " ?= " + Math.PI / 4 * 5);
        System.out.println("v4.angle() = " + v4.angle() + " ?= " + Math.PI / 4 * 7);
    }

    @org.junit.Test
    public void name4() throws Exception {
        ThreeVector v1 = new ThreeVector(1, 1, 0);
        ThreeVector v2 = new ThreeVector(0, 1, 0);
        System.out.println(v2.projectOn(v1));
        System.out.println(v1.projectOn(v2));
    }

    @org.junit.Test
    public void name2() throws Exception {

        new Thread(() -> {
            while (true) {
                synchronized (o) {
                    System.out.println(Thread.currentThread().getName() + ". In");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + ". Out");
                }
                try {
                    Thread.sleep((long) (Math.random() * 20));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            synchronized (o) {
                System.out.println(Thread.currentThread().getName() + ". In");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + ". Out");
            }
            Thread.sleep((long) (Math.random() * 20));
        }
    }

    @org.junit.Test
    public void name() throws Exception {
        st.add("1");
        st.add("2");
        st.add("3");
        st.add("4");
        st.add("5");
        st.add("6");
        st.add("7");

        Thread thread = new Thread(() -> {
//            for (int i = 0; i < st.size(); i++) {
//                System.out.println("Remove: " + st.get(i));
//                st.remove(i);
//            }
            while (true) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (st.size() > 2 && Math.random() < 0.2) {
                    st.remove(0);
                } else if (Math.random() < 0.25) {
                    st.add(String.valueOf(Integer.parseInt(st.get(st.size() - 1)) + 1));
                }
            }

        });
        thread.start();
        while (true) {
            System.out.print("List: ");
            try {
                for (int i = 0; i < st.size(); i++) {
                    Thread.sleep(10);
                    System.out.print(st.get(i) + ", ");
                }
            } catch (Exception e) {
                System.err.println(e.getClass());
                thread.interrupt();
                for (int i = 0; i < st.size(); i++) {
//                    Thread.sleep(10);
                    System.out.print(st.get(i) + ", ");
                }
                System.exit(200);
            }
            System.out.println(".");
//            Thread.sleep(200);
        }


    }
}
