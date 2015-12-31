package cov.mjp.eclipsemap;

import java.util.LinkedHashMap;
import java.util.Map;

public class MappedObject {

    public String className;
    public Map<String, String> leafFields = new LinkedHashMap<>();
    public Map<String, MappedObject> treeFields = new LinkedHashMap<>();

    @Override
    public String toString() {
        return "[className=" + className 
                + (leafFields.isEmpty() ? "" : ", leafFields=" + leafFields) 
                + (treeFields.isEmpty() ? "" : ", treeFields=" + treeFields) 
                + "]";
    }

    public static MappedObject createFromString(String s) {
        MappedObject mo = new MappedObject();
        new Mapper(s).mapObject(mo);
        return mo;
    }

    private static class Mapper {

        private String s;

        public Mapper(String s) {
            this.s = s;
        }

        private String mapObject(MappedObject mo) {
            mo.className = removeFirstNCharacters(s.indexOf(' '));

            while (s.contains("=")) {
                removeLeadingNonLetters();
                String key = removeFirstNCharacters(s.indexOf('='));
                removeFirstNCharacters(1);  // remove the =
                String leafValue = getLeafValue();
                if (leafValue != null) {
                    mo.leafFields.put(key, leafValue);
                    if (s.startsWith("]")) {    // that was the last field in the tree
                        return s;
                    }
                } else {
                    MappedObject treeField = new MappedObject();
                    mo.treeFields.put(key, treeField);
                    s = new Mapper(s).mapObject(treeField);
                }
            }
            return s;   // s contains only close brackets - ]
        }

        private void removeLeadingNonLetters() {
            int i = 0;
            while (!Character.isLetter(s.charAt(i))) {
                i++;
            }
            removeFirstNCharacters(i);
        }

        private String removeFirstNCharacters(int n) {
            String value = s.substring(0, n);
            s = s.substring(value.length());
            return value;
        }

        private String getLeafValue() {
            int endIndex = getEndIndex();
            if (!s.contains("[") || s.indexOf('[') > endIndex) {
                return removeFirstNCharacters(endIndex);
            }
            return null;
        }

        /** The end of the value, if it's a leaf field. */
        private int getEndIndex() {
            if(s.contains(",")) {
                return Math.min(s.indexOf(','), s.indexOf(']'));
            }
            return s.indexOf(']');
        }
    }
}
