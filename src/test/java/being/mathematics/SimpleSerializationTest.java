package being.mathematics;

import being.elements.Atom;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Serega on 21.03.2017.
 */
public class SimpleSerializationTest {
    @org.junit.Test
    public void testHashMap() throws Exception {
        Map<String, List<Atom>> hmap = new HashMap<String, List<Atom>>();
        ArrayList<Atom> list = new ArrayList<>();
        ArrayList<Atom> list1 = new ArrayList<>();
        ArrayList<Atom> list2 = new ArrayList<>();
        list.add(new Atom.AtomCreator().setName("aaa1-1").build());
        list.add(new Atom.AtomCreator().setName("aaa1-2").build());
        list.add(new Atom.AtomCreator().setName("aaa1-3").build());
        list.add(new Atom.AtomCreator().setName("aaa2-1").build());
        list.add(new Atom.AtomCreator().setName("aaa2-2").build());
        list.add(new Atom.AtomCreator().setName("aaa2-3").build());
        list.add(new Atom.AtomCreator().setName("aaa3-1").build());
        list.add(new Atom.AtomCreator().setName("aaa3-2").build());
        list.add(new Atom.AtomCreator().setName("aaa3-3").build());
        hmap.put("key", list);
        hmap.put("key1", list1);
        hmap.put("key2", list2);
//        HashMap<String, String> hmap = new HashMap<String, String>() {{
//            put(new String("key"), new String("value"));
//        }};

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        out = new ObjectOutputStream(bos);
        out.writeObject(hmap);
        byte[] yourBytes = bos.toByteArray();
        if (out != null) {
            out.close();
        }
        bos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
        ObjectInput in = null;
        in = new ObjectInputStream(bis);
        Object o = in.readObject();
        bis.close();
        if (in != null) {
            in.close();
        }
        Map<String, List<Atom>>  actual = (Map<String, List<Atom>>) o;

        for (String title : hmap.keySet()) {
            if(!hmap.get(title).equals(actual.get(title))){
                System.out.println("NOT EQUAL ("+title+"): ");
                System.out.println("hmap.get(title) = " + hmap.get(title));
                System.out.println("actual.get(title) = " + actual.get(title));
            }
        }
        assertEquals(hmap, o);
    }
}
