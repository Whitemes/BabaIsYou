package fr.esiee.baba.controller;

import java.util.*;

/**
 * This class provides functionality to parse command-line arguments.
 * It is designed to interpret a series of arguments passed to an application
 * and organize them into a structured format that maps options to their respective arguments.
 */
public class CommandLineParser {
	
	/**
	 * Default Constructor
	 */
	public CommandLineParser() {
		
	}
	
    /**
     * Parses the command-line arguments into a map where each option (a string starting with "--") 
     * is associated with a list of argument lists. This method groups arguments following an option 
     * into lists of up to three arguments. If more than three arguments follow an option without 
     * another option in between, they are grouped into a new list.
     *
     * @param args the command-line arguments to parse, expected to include options prefixed with "--"
     *             followed by any number of arguments that relate to each option.
     * @return a map where each key is an option (including the "--" prefix) and the value is a list of lists,
     *         each containing up to three arguments associated with the key. If an option is repeated,
     *         all arguments following each occurrence until the next option are grouped separately.
     * 
     */
    public static Map<String, List<List<String>>> parse(String[] args) {
        var options = new HashMap<String, List<List<String>>>();
        String currentOption = null;
        for (var arg : args) {
            if (arg.startsWith("--")) {
                currentOption = arg;
                options.putIfAbsent(currentOption, new ArrayList<>());
            } else if (currentOption != null) {
                if (options.get(currentOption).isEmpty() || options.get(currentOption).get(options.get(currentOption).size() - 1).size() == 3) {
                    options.get(currentOption).add(new ArrayList<>());
                }
                options.get(currentOption).get(options.get(currentOption).size() - 1).add(arg);
            }
        }
        return options;
    }
}
