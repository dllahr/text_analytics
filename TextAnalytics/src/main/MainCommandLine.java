package main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import controller.stockPrices.update.WebReaderHeaderMismatchException;
import controller.util.CommandLineParserUnrecognizedTokenException;

public class MainCommandLine {
	
	private static final Class<?>[] classArray = {MainAddOrReplaceStockData.class, MainCreatePredictionModelSmoothingCoefs.class, 
		MainFindDuplicateArticles.class, MainGeneratePredictions.class, MainGeneratePrincipalComponentValues.class,
		MainLoadArticles.class, MainLoadEigPrincComp.class, MainLoadRegressionModel.class, MainTest.class};
	

	/**
	 * @param args
	 * @throws WebReaderHeaderMismatchException 
	 * @throws CommandLineParserUnrecognizedTokenException 
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static void main(String[] args) throws IOException, ParseException, CommandLineParserUnrecognizedTokenException, WebReaderHeaderMismatchException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Map<String, Method> nameMethodMap = buildNameMethodMap();
		
		if (args.length == 0) {
			System.out.println("no command specified, please choose one of:");
			printCommandNames(nameMethodMap.keySet());
			return;
		}
		
		final String cmdName = args[0];
		
		String[] newArgs = new String[args.length - 1]; 
		if (args.length > 1) {
			System.arraycopy(args, 1, newArgs, 0, newArgs.length);
		}		
	
		
		
		Method method = nameMethodMap.get(cmdName.toLowerCase());
		if (method != null) {
			method.invoke(null, (Object)newArgs);
		} else {
			System.out.println("that command was not recognized:  " + cmdName);
			printCommandNames(nameMethodMap.keySet());
		}
	}
	
	static Map<String, Method> buildNameMethodMap() throws NoSuchMethodException, SecurityException {
		Map<String, Method> result = new HashMap<String, Method>();

		for (Class<?> curClass : classArray) {
			result.put(curClass.getSimpleName().toLowerCase(), curClass.getMethod("main", String[].class));
		}
		
		return result;
	}	
	
	static void printCommandNames(Collection<String> commandNameColl) {
		for (String commandName : commandNameColl) {
			System.out.println(commandName);
		}
	}
}
