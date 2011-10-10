package PenoPlatinum;


import java.util.ArrayList;

/**
 *
 * @author MHGameWork
 */
public class ActionParameters {

    private ArrayList<Pair> list = new ArrayList<Pair>();

    public void SetParameter(String name, Object value) {
        Pair pair = getPair(name);
        if (pair == null) {
            pair = new Pair();
            pair.setKey(name);
        }

        pair.setValue(value);

    }

    public Object GetParameter(String name) {
        Pair pair = getPair(name);
        if (pair == null) {
            return null;
        }
        return pair.getValue();
    }

    private Pair getPair(String key) {
        for (int i = 0; i < list.size(); i++) {
            Pair p = list.get(i);
            if (p.getKey().equals(key)) {
                return p;
            }
        }
        return null;
    }

    private class Pair {

        private String key;
        private Object value;

        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key the key to set
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return the value
         */
        public Object getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(Object value) {
            this.value = value;
        }
    }
}
