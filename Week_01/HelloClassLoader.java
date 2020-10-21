
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader{
    public static void main(String[] args) {
        try {
            Class<?> clazz = new HelloClassLoader().findClass("Hello");
            Object obj = clazz.newInstance();
            Method m = clazz.getMethod("hello");
            m.invoke(obj);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = null;
        try{
            System.out.println(this.getResource("Hello.xlass").getPath());
            FileInputStream fis = new FileInputStream(new File(this.getResource("Hello.xlass").getPath()));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int v;
            while ((v = fis.read()) != -1){
                bos.write(255 - v);
            }
            b = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(name, b, 0, b.length);
    }
}
