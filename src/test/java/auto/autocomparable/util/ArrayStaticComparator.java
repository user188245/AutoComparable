package auto.autocomparable.util;

public class ArrayStaticComparator {

    @SuppressWarnings("all")
    public static int compareArrayByInnerJoiningMethod(Comparable[] a, Comparable[] b){
        if(b.length > a.length)
            return compareArrayByInnerJoiningMethod(b,a);
        int e = 0;
        for(int i=0; i<b.length; i++){
            e = a[i].compareTo(b[i]);
            if(e != 0)
                return e;
        }
        return e;
    }

}
