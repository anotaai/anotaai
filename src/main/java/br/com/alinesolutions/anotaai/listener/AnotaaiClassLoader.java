package br.com.alinesolutions.anotaai.listener;
//

//import java.lang.reflect.Method;
//import java.util.List;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.com.alinesolutions.anotaai.infra.FileUtils;
import br.com.alinesolutions.anotaai.metadata.model.domain.Path;
import br.com.alinesolutions.anotaai.model.util.EnumSerialize;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.ClassFile;
import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ClassFilter;

/**
 * Application Lifecycle Listener implementation class AnotaaiClassLoader
 *
 */
@WebListener
public class AnotaaiClassLoader implements ServletContextListener {

	private static final Logger logger;

	static {
		logger = Logger.getLogger(AnotaaiClassLoader.class.getSimpleName());
	}

	/**
	 * Default constructor.
	 */
	public AnotaaiClassLoader() {
		super();
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {

	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Criando diretorio temporario");
		createPathTempFile();
		loadEnums(sce);
	}

	private void createPathTempFile() {
		String rootDir = File.separator;
		FileUtils.getInstance().setRootDir(rootDir);
		Arrays.asList(Path.values()).stream().forEach(path -> {
			String p = rootDir + path.getPath();
			logger.log(Level.CONFIG, "Create file: " + p);
			FileUtils.getInstance().createFile(p);
		});
	}

	private void loadEnums(ServletContextEvent sce) {
		// TODO incluir metodos nos enuns para deserilizacao
		List<Class<?>> classes = CPScanner.scanClasses(new ClassFilter().packageName("br.com.alinesolutions.anotaai.*").annotation(EnumSerialize.class));
		CtClass ctClass = null;
		try {
			for (Class<?> clazz : classes) {
				if (!Enum.class.isAssignableFrom(clazz)) {
					throw new RuntimeException("class " + clazz + " is not an instance of Enum");
				}
				ClassPool.getDefault().insertClassPath(new ClassClassPath(clazz));
				ctClass = ClassPool.getDefault().get(clazz.getName());
				ClassFile cf = ctClass.getClassFile();
				CtMethod m = CtNewMethod.make("public String getTypeTest() { return this.toString(); }", ctClass);
				// ctClass.addMethod(m);
				cf.addMethod(m.getMethodInfo());
				ctClass.writeFile();
				// Class<?> class1 = ctClass.toClass();
				// Method method = null;

				ctClass.getDeclaredMethod("getType");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
