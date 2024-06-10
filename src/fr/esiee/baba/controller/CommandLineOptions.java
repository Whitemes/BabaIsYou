package fr.esiee.baba.controller;

import java.util.*;

/**
 * This class provides a mechanism to parse and store command-line options and their associated arguments.
 * It organizes the command-line inputs into a structured format that facilitates easy retrieval of options
 * and their corresponding argument lists.
 */
public class CommandLineOptions {
    /**
     * Stores command-line options and their arguments. Each option is a key in this map, and the value is
     * a list of lists containing the arguments associated with the option.
     */
    private final Map<String, List<List<String>>> options = new HashMap<>();

    /**
     * Constructor that initializes the parsing of command-line arguments.
     *
     * @param args the array of command-line arguments passed to the application.
     */
    public CommandLineOptions(String[] args) {
        parseOptions(args);
    }

    /**
     * Parses the command-line arguments and organizes them into a map where each option is mapped to
     * a list of argument lists.
     *
     * @param args the command-line arguments to be parsed.
     */
    private void parseOptions(String[] args) {
        List<String> currentOption = null;
        for (String arg : args) {
            if (arg.startsWith("--")) {
                currentOption = new ArrayList<>();
                options.put(arg, new ArrayList<>(Collections.singletonList(currentOption)));
            } else if (currentOption != null) {
                currentOption.add(arg);
            }
        }
    }

    /**
     * Retrieves the arguments associated with a specific option.
     *
     * @param option the option whose arguments are to be retrieved.
     * @return a list of lists of strings where each list contains arguments for the specified option.
     *         If the option does not exist, returns an empty list.
     */
    public List<List<String>> getOption(String option) {
        return options.getOrDefault(option, Collections.emptyList());
    }

    /**
     * Checks if the specified option is present in the command-line arguments.
     *
     * @param option the option to check.
     * @return true if the option exists, false otherwise.
     */
    public boolean hasOption(String option) {
        return options.containsKey(option);
    }
}
