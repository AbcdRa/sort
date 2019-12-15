import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Sort {
    private static String filename = "";
    private static String strSplitter = "[\n]";
    private static String wrdSplitter = "[ ,.]";
    private static boolean ignoreCase = false;
    private static int indexCmp = 0;
    private static boolean cmpWithLen = false;

    //Точка входа
    public static void main(String[] args) {
        initParams(args);
        String[] text = getTextFromFile(filename,strSplitter);
        sortText(text);
    }

    //Ф-ия которая пытается разобрать передаваемые параметры
    private static void initParams(String[] args) {
        //Если ничего не передано инициализируем по умолчанию
        if(args.length == 0) {
            System.out.println("Не передано параметров !!");
            filename = "C:\\text.txt";
            return;
        }
        //Если передан один аргумент интерпретируем его как имя файла
        if(args.length == 1) {
            if(args[0].equals("help")) {
                System.out.println("Первый аргумент = имя файл; второй аргумент -i(-) = игнор регистра(нет)");
                System.out.println("Третий аргумент -l(-) Сортировка по длине(по Алфавиту)");
                System.out.println("Четвертый аргумент = по какому слову сортировать");
                return;
            }
            filename = args[0];
            return;
        }
        if(args.length == 2) {
            filename = args[0];
            if(args[1].equals("-i")) {
                ignoreCase = true;
            }
        }
        if(args.length == 3) {
            filename = args[0];
            if(args[1].equals("-i")) {
                ignoreCase = true;
            }
            if(args[2].equals("-l")) {
                cmpWithLen = true;
            }
        }
        else {
            filename = args[0];
            if(args[1].equals("-i")) {
                ignoreCase = true;
            }
            if(args[2].equals("-l")) {
                cmpWithLen = true;
            }
            try {
                indexCmp = Integer.parseInt(args[3]);
            } catch (NumberFormatException ex) {
                System.out.println(args[3] + " не распознано как число");
                indexCmp = 0;
            }
        }
    }

    private static String[] getTextFromFile(String filename, String splitter) {
        String[] text = null;
        try (FileReader reader = new FileReader(filename))
        {
            int currentChar = reader.read();
            int indexStr = 0;
            String str = "";
            while(currentChar != -1)
            {
                str += (char) currentChar;
                currentChar = reader.read();
            }
            text = str.split(splitter);
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        return text;
    }

    private static void sortTextWABC(String[] text, boolean ignoreCase) {
        ArrayList<String> textList = new ArrayList<>(Arrays.asList(text));
        if(!ignoreCase) {
            textList.sort((s1, s2) -> (s1.compareTo(s2)));
        }
        else {
            textList.sort((s1, s2) -> (s1.compareToIgnoreCase(s2)));
        }
        textList.forEach(n -> System.out.println(n));
    }

    private static void sortTextWLen(String[] text) {
        ArrayList<String> textList = new ArrayList<>(Arrays.asList(text));
        textList.sort((s1,s2) -> (s1.length() - s2.length()));
        textList.forEach(n -> System.out.println(n) );
    }

    private static void sortText(String[] text) {
        if(indexCmp == 0) {
            if (!cmpWithLen) {
                sortTextWABC(text, ignoreCase);
                return;
            } else {
                sortTextWLen(text);
            }
        }
        else {
            ArrayList<String> textList = new ArrayList<>(Arrays.asList(text));
            Comparator<String> comp = cmpWithLen ? (s1, s2) -> (s1.length() - s2.length()) :
                    ignoreCase ? (s1, s2) -> (s1.compareToIgnoreCase(s2)) : (s1, s2) -> (s1.compareTo(s2));
            textList.sort((s1, s2) -> {
                String[] wrds1 = s1.split(wrdSplitter);
                String[] wrds2 = s2.split(wrdSplitter);
                String cmpWrd1 = wrds1.length < indexCmp ? wrds1[wrds1.length - 1] : wrds1[indexCmp - 1];
                String cmpWrd2 = wrds2.length < indexCmp ? wrds2[wrds2.length - 1] : wrds2[indexCmp - 1];
                return comp.compare(cmpWrd1, cmpWrd2);
            });
            textList.forEach(n -> System.out.println(n) );
        }
    }
}
