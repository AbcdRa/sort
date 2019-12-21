import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Sort {
    private static String filename = "C:\\text.txt";
    private static String strSplitter = "[\n]";
    private static String wrdSplitter = "[ ,.]*";
    private static boolean ignoreCase = false;
    private static int indexCmp = 0;
    private static boolean cmpWithLen = false;
    private static boolean isReverse = false;

    //Точка входа
    public static void main(String[] args) {
        initParams(args);
        String[] text = getTextFromFile(filename,strSplitter);
        if(text == null) {
            System.out.println("Текст не инициализирован");
            return;
        }
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
        //Но если этот аргумент help то выводим справку;
        if(args[0].equals("help")) {
            System.out.println("Первый аргумент = имя файл; второй аргумент -i(-) = игнор регистра(нет)");
            System.out.println("Третий аргумент -l(-) Сортировка по длине(по Алфавиту)");
            System.out.println("Четвертый аргумент 0, 9 = по какому слову сортировать(0- по всему тексту)");
            System.out.println("Пятый аргумент -r(-) = Сортировка в обратном порядке(нет)");
            return;
        }
        System.out.println("Распознан один аргумент - " + args[0]);
        filename = args[0];

        //Если аргументов два и больше
        if(args.length > 1) {
            System.out.println("Разпознан второй аргумент - " + args[1]);
            //И второй аргумент == -i то флаг игнорирования регистра = true
            if(args[1].equals("-i")) {
                ignoreCase = true;
            }
        }

        if(args.length > 2) {
            System.out.println("Распознан третий аргумент - " + args[2]);
            if(args[2].equals("-l")) {
                cmpWithLen = true;
            }
        }

        if(args.length > 3) {
            System.out.println("Распознан 3 аргумент - " + args[3]);
            try {
                //Так как аргумент это строка, а нам нужно число
                indexCmp = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                System.out.println(args[3] + " не распознано как число");
                indexCmp = 0;
            }
        }

        if(args.length > 4) {
            System.out.println("Распознан 4 аргумент - " + args[4]);
            if(args[4].equals("-r")) {
                isReverse = true;
            }
        }
    }

    //Функция извлекающая строки из файла с именем filename, и разделяющая их
    //по регулярному выражения заданному в splitter
    private static String[] getTextFromFile(String filename, String splitter) {
        String[] text = null;
        try (FileReader reader = new FileReader(filename))
        {
            int currentChar = reader.read();
            //StringBuilder как String но в циклах работает быстрее
            StringBuilder str = new StringBuilder();
            while(currentChar != -1)
            {
                str.append((char) currentChar);
                currentChar = reader.read();
            }
            text = str.toString().split(splitter);
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        return text;
    }

    private static void sortText(String[] text) {
        ArrayList<String> textList = new ArrayList<>(Arrays.asList(text));
        if(indexCmp == 0) {
            Comparator<String> comp = getComparator();
            textList.sort(comp);
        }
        else {
            Comparator<String> comp = getComparator();
            textList.sort((s1, s2) -> {
                //Разделяем строки на слова
                String[] words1 = s1.split(wrdSplitter);
                String[] words2 = s2.split(wrdSplitter);
                //Смотрим если длина не позволяет нам взять indexCmp(слов не хватает)
                //То берем последний индекс
                String cmpWrd1 = words1.length < indexCmp ? words1[words1.length - 1] : words1[indexCmp - 1];
                String cmpWrd2 = words2.length < indexCmp ? words2[words2.length - 1] : words2[indexCmp - 1];
                return comp.compare(cmpWrd1, cmpWrd2);
            });
        }
        textList.forEach(System.out::println);
    }

    //Используя статические поля строит нам компаратор мечты
    private static Comparator<String> getComparator() {
        Comparator<String> comp = Comparator.naturalOrder();
        if(ignoreCase) {
            comp = String::compareToIgnoreCase;
        }
        if(cmpWithLen) {
            comp = Comparator.comparingInt(String::length);
        }
        if(isReverse) {
            comp = comp.reversed();
        }
        return comp;

    }
}
