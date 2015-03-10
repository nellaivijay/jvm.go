package jvmgo.cl;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import jvmgo.UnitTestRunner;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassLoaderTest {
    
    public static void main(String[] args) throws Exception {
        UnitTestRunner.run(ClassLoaderTest.class);
    }
    
    @Test
    public void sysClassLoader() {
        ClassLoader sysCl = ClassLoader.getSystemClassLoader();
        assertEquals("sun.misc.Launcher$AppClassLoader", sysCl.getClass().getName());
        
        ClassLoader extCl = sysCl.getParent();
        assertEquals("sun.misc.Launcher$ExtClassLoader", extCl.getClass().getName());
        
        ClassLoader bootCl = extCl.getParent();
        assertNull(bootCl);
    }
    
    @Test
    public void sysClassLoader2() {
        ClassLoader sysCl = ClassLoader.getSystemClassLoader();
        URLClassLoader urlCl = (URLClassLoader) sysCl;
        assertTrue(urlCl.getURLs().length > 0);
    }
    
    @Test
    public void getClassLoader() {
        ClassLoader bootCl = Object.class.getClassLoader();
        assertNull(bootCl);
        
        ClassLoader appCl = ClassLoaderTest.class.getClassLoader();
        ClassLoader sysCl = ClassLoader.getSystemClassLoader();
        assertSame(sysCl, appCl);
    }
    
    @Test
    public void loadClass() throws Exception {
        ClassLoader sysCl = ClassLoader.getSystemClassLoader();
        assertSame(Object.class, sysCl.loadClass("java.lang.Object"));
        assertSame(ClassLoaderTest.class, sysCl.loadClass("jvmgo.cl.ClassLoaderTest"));
    }
    
    @Test
    public void classNotFound() throws Exception {
        try {
            ClassLoader sysCl = ClassLoader.getSystemClassLoader();
            sysCl.loadClass("foo.bar.XXX");
            fail();
        } catch (ClassNotFoundException e) {
            assertEquals("foo.bar.XXX", e.getMessage());
        }
    }
    
    @Test
    public void getResource() {
        ClassLoader appCl = ClassLoaderTest.class.getClassLoader();
        //URL url = appCl.getResource("org/eclipse/jetty/http/mime.properties");
        URL url = appCl.getResource("LICENSE.txt");
        System.out.println(url);
        assertNotNull(url);
        
        InputStream is = appCl.getResourceAsStream("LICENSE.txt");
        assertNotNull(is);
    }
    
}
