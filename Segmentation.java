import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Segmentation {


    private static Integer key(String str){
        Integer key = 0;
        for(int i = 0; i < str.length();i++){
            key += 32*i * str.charAt(i);
        }
        return key;
    }



    public static void main(String[] agr) throws FileNotFoundException {
        /*
        String fileForWord = "QiYi_1.txt";
        String fileForSegment = "QiYi_2.txt";
        arraySegmentation(fileForWord, fileForSegment);
        hashmapSegmentation(fileForWord, fileForSegment);
        */

        //提供词典的文档和待分词的文档相对应，即文字内容一样，从0到2文档依次变大
        String fileForWord_0 = "test0_1.txt";
        String fileForSegment_0 = "test0_2.txt";
        arraySegmentation(fileForWord_0, fileForSegment_0);
        hashmapSegmentation(fileForWord_0, fileForSegment_0);
        String fileForWord_1 = "test1_1.txt";
        String fileForSegment_1 = "test1_2.txt";
        arraySegmentation(fileForWord_1, fileForSegment_1);
        hashmapSegmentation(fileForWord_1, fileForSegment_1);
        String fileForWord_2 = "test2_1.txt";
        String fileForSegment_2 = "test1_2.txt";
        arraySegmentation(fileForWord_2, fileForSegment_2);
        hashmapSegmentation(fileForWord_2, fileForSegment_2);
        //提供词典的文档和待分词的文档不对应，提供词典的文档更大
        arraySegmentation(fileForWord_2, fileForSegment_0);
        hashmapSegmentation(fileForWord_2, fileForSegment_0);
        //提供词典的文档和待分词的文档不对应，待分词的文档更大
        arraySegmentation(fileForWord_0, fileForSegment_2);
        hashmapSegmentation(fileForWord_0, fileForSegment_2);



    }

    public static void arraySegmentation(String fileForWord,String fileForSegment) throws FileNotFoundException{
        long startTime = System.currentTimeMillis();    //获取开始时间

        ArrayList<String> array = new ArrayList<>();
        ArrayList<String> text = new ArrayList<>();
        int moreWordSum = 0;
        int maxWordLength = 0;
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileForWord),"gbk"));
            String str;			// 按行读取字符串
            while ((str = bf.readLine()) != null) {
                String[] ss = str.split("/");
                for(int j = 1; j < ss.length ;j++){
                    int flag = 0;
                    String w = ss[j].replaceAll("[a-zA-Z]","" ).replace(" ","");
                    if(w.length() > maxWordLength)
                        maxWordLength = w.length();
                    Integer key = key(w);
                    //System.out.print("111");
                    for(int k = 0; k< array.size();k++)
                    {
                        if(array.get(k).contentEquals(w))
                        {
                            flag = 1;
                            break;
                        }
                    }
                    if(flag == 0)
                    {
                        array.add(w);
                        if(w.length() == 26)
                            System.out.print(w + "\n");
                        if(w.length() > maxWordLength)
                            maxWordLength = w.length();
                    }

                }
            }
            bf.close();
            BufferedReader bff = new BufferedReader(new InputStreamReader(new FileInputStream(fileForSegment),"gbk"));
            //BufferedReader bff = new BufferedReader(new InputStreamReader(new FileInputStream("199801_sent.txt"),"gbk"));
            String s;			// 按行读取字符串
            int actualNum = 0;
            while ((s = bff.readLine()) != null) {
                for(int i = 0; i < s.length(); i++){
                    text.add(String.valueOf(s.charAt(i)));
                    // System.out.print(s.charAt(i));
                }
            }
            int startFlag = text.size() - 1;
            while(startFlag > 0){
                int checkOut = 0;
                int end = startFlag;
                startFlag = startFlag - maxWordLength;
                if(startFlag < 0)
                    startFlag = 0;
                // System.out.print(startFlag + "\n");
                int checkLength = maxWordLength;
                int num = 0;
                while(num < 26){
                    String check = text.get(startFlag); ;
                    for(int i = startFlag + 1;i < end;i++){
                        check = check + text.get(i);

                    }
                    //System.out.print(check + "\n");

                    for(int i = 0 ; i < array.size();i++){
                        if(check != null)
                            if(check.equals(array.get(i))){
                                checkOut = 1;
                                break;
                            }
                    }



                    if(checkOut == 0){
                        //说明没有被识别
                        if(check.length() == 1)
                        {
                            moreWordSum += 1;
                            actualNum += 1;
                            //System.out.print(moreWordSum);
                            break;
                        }


                        checkLength = checkLength - 1;
                        startFlag = startFlag +1;
                        num = num + 1;
                    }
                    else{
                        actualNum += 1;
                        break;
                    }

                }


            }
            long endTime = System.currentTimeMillis();    //获取结束时间
            System.out.print("\n词典中总词数"+ array.size()+"\n");
            System.out.print("被测文段总词数(包括重复)"+ actualNum+"\n");
            System.out.print("array time: " + (endTime - startTime) + "ms\n");
            double error = (double)moreWordSum/array.size();
            System.out.print(String .format("%.2f",(100 - 100* error)) );

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void hashmapSegmentation(String fileForWord,String fileForSegment) throws FileNotFoundException{
        long startTime = System.currentTimeMillis();    //获取开始时间

        ArrayList<String> text = new ArrayList<>();
        int moreWordSum = 0;
        int maxWordLength = 0;
        int maxOffSet = 0;
        HashMap<Integer,String> wordMAp = new HashMap<>();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileForWord),"gbk"));
            String str;			// 按行读取字符串
            while ((str = bf.readLine()) != null) {
                String[] ss = str.split("/");
                for(int j = 1; j < ss.length ;j++){
                    // int flag = 0;
                    String w = ss[j].replaceAll("[a-zA-Z]","" ).replace(" ","");
                    //System.out.print(w);
                    if(w.length() > maxWordLength){
                        maxWordLength = w.length();
                        //System.out.print(w);
                    }

                    Integer key = key(w);
                    if(!wordMAp.keySet().contains(key))
                        wordMAp.put(key,w);
                    else
                    {
                        if(!wordMAp.values().contains(w)){
                            int offset = 0;
                            while(wordMAp.keySet().contains(key)){
                                offset += 1;
                                key += 1;
                            }
                            if (offset > maxOffSet)
                                maxOffSet = offset;
                            wordMAp.put(key,w);
                        }

                    }
                }
            }
            bf.close();
            BufferedReader bff = new BufferedReader(new InputStreamReader(new FileInputStream(fileForSegment),"gbk"));
            //BufferedReader bff = new BufferedReader(new InputStreamReader(new FileInputStream("199801_sent.txt"),"gbk"));
            String s;			// 按行读取字符串
            while ((s = bff.readLine()) != null) {
                for(int i = 0; i < s.length(); i++){
                    text.add(String.valueOf(s.charAt(i)));
                    // System.out.print(s.charAt(i));
                }
            }
            //System.out.print("\n\n\n" + maxWordLength+ "\n\n\n");

            int startFlag = text.size() - 1;
            while(startFlag > 0){
                int checkOut = 0;
                int end = startFlag;
                startFlag = startFlag - maxWordLength;
                if(startFlag < 0)
                    startFlag = 0;
                // System.out.print(startFlag + "\n");
                int checkLength = maxWordLength;
                int num = 0;
                while(num < maxWordLength){
                    String check = text.get(startFlag); ;
                    for(int i = startFlag + 1;i < end;i++){
                        check = check + text.get(i);

                    }

                    int checkKey = key(check);
                    for(int i = 0;i < maxOffSet + 1;i++){
                        if(wordMAp.get(checkKey + i)!=null)
                            if(wordMAp.get(checkKey + i).equals(check)){
                                checkOut = 1;
                                break;
                            }
                    }

                    if(checkOut == 0){
                        //说明没有被识别
                        if(check.length() == 1)
                        {
                            moreWordSum += 1;
                            //System.out.print(check + "\\");
                            //System.out.print(moreWordSum);

                            break;
                        }


                        checkLength = checkLength - 1;
                        startFlag = startFlag +1;
                        num = num + 1;

                    }
                    else{
                        //System.out.print(check + "\\");
                        break;
                    }
                }


            }
            System.out.print(wordMAp.size()+ "\n");
            long endTime = System.currentTimeMillis();    //获取结束时间
            System.out.print("hashmap time : " + (endTime - startTime) + "ms\n");
            double error = (double)moreWordSum/wordMAp.size();
            System.out.print(String .format("%.2f",(100 - 100* error)) + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
