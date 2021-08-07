package jvm;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;

public class HelloClassLoader extends ClassLoader{

    public static void main(String[] args) throws Exception{
        HelloClassLoader.handleFile();
        Class hello = new HelloClassLoader().findClass("Hello");
        Method[] methods = hello.getMethods();
        Arrays.asList(methods).forEach(method -> System.out.println(method.getName()));
        Method method = hello.getDeclaredMethod("hello");
        method.invoke(hello.newInstance());
    }
    @Override
    protected Class<?> findClass(String name) {
        String base64Code = "";
        try {
            base64Code = encodeBase64File("D:\\Hello.class");
            System.out.println("解密后的base64字符串："+base64Code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bytes = decode(base64Code);
        return defineClass(name,bytes,0,bytes.length);
    }

    public byte[] decode(String base64){
        return Base64.getDecoder().decode(base64);
    }

    public static void handleFile(){
        File inputfile = new File("C:\\Users\\Administrator\\Desktop\\Hello.xlass");
        File outFile = new File("D:\\Hello.class");
        InputStream in = null;
        FileOutputStream fos = null;

        try {
            // 一次读多个字节
            byte[] bytes = Files.readAllBytes(inputfile.toPath());
            for(int i = 0;i<bytes.length;i++){
                bytes[i] = (byte)(255-bytes[i]);
            }
            System.out.println("执行完成！");
            // 输出到D盘
            fos = new FileOutputStream(outFile);
            fos.write(bytes);
            fos.close();
            System.out.println("执行完成！");
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if(fos!=null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String encodeBase64File(String path) throws Exception {
        File  file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.getEncoder().encodeToString(buffer);
    }


}
