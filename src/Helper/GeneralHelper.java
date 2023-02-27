package Helper;

public class GeneralHelper {
    public static String cleanCategoryString(String pDirtyCategoryString){
        return pDirtyCategoryString.toUpperCase().trim().replace(" ", "_");
    }
}
