package Tools;
public class Tools {
    public static final String regexDate = "\\d{4}-\\d{2}-\\d{2}";
    public static final String regexDouble = "^-?\\d+(\\.\\d+)?$";
    public static final String regexWeightInKilogram = "\\s*?(?i)[kK]g\\s*?";
    public static final String regexGallon = "\\s*?[lL]\\s*?";
    public static double stringWeightToDouble(String weight){
        String text = weight.replaceAll(regexWeightInKilogram,"");

        return Double.parseDouble(text);
    }
    public static String doubleWeightToString(double weight){
        return weight + "Kg";
    }
    public static double stringGallonToDouble(String gallon){
        String text = gallon.replaceAll(regexGallon,"");

        return Double.parseDouble(text);
    }
    public static String doubleGallonToString(double gallon){
        return gallon + "L";
    }
}
