package Utilities;

import java.util.TreeMap;

import models.Term;

public class TermData {
    //class is used to store TermData that can be reused accross the app.
  private static TreeMap<String, Term> TermList = new TreeMap<>();

    public static TreeMap<String, Term> getTermList() {
        return TermList;
    }

    public static void setTermList(TreeMap<String, Term> termList) {
        TermList = termList;
    }

}
