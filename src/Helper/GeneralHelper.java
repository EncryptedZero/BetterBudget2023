package Helper;

// Just functions that may be used over multiple classes, that don't belong in an abstract class logically.
public class GeneralHelper {
    public static String cleanCategoryString(String pDirtyCategoryString){
        if(pDirtyCategoryString != null){
            return pDirtyCategoryString.toUpperCase().trim().replace(" ", "_");
        }
        else{
            return "";
        }
    }
}
