package vn.edu.hcmut.emrre.core.feature.wiki;

import info.bliki.api.Connector;
import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.api.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikiQuery.class);
    private static User user;
    static {
        user = new User("", "", "https://en.wikipedia.org/w/api.php");
        user.login();
    }

    public static List<Page> getArticals(String title) {
        List<Page> pages = Collections.emptyList();
        String titles[] = substrings(title);
        try {
            Connector connector = new Connector();
            CustomQuery query = new CustomQuery();
            query.titles(titles).redirects().prop("links|categories").aplimit(500);
            pages = connector.query(user, query);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return pages;
    }

    public static String[] substrings(String str) {
        String[] nonSenseWordsArr = { "these", "another", "this", "all", "no", "any", "the", "both", "that", "every",
                "some", "each", "neither", "either", "those", "an", "a" };
        for (String string : nonSenseWordsArr) {
            String regex = String.format("\\s%s\\s|\\s%s|%s\\s", string, string, string);
            str = str.toLowerCase().replaceAll(regex, "");
        }
        List<String> result = new ArrayList<String>();
        String[] arr = str.trim().split("\\s+");
        for (int i = 1; i < arr.length + 1; i++) {
            for (int j = 0; j < arr.length + 1 - i; j++) {
                List<String> tmp = new ArrayList<String>();
                for (int k = j; k < j + i; k++) {
                    tmp.add(arr[k]);
                }
                result.add(String.join(" ", tmp));
            }
        }
        return result.stream().toArray(String[]::new);
    }

    public static class CustomQuery extends Query {
        @Override
        public Query aplimit(int limit) {
            put("pllimit", "max");
            return this;
        }
    }
    
    public static void main(String[] args) {
        String str = "ventral hernia";
        List<Page> pages = WikiQuery.getArticals(str);
        for (Page page : pages) {
            System.out.println(page.sizeOfLinksList());
            for (int i = 0; i < 10; i++) {
                System.out.println(page.getLink(i));
            }
        }
        WikiDataWrapper dataWrapper = new WikiDataWrapper(pages);
        System.out.println(dataWrapper.toString());
    }
}